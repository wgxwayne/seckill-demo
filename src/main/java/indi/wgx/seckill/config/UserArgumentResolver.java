package indi.wgx.seckill.config;

import indi.wgx.seckill.pojo.User;
import indi.wgx.seckill.service.IUserService;
import indi.wgx.seckill.utils.CookieUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义用户参数
 *
 * @Author xiaowei
 * @Date 2022/3/19 22:46
 * @Description:
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserContext.getUser();
//        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse nativeResponse = webRequest.getNativeResponse(HttpServletResponse.class);
//        String userTicket = CookieUtil.getCookieValue(nativeRequest, "userTicket");
//        if (StringUtils.isEmpty(userTicket)) {
//            return null;
//        }
//        return userService.getUserByCookie(userTicket, nativeRequest, nativeResponse);
    }
}
