package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopType")
public class ShopTypeController {
    @Autowired
    private ShopTypeService shopTypeService;
}
