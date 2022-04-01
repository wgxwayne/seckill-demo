package indi.wgx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-18
 */
@Data
@TableName("t_user")
//@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "用户ID,手机号码")
    @TableId("id")
    private Long id;

//    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

//    @ApiModelProperty(value = "MD5(MD5(pass明文+固定salt)+salt)")
    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

//    @ApiModelProperty(value = "头像")
    @TableField("head")
    private String head;

//    @ApiModelProperty(value = "注册时间")
    @TableField("register_date")
    private Date registerDate;

//    @ApiModelProperty(value = "最后一次登录事件")
    @TableField("last_login_date")
    private Date lastLoginDate;

//    @ApiModelProperty(value = "登录次数")
    @TableField("login_count")
    private Integer loginCount;

    public User() {
    }

    public User(Long id, String nickname, String password, String salt, String head, Date registerDate, Date lastLoginDate, Integer loginCount) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.salt = salt;
        this.head = head;
        this.registerDate = registerDate;
        this.lastLoginDate = lastLoginDate;
        this.loginCount = loginCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }
}
