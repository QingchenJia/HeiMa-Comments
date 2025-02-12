package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Shop;

import java.util.List;

public interface ShopService extends IService<Shop> {
    R<Shop> queryById(Long id);

    R<?> updateShop(Shop shop);

    R<Long> insertShop(Shop shop);

    R<List<Shop>> queryByType(Integer typeId, Integer page,Double x, Double y);

    R<List<Shop>> queryByName(String name, Integer page);
}
