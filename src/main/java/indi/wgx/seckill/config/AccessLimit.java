package indi.wgx.seckill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: LC
 * @date 2022/3/9 4:25 下午
 * @ClassName: AccessLimit
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    // 时间
    int second();

    // 对应的时间之内刷新几次
    int maxCount();

    // 是否需要登录
    boolean needLogin() default true;
}
