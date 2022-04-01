package indi.wgx.seckill.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author xiaowei
 * @Date 2022/3/23 23:03
 * @Description:
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg) {
//        log.info("发送消息:" + msg);
//        rabbitTemplate.convertAndSend("queue", msg);
//    }

    /**
     * 发送秒杀信息
     * @param message
     * @return void
     **/
    public void sendSeckillMessage(String message) {
        log.info("发送消息" + message);
        // convertAndSend:使用此方法，交换机会马上把所有的信息都交给所有的消费者，消费者再自行处理，不会因为消费者处理慢而阻塞线程。
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }
}
