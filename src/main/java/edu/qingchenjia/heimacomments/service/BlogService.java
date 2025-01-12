package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.Blog;

import java.util.List;

public interface BlogService extends IService<Blog> {
    R<List<Blog>> hotBlog(Integer page);

    R<Long> insertBlog(Blog blog);

    R<List<Blog>> mine(Integer page);

    R<?> like(Long id);

    R<Blog> queryBlogById(Long id);

    R<List<UserDto>> likeList(Long id);
}
