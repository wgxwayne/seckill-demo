package indi.wgx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.vo.LoginVo;
import indi.wgx.seckill.vo.RespBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-18
 */
public interface IUserService extends IService<User> {
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    // 根据cookie获取用户
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
