package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.Blog;
import edu.qingchenjia.heimacomments.mapper.BlogMapper;
import edu.qingchenjia.heimacomments.service.BlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
}
