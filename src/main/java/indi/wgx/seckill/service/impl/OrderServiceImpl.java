package indi.wgx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.wgx.seckill.exception.GlobalException;
import indi.wgx.seckill.mapper.OrderMapper;
import indi.wgx.seckill.pojo.Order;
import indi.wgx.seckill.pojo.SeckillGoods;
import indi.wgx.seckill.pojo.SeckillOrder;
import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.service.IGoodsService;
import indi.wgx.seckill.service.IOrderService;
import indi.wgx.seckill.service.ISeckillGoodsService;
import indi.wgx.seckill.service.ISeckillOrderService;
import indi.wgx.seckill.utils.MD5Util;
import indi.wgx.seckill.utils.UUIDUtil;
import indi.wgx.seckill.vo.GoodsVo;
import indi.wgx.seckill.vo.OrderDetailVo;
import indi.wgx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Resource(name = "seckillGoodsServiceImpl")
    private ISeckillGoodsService seckillGoodsService;

    @Resource(name = "seckillOrderServiceImpl")
    private ISeckillOrderService seckillOrderService;

    @Resource(name = "orderMapper")
    private OrderMapper orderMapper;

    @Resource(name = "goodsServiceImpl")
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;




    /**
     * 秒杀下订单功能
     *
     * @param user
     * @param goods
     * @return
     */
    @Override
    @Transactional  // 添加事务控制并不能保证减库存方法secKill()执行的原子性，代码仍然会并发执行。
    public Order seckill(User user, GoodsVo goods) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // 秒杀商品表减库
        // 先查询库存，而不是拿前端传过来的库存量
        QueryWrapper<SeckillGoods> goodsQueryWrapper = new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId());
        SeckillGoods seckillGoods = seckillGoodsService.getOne(goodsQueryWrapper);
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);

//        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
//                //用SQL语句扣库存
//                .setSql("stock_count = " + "stock_count-1")
//                .eq("goods_id", goods.getId())
//                // 判断库存大于0
//                .gt("stock_count", 0)
//        );
//        // 更新失败，不创建订单
//        if (!seckillGoodsResult) {
//            return null;
//        }

        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                //用SQL语句扣库存
                .setSql("stock_count = " + "stock_count-" +
                        "1")
                .eq("goods_id", goods.getId())
                // 判断库存大于0
                .gt("stock_count", 0)
        );


        if (seckillGoods.getStockCount() < 1) {
            valueOperations.set("isStockEmpty" + goods.getId(), "0");
            return null;
        }

        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        // 将订单插入到数据表中
        orderMapper.insert(order);

        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        // 下单成功后，将订单信息存入Redis
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);
        return order;
    }


    @Override
    public OrderDetailVo detail(Long orderId) {
        // 订单不存在
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodsVo);
        return orderDetailVo;
    }

    /**
     * 生成路径
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        // 存在Redis中，设置失效时间
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 1, TimeUnit.MINUTES);
        return str;
    }


    /**
     * 校验路径
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        // 和Redis中的路径比较，查看是否一致
        return path.equals(redisPath);
    }

    /**
     * 校验验证码
     *
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
