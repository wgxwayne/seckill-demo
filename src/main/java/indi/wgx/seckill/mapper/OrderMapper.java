package indi.wgx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.wgx.seckill.pojo.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Component("orderMapper")
public interface OrderMapper extends BaseMapper<Order> {

}
