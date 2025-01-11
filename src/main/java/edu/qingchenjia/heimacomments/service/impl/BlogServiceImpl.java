package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.BaseContext;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.Blog;
import edu.qingchenjia.heimacomments.entity.User;
import edu.qingchenjia.heimacomments.mapper.BlogMapper;
import edu.qingchenjia.heimacomments.service.BlogService;
import edu.qingchenjia.heimacomments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private UserService userService;

    /**
     * 根据页面编号获取热门博客列表
     *
     * @param page 页面编号，用于指定从哪一页开始获取博客
     * @return 返回一个自定义响应对象，包含热门博客列表
     */
    @Override
    public R<List<Blog>> hotBlog(Integer page) {
        // 创建一个博客分页对象，指定当前页和最大页大小
        Page<Blog> blogPage = new Page<>(page, Constant.MAX_PAGE_SIZE);
        // 创建一个Lambda查询包装器，用于后续的查询排序
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询排序条件，根据博客的点赞数降序排序
        queryWrapper.orderByDesc(Blog::getLiked);

        // 执行分页查询，获取热门博客数据
        page(blogPage, queryWrapper);

        // 获取查询结果中的博客记录
        List<Blog> dbBlogs = blogPage.getRecords();

        // 遍历博客列表，为每个博客添加用户信息
        dbBlogs.forEach(blog -> {
            // 根据博客的用户ID获取对应的用户信息
            User blogUser = userService.getById(blog.getUserId());
            // 为博客设置用户的头像和昵称
            blog.setIcon(blogUser.getIcon());
            blog.setName(blogUser.getNickName());
        });

        // 返回包含热门博客列表的自定义响应对象
        return R.ok(dbBlogs, (long) dbBlogs.size());
    }

    /**
     * 插入博客文章
     * <p>
     * 该方法负责将博客文章实例保存到数据库中，并返回保存成功后的博客文章ID
     * 使用了@Override注解来表明这是在重写父类或接口中的方法
     *
     * @param blog 博客文章实例，包含文章的各种信息，如标题、内容、作者等
     * @return 返回一个封装了博客文章ID的响应对象，表示插入操作的结果
     */
    @Override
    public R<Long> insertBlog(Blog blog) {
        // 保存博客文章到数据库
        save(blog);
        // 返回保存成功后的博客文章ID，使用R.ok()方法表示操作成功
        return R.ok(blog.getId());
    }

    /**
     * 获取当前用户的所有博客
     *
     * @param page 页码，用于指定从哪一页开始获取博客
     * @return 返回一个响应对象，包含博客列表和博客数量
     */
    @Override
    public R<List<Blog>> mine(Integer page) {
        // 获取当前用户信息
        UserDto userDto = BaseContext.getCurrentUser();

        // 初始化博客分页对象，设置当前页和最大页大小
        Page<Blog> blogPage = new Page<>(page, Constant.MAX_PAGE_SIZE);

        // 创建查询条件，用于筛选当前用户的博客
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getUserId, userDto.getId());

        // 执行分页查询
        page(blogPage, queryWrapper);

        // 获取查询结果中的博客记录
        List<Blog> dbBlogs = blogPage.getRecords();

        // 返回包含博客列表和博客数量的响应对象
        return R.ok(dbBlogs, (long) dbBlogs.size());
    }

    /**
     * 点赞功能
     * <p>
     * 该方法用于处理用户对特定博客的点赞操作它首先从数据库中获取博客对象，
     * 然后增加其点赞数，并更新数据库中的对象
     *
     * @param id 博客的唯一标识符，用于定位要点赞的博客
     * @return 返回一个表示操作结果的响应对象，用于告知客户端操作是否成功
     */
    @Override
    public R<?> like(Long id) {
        // 根据ID获取博客对象
        Blog dbBlog = getById(id);
        // 增加博客的点赞数
        dbBlog.setLiked(dbBlog.getLiked() + 1);
        // 更新数据库中的博客对象
        updateById(dbBlog);

        // 返回操作成功的响应
        return R.ok();
    }
}
