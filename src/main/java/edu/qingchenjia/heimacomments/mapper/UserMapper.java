package edu.qingchenjia.heimacomments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qingchenjia.heimacomments.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
