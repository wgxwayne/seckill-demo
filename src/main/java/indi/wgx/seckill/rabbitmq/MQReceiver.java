package indi.wgx.seckill.rabbitmq;

import indi.wgx.seckill.pojo.SeckillMessage;
import indi.wgx.seckill.pojo.SeckillOrder;
import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.service.IGoodsService;
import indi.wgx.seckill.service.IOrderService;
import indi.wgx.seckill.utils.JsonUtil;
import indi.wgx.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author xiaowei
 * @Date 2022/3/23 23:08
 * @Description:
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Resource(name = "goodsServiceImpl")
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "orderServiceImpl")
    private IOrderService orderService;

    /**
     * 对消息进行消费
     *
     * @param
     */
    // 监听，消费消息
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接收消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return;
        }
        //下单操作
        orderService.seckill(user, goodsVo);
    }
}
