package indi.wgx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.wgx.seckill.pojo.Order;
import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.vo.GoodsVo;
import indi.wgx.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
public interface IOrderService extends IService<Order> {


    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    /**
     * 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 校验秒杀地址
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    /**
     * 验证码校验
     *
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
