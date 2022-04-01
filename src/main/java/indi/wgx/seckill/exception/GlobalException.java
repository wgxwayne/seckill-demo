package indi.wgx.seckill.exception;

import indi.wgx.seckill.vo.RespBeanEnum;

/**
 * 全局异常
 *
 * @author xiaowei
 * @date 2022/3/18 17:52
 */


public class GlobalException extends RuntimeException {

    private RespBeanEnum respBeanEnum;

    public RespBeanEnum getRespBeanEnum() {
        return respBeanEnum;
    }

    public void setRespBeanEnum(RespBeanEnum respBeanEnum) {
        this.respBeanEnum = respBeanEnum;
    }

    public GlobalException(RespBeanEnum respBeanEnum) {
        this.respBeanEnum = respBeanEnum;
    }
}
