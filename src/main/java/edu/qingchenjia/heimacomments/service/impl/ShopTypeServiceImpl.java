package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.ShopType;
import edu.qingchenjia.heimacomments.mapper.ShopTypeMapper;
import edu.qingchenjia.heimacomments.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R selectList() {
        // 构造Redis缓存的键
        String key = Constant.REDIS_CACHE_SHOPTYPE_KEY + "all";

        // 从Redis中获取缓存的店铺类型信息
        String jsonShopTypes = stringRedisTemplate.opsForValue().get(key);
        // 如果缓存存在且不为空，则直接返回缓存的店铺类型信息
        if (StrUtil.isNotBlank(jsonShopTypes)) {
            List<ShopType> cacheShopTypes = JSONUtil.toBean(jsonShopTypes, List.class);
            return R.ok(cacheShopTypes);
        }

        // 如果缓存不存在，则从数据库中查询店铺类型信息
        LambdaQueryWrapper<ShopType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ShopType::getSort);

        List<ShopType> shopTypes = list(queryWrapper);
        // 如果查询结果为空，则返回错误信息
        if (CollUtil.isEmpty(shopTypes)) {
            return R.fail("店铺类型不存在");
        }

        // 将查询到的店铺类型信息转换为JSON字符串
        JSONUtil.toJsonStr(shopTypes);
        // 将查询到的店铺类型信息存入Redis缓存，并设置过期时间
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopTypes), Constant.REDIS_CACHE_SHOPTYPE_TTL, TimeUnit.MINUTES);
        // 返回查询到的店铺类型信息
        return R.ok(shopTypes);
    }
}
