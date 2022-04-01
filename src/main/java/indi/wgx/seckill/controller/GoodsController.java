package indi.wgx.seckill.controller;

import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.service.IGoodsService;
import indi.wgx.seckill.service.IUserService;
import indi.wgx.seckill.vo.DetailVo;
import indi.wgx.seckill.vo.GoodsVo;
import indi.wgx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaowei
 * @date 2022/3/18 23:51
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource(name="userServiceImpl")
    private IUserService userService;

    @Resource(name = "goodsServiceImpl")
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        // Redis页面缓存, 如果不为空, 直接返回页面
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 如果为空, 则手动渲染, 并存入Redis
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());

        // 手动渲染
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
            // 存入 Redis,过期时间为60s
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;

    }

    /**
     * 跳转商品详情页
     *
     * @param goodsId
     * @param
     * @param user
     * @return
     */
//    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toDetail2(@PathVariable Long goodsId, Model model, User user, HttpServletRequest request, HttpServletResponse response) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        String html = valueOperations.get("goodsDetail" + goodsId);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
//
//        // 如果为空，则手动去获取
//        model.addAttribute("user", user);
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        Date startDate = goodsVo.getStartDate();
//        Date endDate = goodsVo.getEndDate();
//        Date nowDate = new Date();
//        //秒杀状态
//        int seckillStatus = 0;
//        //秒杀倒计时
//        int remainSeconds = 0;
//
//        if (nowDate.before(startDate)) {
//            //0:秒杀还未开始
//            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
//        } else if (nowDate.after(endDate)) {
//            //2:秒杀已经结束
//            seckillStatus = 2;
//            remainSeconds = -1;
//        } else {
//            //1:秒杀进行中
//            seckillStatus = 1;
//            // remainSeconds默认为0
//        }
//
//        model.addAttribute("seckillStatus", seckillStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//        model.addAttribute("goods", goodsVo);
//
//        // 手动渲染
//        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
//        if (!StringUtils.isEmpty(html)) {
//            // 存入 Redis,过期时间为60s
//            valueOperations.set("goodsDetail" + goodsId, html, 60, TimeUnit.SECONDS);
//        }
//        return html;
//    }

    @RequestMapping(value = "/detail/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId) {

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;

        if (nowDate.before(startDate)) {
            //秒杀还未开始0
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            //秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            seckillStatus = 1;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(seckillStatus);
        return RespBean.success(detailVo);
    }
}
