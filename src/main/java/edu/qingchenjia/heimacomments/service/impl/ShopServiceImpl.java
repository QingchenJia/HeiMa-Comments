package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Shop;
import edu.qingchenjia.heimacomments.mapper.ShopMapper;
import edu.qingchenjia.heimacomments.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public R<Shop> queryById(Long id) {
        // 构造Redis缓存的键
        String key = Constant.REDIS_CACHE_SHOP_KEY + id;

        // 检查Redis缓存中是否存在该店铺信息
        if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))) {
            // 如果缓存存在，从Redis中获取店铺信息并返回
            String jsonShop = stringRedisTemplate.opsForValue().get(key);

            if (StrUtil.isBlank(jsonShop)) {
                return R.ok(null);
            }

            Shop cacheShop = JSONUtil.toBean(jsonShop, Shop.class);
            return R.ok(cacheShop);
        }

        // 如果缓存中不存在，从数据库中查询店铺信息
        Shop dbShop = getById(id);

        if (ObjectUtil.isEmpty(dbShop)) {
            stringRedisTemplate.opsForValue().set(key, Constant.REDIS_NO_DATA, Constant.REDIS_NO_DATA_TTL, TimeUnit.MINUTES);
            return R.ok(null);
        }

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
    @Transactional
    public R<?> updateShop(Shop shop) {
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

    /**
     * 插入新店铺信息
     * <p>
     * 该方法通过保存传入的Shop对象到数据库中，实现店铺信息的插入操作
     * 使用OVERRIDE注解表明该方法重写了父类或接口的方法
     *
     * @param shop 要插入的新店铺实例，包含店铺的相关信息
     * @return 返回一个封装了执行结果的R对象，包含店铺插入后的ID
     * 使用R.ok()方法表示操作成功，并传入店铺ID作为返回值
     */
    @Override
    public R<Long> insertShop(Shop shop) {
        save(shop);
        return R.ok(shop.getId());
    }

    /**
     * 根据类型ID查询店铺信息
     * <p>
     * 此方法用于分页查询特定类型ID的店铺列表它首先创建一个分页对象和查询条件对象，
     * 然后根据给定的类型ID查询数据库，最后返回包含店铺列表和总记录数的响应对象
     *
     * @param typeId 店铺类型ID，用于筛选查询结果
     * @param page   页码，用于指定查询的页数
     * @return 返回一个自定义响应对象R，包含店铺列表和总记录数
     */
    @Override
    public R<List<Shop>> queryByType(Integer typeId, Integer page) {
        // 创建分页对象，参数为当前页码和每页最大记录数
        Page<Shop> shopPage = new Page<>(page, Constant.MAX_PAGE_SIZE);

        // 创建Lambda查询条件对象，用于构建查询条件
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件：类型ID等于传入的typeId
        queryWrapper.eq(Shop::getTypeId, typeId);

        // 执行分页查询
        page(shopPage, queryWrapper);

        // 获取查询结果列表
        List<Shop> dbShops = shopPage.getRecords();

        // 返回包含查询结果和总记录数的响应对象
        return R.ok(dbShops, (long) dbShops.size());
    }

    /**
     * 根据名称查询店铺
     *
     * @param name 店铺名称，用于查询的关键词
     * @param page 当前页码，用于分页查询
     * @return 返回一个包含店铺列表的结果对象，包括店铺信息和总数
     * <p>
     * 此方法用于根据提供的店铺名称关键词和页码，查询并返回相应的店铺列表
     * 它通过分页方式来限制查询结果的数量，以提高查询效率和用户体验
     */
    @Override
    public R<List<Shop>> queryByName(String name, Integer page) {
        // 初始化分页对象，用于存储分页查询的结果
        Page<Shop> shopPage = new Page<>(page, Constant.MAX_PAGE_SIZE);

        // 创建查询条件构造器，用于构建灵活的查询条件
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称查询店铺，如果名称不为空，则添加名称作为查询条件
        queryWrapper.like(StrUtil.isNotBlank(name), Shop::getName, name);

        // 执行分页查询，将查询结果存储在shopPage中
        page(shopPage, queryWrapper);

        // 获取查询结果中的店铺列表
        List<Shop> dbShops = shopPage.getRecords();

        // 返回查询结果，包括店铺列表和列表的总数量
        return R.ok(dbShops, (long) dbShops.size());
    }
}
