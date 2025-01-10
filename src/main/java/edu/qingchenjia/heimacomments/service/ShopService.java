package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Shop;

public interface ShopService extends IService<Shop> {
    R<Shop> queryById(Long id);

    <T> R<T> updateShop(Shop shop);
}
