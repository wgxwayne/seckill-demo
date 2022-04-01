package indi.wgx.seckill.controller;

import indi.wgx.seckill.service.IUserService;
import indi.wgx.seckill.service.impl.UserServiceImpl;
import indi.wgx.seckill.vo.LoginVo;
import indi.wgx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author xiaowei
 * @date 2022/3/18 16:04
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Resource(name="userServiceImpl")
    IUserService userService;

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        return userService.doLogin(loginVo, request, response);
    }
}
