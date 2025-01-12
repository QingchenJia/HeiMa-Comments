package edu.qingchenjia.heimacomments;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Blog;
import edu.qingchenjia.heimacomments.service.BlogService;
import edu.qingchenjia.heimacomments.service.ShopTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.util.List;

@SpringBootTest
class HeiMaCommentsApplicationTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
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

    @Test
    void testStrUtilIsBlank() {
        boolean isBlank = StrUtil.isBlank("");
        System.out.println(isBlank);
    }

    @Test
    void testFileMove() {
        FileUtil.move(new File("D:\\Code-Storage\\HeiMaComments\\images\\1\\1"),
                new File("D:\\Code-Storage\\HeiMaComments\\images\\2\\2"),
                true);
    }

    @Test
    void testRedisZSetCacheData() {
        Double score = stringRedisTemplate.opsForZSet().score("key", Convert.toStr(1878334743381135360L));
        System.out.println(score);
    }
}
