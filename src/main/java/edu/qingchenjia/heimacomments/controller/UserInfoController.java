package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
}
