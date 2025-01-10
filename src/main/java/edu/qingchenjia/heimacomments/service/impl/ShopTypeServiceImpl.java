package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
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
    public R<List<ShopType>> selectList() {
        // 构造Redis缓存的键
        String key = Constant.REDIS_CACHE_SHOPTYPE_KEY + "all";

        // 检查Redis缓存中是否存在该键
        if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))) {
            // 如果缓存存在，则直接从缓存中获取店铺类型信息
            String jsonShopTypes = stringRedisTemplate.opsForValue().get(key);

            if (StrUtil.isBlank(jsonShopTypes)) {
                return R.ok(null);
            }

            // 将JSON字符串转换为List<ShopType>对象
            List<ShopType> cacheShopTypes = JSONUtil.toList(jsonShopTypes, ShopType.class);
            // 返回查询到的店铺类型信息
            return R.ok(cacheShopTypes);
        }

        // 如果缓存不存在，则从数据库中查询店铺类型信息
        LambdaQueryWrapper<ShopType> queryWrapper = new LambdaQueryWrapper<>();
        // 按照排序字段升序排列
        queryWrapper.orderByAsc(ShopType::getSort);

        // 执行查询
        List<ShopType> dbShopTypes = list(queryWrapper);

        if (ObjectUtil.isEmpty(dbShopTypes)) {
            stringRedisTemplate.opsForValue().set(key, Constant.REDIS_NO_DATA, Constant.REDIS_NO_DATA_TTL, TimeUnit.MINUTES);
            return R.ok(null);
        }

        // 将查询到的店铺类型信息转换为JSON字符串
        JSONUtil.toJsonStr(dbShopTypes);
        // 将查询到的店铺类型信息存入Redis缓存，并设置过期时间
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(dbShopTypes), Constant.REDIS_CACHE_SHOPTYPE_TTL, TimeUnit.MINUTES);
        // 返回查询到的店铺类型信息
        return R.ok(dbShopTypes);
    }
}
