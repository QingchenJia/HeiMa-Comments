package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
