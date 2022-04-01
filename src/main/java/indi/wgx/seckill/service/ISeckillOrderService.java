package indi.wgx.seckill.service;


import com.baomidou.mybatisplus.extension.service.IService;
import indi.wgx.seckill.pojo.SeckillOrder;
import indi.wgx.seckill.pojo.User;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 获取秒杀结果
     *
     * @param tUser
     * @param goodsId
     * @return orderId 成功 ；-1 秒杀失败 ；0 排队中
     * @author LiChao
     * @operation add
     * @date 7:07 下午 2022/3/8
     **/
    Long getResult(User tUser, Long goodsId);

}
