package indi.wgx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 秒杀商品表
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_seckill_goods")
//@ApiModel(value="TSeckillGoods对象", description="秒杀商品表")
public class SeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "秒杀商品ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "商品ID")
    @TableField("goods_id")
    private Long goodsId;

//    @ApiModelProperty(value = "秒杀家")
    @TableField("seckill_price")
    private BigDecimal seckillPrice;

//    @ApiModelProperty(value = "库存数量")
    @TableField("stock_count")
    private Integer stockCount;

//    @ApiModelProperty(value = "秒杀开始时间")
    @TableField("start_date")
    private Date startDate;

//    @ApiModelProperty(value = "秒杀结束时间")
    @TableField("end_date")
    private Date endDate;


}
