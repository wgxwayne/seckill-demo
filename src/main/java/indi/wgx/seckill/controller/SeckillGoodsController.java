package indi.wgx.seckill.controller;

import com.wf.captcha.ArithmeticCaptcha;
import indi.wgx.seckill.config.AccessLimit;
import indi.wgx.seckill.exception.GlobalException;
import indi.wgx.seckill.pojo.SeckillMessage;
import indi.wgx.seckill.pojo.SeckillOrder;
import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.rabbitmq.MQSender;
import indi.wgx.seckill.service.IGoodsService;
import indi.wgx.seckill.service.IOrderService;
import indi.wgx.seckill.service.ISeckillOrderService;
import indi.wgx.seckill.utils.JsonUtil;
import indi.wgx.seckill.vo.GoodsVo;
import indi.wgx.seckill.vo.RespBean;
import indi.wgx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 秒杀商品表 前端控制器
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Controller
@Slf4j
@RequestMapping("/seckill")
public class SeckillGoodsController implements InitializingBean {

    @Resource(name = "goodsServiceImpl")
    private IGoodsService goodsService;

    @Resource(name = "seckillOrderServiceImpl")
    private ISeckillOrderService seckillOrderService;

    @Resource(name = "orderServiceImpl")
    private IOrderService orderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisScript<Long> redisScript;

    // 内存标记
    private final Map<Long, Boolean> emptyStockMap = new HashMap<>(0);

    /**
     *
     * @param
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId) {
        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // 校验秒杀地址
        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }


        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (null != seckillOrder) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        // 通过内存标记减少Redis的访问
        if(emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 在Redis预减库存
        // 递减之后的库存,判断库存是否充足
        // 预减库存，递减和递增其实也是原子性的，也可以用lua脚本
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);


        if (stock < 0) {
            emptyStockMap.put(goodsId, true);  // 库存不足
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 下订单
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        // 将对象转化为字符串，发送到消息队列中
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        // 0 表示正在排队中
        return RespBean.success(0);
    }


    /**
     * 将系统初始化、库存加载到Redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVoList)) {
            return;
        }
        goodsVoList.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);  // 最开始有库存
        });
    }

    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/getResult")
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }


    /**
     * 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @param captcha
     * @param request
     * @return
     */

    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @GetMapping(value = "/path")
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();


        // 这块内容可以用注解代替
//        //  限制访问次数，5秒内访问5次 （使用计数器算法）
//        String uri = request.getRequestURI();
//        captcha = "0";
//        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
//        if (count == null) {
//            valueOperations.set(uri + ":" + user.getId(), 1, 5, TimeUnit.SECONDS);
//        } else if (count < 5) {
//            valueOperations.increment(uri + ":" + user.getId());
//        } else {
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
//        }


        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }


    /**
     * 获取验证码
     *
     * @param user
     * @param goodsId
     * @param response
     */
    @GetMapping(value = "/captcha")
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if (user == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // 设置请求头为输出图片的类型
        response.setContentType("image/jpg");
        // 没有缓存
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        // 验证码存入Redis
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 30, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }

}
