package indi.wgx.seckill.vo;

import javax.validation.constraints.NotNull;
import indi.wgx.seckill.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

/**
 * 登录传参
 *
 * @author xiaowei
 * @date 2022/3/18 17:38
 */

public class LoginVo {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

