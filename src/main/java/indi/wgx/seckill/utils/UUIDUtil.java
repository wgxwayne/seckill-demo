package indi.wgx.seckill.utils;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author xiaowei
 * @date 2022/3/18 17:55
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
