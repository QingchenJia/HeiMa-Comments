package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Shop;
import edu.qingchenjia.heimacomments.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 根据ID查询店铺信息
     *
     * @param id 店铺ID，通过URL路径传入
     * @return 返回查询结果，包括店铺详细信息
     */
    @GetMapping("/{id}")
    public R queryOne(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    /**
     * 更新店铺信息的接口
     * <p>
     * 该方法通过POST请求接收一个Shop对象，其中包含要更新的店铺信息
     * 它调用ShopService中的updateShop方法来执行更新操作
     *
     * @param shop 包含更新信息的Shop对象，由请求体中获取
     * @return 返回更新结果，通常是一个表示操作结果的响应对象
     */
    @PostMapping("/update")
    public R update(@RequestBody Shop shop) {
        return shopService.updateShop(shop);
    }
}
