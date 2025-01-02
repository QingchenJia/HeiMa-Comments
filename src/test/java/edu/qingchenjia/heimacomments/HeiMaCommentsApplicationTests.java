package edu.qingchenjia.heimacomments;

import edu.qingchenjia.heimacomments.entity.Blog;
import edu.qingchenjia.heimacomments.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HeiMaCommentsApplicationTests {
    @Autowired
    private BlogService blogService;

    @Test
    void contextLoads() {
    }

    @Test
    void testDbSelect() {
        List<Blog> blogs = blogService.list();
        System.out.println(blogs);
    }
}
