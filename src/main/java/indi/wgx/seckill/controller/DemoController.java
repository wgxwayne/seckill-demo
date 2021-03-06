package indi.wgx.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaowei
 * @date 2022/3/18 13:16
 */

@Controller
@RequestMapping("/demo")
public class DemoController {

    /**
     * 功能描述：测试页面跳转
     * @param model
     * @return
     */
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xiaowei");
        return "hello";
    }
}
