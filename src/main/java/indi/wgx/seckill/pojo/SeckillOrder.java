package indi.wgx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 秒杀订单表
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_seckill_order")
//@ApiModel(value="TSeckillOrder对象", description="秒杀订单表")
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "秒杀订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

//    @ApiModelProperty(value = "订单ID")
    @TableField("order_id")
    private Long orderId;

//    @ApiModelProperty(value = "商品ID")
    @TableField("goods_id")
    private Long goodsId;


}
