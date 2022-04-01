package indi.wgx.seckill.vo;

import indi.wgx.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xiaowei
 * @Date 2022/3/20 16:16
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private User User;

    private GoodsVo goodsVo;

    /**
     * 秒杀状态，0：秒杀倒计时 1：秒杀进行中，2：秒杀已结束
     */
    private int secKillStatus;

    /**
     * 秒杀之前剩余时间（秒杀未开始）
     */
    private int remainSeconds;
}
