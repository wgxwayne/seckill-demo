package indi.wgx.seckill.controller;


import indi.wgx.seckill.rabbitmq.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    MQSender mqSender;


//    /**
//     * 测试发送rabbitMQ消息
//     */
//    @RequestMapping(value = "/mq")
//    @ResponseBody
//    public void mq() {
//        mqSender.send("Hello");
//    }

}
