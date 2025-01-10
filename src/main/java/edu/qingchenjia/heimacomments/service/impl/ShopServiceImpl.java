package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
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
     * 首先尝试从Redis缓存中获取店铺信息，如果缓存中不存在，则从数据库中查询
     * 并将查询结果缓存到Redis中，以提高下次查询的效率
     *
     * @param id 店铺ID，用于查询店铺信息
     * @return 返回查询结果，包括店铺信息
     */
    @Override
    public R queryById(Long id) {
        // 构造Redis缓存的键
        String key = Constant.REDIS_CACHE_SHOP_KEY + id;

        // 检查Redis缓存中是否存在该店铺信息
        if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))) {
            // 如果缓存存在，从Redis中获取店铺信息并返回
            String jsonShop = stringRedisTemplate.opsForValue().get(key);
            Shop cacheShop = JSONUtil.toBean(jsonShop, Shop.class);
            return R.ok(cacheShop);
        }

        // 如果缓存中不存在，从数据库中查询店铺信息
        Shop dbShop = getById(id);
        // 将从数据库中查询到的店铺信息添加到Redis缓存中，并设置缓存过期时间
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(dbShop), Constant.REDIS_CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 返回查询到的店铺信息
        return R.ok(dbShop);
    }

    /**
     * 更新店铺信息
     * <p>
     * 此方法主要用于更新数据库中的店铺信息，并在更新成功后删除Redis缓存中的相应店铺信息，
     * 以确保缓存数据与数据库数据保持一致
     *
     * @param shop 待更新的店铺对象，包含要更新的店铺信息
     * @return 返回更新结果，包括成功或失败的提示信息
     */
    @Override
    public R updateShop(Shop shop) {
        // 检查店铺ID是否为空，如果为空则返回失败响应，提示店铺ID不能为空
        if (ObjectUtil.isEmpty(shop.getId())) {
            return R.fail("店铺ID不能为空");
        }

        // 更新数据库中的店铺信息
        updateById(shop);

        // 构造Redis缓存中的店铺键名
        String key = Constant.REDIS_CACHE_SHOP_KEY + shop.getId();
        // 删除Redis缓存中的店铺信息，以保证缓存数据与数据库数据一致
        stringRedisTemplate.delete(key);

        // 返回成功响应
        return R.ok();
    }
}
