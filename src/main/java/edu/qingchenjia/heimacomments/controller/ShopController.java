package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Shop;
import edu.qingchenjia.heimacomments.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public R<Shop> queryOne(@PathVariable("id") Long id) {
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
    @PutMapping
    public R<?> update(@RequestBody Shop shop) {
        return shopService.updateShop(shop);
    }

    /**
     * 保存店铺信息
     * <p>
     * 该方法通过POST请求接收一个Shop对象作为请求体，并将其保存到数据库中
     * 使用@PostMapping注解指定该方法处理POST请求，请求体中的数据被自动绑定到Shop对象中
     *
     * @param shop 店铺对象，包含要保存的店铺信息
     * @return 返回一个R对象，其中包含保存成功后的店铺ID
     */
    @PostMapping
    public R<Long> save(@RequestBody Shop shop) {
        return shopService.insertShop(shop);
    }

    /**
     * 根据店铺类型查询店铺信息
     *
     * @param typeId 店铺类型ID，用于筛选具有特定类型的店铺
     * @param page   当前页码，用于分页查询，默认值为1
     * @return 返回一个结果对象，包含根据类型筛选并分页查询到的店铺列表
     */
    @GetMapping("/of/type")
    public R<List<Shop>> queryByType(@RequestParam("typeId") Integer typeId, @RequestParam(value = "current", defaultValue = "1") Integer page, @RequestParam(value = "x", required = false) Double x, @RequestParam(value = "y", required = false) Double y) {
        return shopService.queryByType(typeId, page, x, y);
    }

    /**
     * 根据店铺名称查询店铺信息
     * 此方法通过GET请求处理，接收店铺名称和当前页码作为参数，返回一个包含店铺列表的响应对象
     * 主要用于演示如何通过Spring MVC的@GetMapping注解来处理HTTP GET请求，并利用参数注解来获取和处理请求参数
     *
     * @param name 店铺名称，作为查询条件，如果未提供则为null
     * @param page 当前页码，用于分页查询，如果未提供则默认为1
     * @return 返回一个包含店铺列表的响应对象，具体列表内容由提供的查询条件决定
     */
    @GetMapping("/of/name")
    public R<List<Shop>> queryByName(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "current", defaultValue = "1") Integer page) {
        return shopService.queryByName(name, page);
    }
}
