package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Shop;
import edu.qingchenjia.heimacomments.mapper.ShopMapper;
import edu.qingchenjia.heimacomments.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据ID查询店铺信息
     * 首先尝试从Redis缓存中获取店铺信息，如果未找到，则从数据库中查询
     * 如果在数据库中找到了店铺信息，将其添加到Redis缓存中，以备下次查询
     *
     * @param id 店铺ID，用于查询店铺信息
     * @return 返回查询结果，包括店铺信息如果查询成功，否则返回失败信息
     */
    @Override
    public R queryById(Long id) {
        // 构造Redis缓存的键
        String key = Constant.REDIS_CACHE_SHOP_KEY + id;

        // 从Redis缓存中获取店铺信息的JSON字符串
        String jsonShop = stringRedisTemplate.opsForValue().get(key);

        // 如果缓存中存在该店铺信息，将其转换为Shop对象并返回
        if (StrUtil.isNotBlank(jsonShop)) {
            Shop cacheShop = JSONUtil.toBean(jsonShop, Shop.class);
            return R.ok(cacheShop);
        }

        // 如果缓存中不存在，从数据库中查询店铺信息
        Shop dbShop = getById(id);
        // 如果数据库中也找不到该店铺，返回失败信息
        if (ObjectUtil.isEmpty(dbShop)) {
            return R.fail("店铺不存在");
        }

        // 将从数据库中查询到的店铺信息添加到Redis缓存中，并设置缓存过期时间
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(dbShop), Constant.REDIS_CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 返回查询到的店铺信息
        return R.ok(dbShop);
    }
}
