package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
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

        // 遍历博客列表，为博客对象添加额外的用户信息和点赞状态
        dbBlogs.forEach(this::expendBlogInfo);

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
        // 获取当前用户信息
        UserDto userDto = BaseContext.getCurrentUser();
        // 设置博客文章的用户ID
        blog.setUserId(userDto.getId());

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
        // 获取当前操作用户信息
        UserDto userDto = BaseContext.getCurrentUser();

        // 构造Redis中存储点赞信息的键
        String key = Constant.REDIS_LIKE_BLOG_KEY + id;

        // 初始化更新条件包装器
        LambdaUpdateWrapper<Blog> updateWrapper = new LambdaUpdateWrapper<>();

        // 检查用户是否已经点赞过该博客
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, Convert.toStr(userDto.getId()));
        if (BooleanUtil.isTrue(isLiked)) {
            // 如果已点赞，减少点赞数
            updateWrapper.setSql("liked = liked - 1")
                    .eq(Blog::getId, id);

            // 从Redis集合中移除用户ID，表示取消点赞
            stringRedisTemplate.opsForSet().remove(key, userDto.getId());
        } else {
            // 如果未点赞，增加点赞数
            updateWrapper.setSql("liked = liked + 1")
                    .eq(Blog::getId, id);

            // 向Redis集合中添加用户ID，表示点赞
            stringRedisTemplate.opsForSet().add(key, Convert.toStr(userDto.getId()));
        }

        // 返回操作成功的响应
        return R.ok();
    }

    /**
     * 根据博客ID查询博客详情
     * 此方法首先从数据库中获取博客对象，然后获取该博客关联的用户信息，
     * 并将用户的头像和昵称设置到博客对象中，最后返回封装了博客对象的响应结果
     *
     * @param id 博客的唯一标识符
     * @return 返回一个封装了博客对象的响应结果
     */
    @Override
    public R<Blog> queryBlogById(Long id) {
        // 根据博客ID从数据库中获取博客对象
        Blog dbBlog = getById(id);
        // 为博客对象添加额外的用户信息和点赞状态
        expendBlogInfo(dbBlog);

        // 返回封装了博客对象的响应结果
        return R.ok(dbBlog);
    }

    /**
     * 扩展博客信息方法，主要用于为博客对象添加额外的用户信息和点赞状态
     * 此方法解释了如何从用户服务获取博客作者的详细信息，以及如何通过Redis判断当前用户是否点赞了该博客
     *
     * @param blog 博客对象，此方法将为此对象添加用户的头像、昵称和点赞状态
     */
    private void expendBlogInfo(Blog blog) {
        // 根据博客的用户ID获取对应的用户信息
        User blogUser = userService.getById(blog.getUserId());
        // 为博客设置用户的头像和昵称
        blog.setIcon(blogUser.getIcon());
        blog.setName(blogUser.getNickName());

        // 获取当前操作的用户信息
        UserDto userDto = BaseContext.getCurrentUser();

        // 构造Redis中保存博客点赞信息的键
        String key = Constant.REDIS_LIKE_BLOG_KEY + userDto.getId();
        // 检查当前博客是否被指定用户点赞过
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, userDto.getId());

        // 设置博客的点赞状态
        blog.setIsLike(isLiked);
    }
}
