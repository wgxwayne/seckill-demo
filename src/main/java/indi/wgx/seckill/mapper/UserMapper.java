package indi.wgx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.wgx.seckill.pojo.User;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-18
 */
@Component("userMapper")
public interface UserMapper extends BaseMapper<User>{

}
