package indi.wgx.seckill.config;


import indi.wgx.seckill.pojo.User;

/**
 * @author:
 * @date
 * @ClassName: UserContext
 */
public class UserContext {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User tUser) {
        userThreadLocal.set(tUser);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }
}
