package indi.wgx.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5工具类
 *
 * @author xiaowei
 * @date 2022/3/18 14:20
 */
public class MD5Util {
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 第一次加密
     * 前端 -> 后端
     **/
    public static String inputPassToFromPass(String inputPass) {
        String str = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 第二次加密
     * 后端 -> 数据库
     */
    public static String fromPassToDBPass(String fromPass, String salt) {
        String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 两次加密流程
     */
    public static String inputPassToDBPass(String inputPass, String salt) {
        String fromPass = inputPassToFromPass(inputPass);
        return fromPassToDBPass(fromPass, salt);
    }


    public static void main(String[] args) {
        String str = "123456";
        String s = inputPassToDBPass(str, salt);
        System.out.println(s);

        String s1 = inputPassToFromPass(str);
        System.out.println(s1);
        String s2 = fromPassToDBPass(s1, salt);
        System.out.println(s2);
        System.out.println(fromPassToDBPass("ce21b747de5af71ab5c2e20ff0a60eea", "1a2b3c4d"));
        System.out.println(fromPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9", "1a2b3c4d"));

        /**
         * 0687f9701bca74827fcefcd7e743d179
         * 第一次：ce21b747de5af71ab5c2e20ff0a60eea
         * 第二次：0687f9701bca74827fcefcd7e743d179
         */
    }
}
