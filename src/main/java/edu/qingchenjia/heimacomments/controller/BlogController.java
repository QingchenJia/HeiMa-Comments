package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.Blog;
import edu.qingchenjia.heimacomments.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    /**
     * 获取热门博客列表
     *
     * @param page 当前页码，用于分页查询，默认值为1
     * @return 返回一个响应对象，包含热门博客列表
     */
    @GetMapping("/hot")
    public R<List<Blog>> hot(@RequestParam(value = "current", defaultValue = "1") Integer page) {
        return blogService.hotBlog(page);
    }

    /**
     * 保存博客文章
     * <p>
     * 该方法通过POST请求接收客户端发送的博客文章数据，并调用blogService的insertBlog方法将数据保存到数据库中
     * 主要用于用户发布新的博客文章或更新现有文章
     *
     * @param blog 博客对象，包含博客文章的相关信息，如标题、内容、作者等
     * @return 返回一个包含博客文章ID的响应对象，表示新保存或更新的博客文章的唯一标识
     */
    @PostMapping
    public R<Long> save(@RequestBody Blog blog) {
        return blogService.insertBlog(blog);
    }

    /**
     * 获取我的博客列表
     * <p>
     * 该方法通过GET请求处理"/of/me"路径，用于获取当前用户 的博客列表
     * 它接受一个名为"current"的请求参数，用于指定请求的页码，默认值为1
     *
     * @param page 请求的页码，用于分页获取博客列表
     * @return 返回一个封装了博客列表的响应对象
     */
    @GetMapping("/of/me")
    public R<List<Blog>> mine(@RequestParam(value = "current", defaultValue = "1") Integer page) {
        return blogService.mine(page);
    }

    /**
     * 处理点赞请求的控制器方法
     * 当用户在前端点击点赞按钮时，通过PUT请求调用此方法来增加对应博客的点赞数
     *
     * @param id 博客的唯一标识符，通过URL路径参数传递，用于指定需要点赞的博客
     * @return 返回一个响应对象，包含点赞操作的结果信息
     */
    @PutMapping("/like/{id}")
    public R<?> like(@PathVariable("id") Long id) {
        return blogService.like(id);
    }

    /**
     * 根据ID查询博客详情
     *
     * @param id 博客的唯一标识符
     * @return 返回一个响应对象，包含查询到的博客信息
     */
    @GetMapping("/{id}")
    public R<Blog> queryOne(@PathVariable("id") Long id) {
        return blogService.queryBlogById(id);
    }

    /**
     * 获取喜欢列表接口
     * 该接口用于获取特定用户喜欢的博客列表
     *
     * @param id 用户ID，用于指定获取哪个用户的喜欢列表
     * @return 返回一个Result对象，其中包含用户喜欢的博客列表
     */
    @GetMapping("/likes/{id}")
    public R<List<UserDto>> likeList(@PathVariable("id") Long id) {
        return blogService.likeList(id);
    }
}
