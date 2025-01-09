package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Autowired
    private ShopTypeService shopTypeService;

    /**
     * 获取店铺类型列表
     * <p>
     * 通过GET请求访问/list路径，返回店铺类型的列表信息
     * 此方法无需参数，返回值为 shopTypeService.selectList() 的执行结果
     */
    @GetMapping("/list")
    public R list() {
        return shopTypeService.selectList();
    }
}
