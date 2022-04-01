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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
//@ApiModel(value="TOrder对象", description="")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

//    @ApiModelProperty(value = "商品ID")
    @TableField("goods_id")
    private Long goodsId;

//    @ApiModelProperty(value = "收获地址ID")
    @TableField("delivery_addr_id")
    private Long deliveryAddrId;

//    @ApiModelProperty(value = "商品名字")
    @TableField("goods_name")
    private String goodsName;

//    @ApiModelProperty(value = "商品数量")
    @TableField("goods_count")
    private Integer goodsCount;

//    @ApiModelProperty(value = "商品价格")
    @TableField("goods_price")
    private BigDecimal goodsPrice;

//    @ApiModelProperty(value = "1 pc,2 android, 3 ios")
    @TableField("order_channel")
    private Integer orderChannel;

//    @ApiModelProperty(value = "订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成")
    @TableField("status")
    private Integer status;

//    @ApiModelProperty(value = "订单创建时间")
    @TableField("create_date")
    private Date createDate;

//    @ApiModelProperty(value = "支付时间")
    @TableField("pay_date")
    private Date payDate;

}
