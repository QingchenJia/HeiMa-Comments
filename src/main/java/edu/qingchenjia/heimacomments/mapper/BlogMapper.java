package edu.qingchenjia.heimacomments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qingchenjia.heimacomments.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
