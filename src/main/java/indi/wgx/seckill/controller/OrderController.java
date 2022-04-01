package indi.wgx.seckill.controller;

import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.service.IOrderService;
import indi.wgx.seckill.vo.OrderDetailVo;
import indi.wgx.seckill.vo.RespBean;
import indi.wgx.seckill.vo.RespBeanEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource(name = "orderServiceImpl")
    private IOrderService orderService;


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public RespBean detail(User tUser, Long orderId) {
        if (tUser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo orderDetailVo = orderService.detail(orderId);
        return RespBean.success(orderDetailVo);
    }

}
