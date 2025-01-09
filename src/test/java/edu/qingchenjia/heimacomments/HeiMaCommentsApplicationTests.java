package edu.qingchenjia.heimacomments;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Blog;
import edu.qingchenjia.heimacomments.service.BlogService;
import edu.qingchenjia.heimacomments.service.ShopTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HeiMaCommentsApplicationTests {
    @Autowired
    private BlogService blogService;
    @Autowired
    private ShopTypeService shopTypeService;

    @Test
    void contextLoads() {
    }

    @Test
    void testDbSelect() {
        List<Blog> blogs = blogService.list();
        System.out.println(blogs);
    }

    @Test
    void testSelectShopTypeList() {
        R r = shopTypeService.selectList();
        System.out.println(r.getData());
    }
}
