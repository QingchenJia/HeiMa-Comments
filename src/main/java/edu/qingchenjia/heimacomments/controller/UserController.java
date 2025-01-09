package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码接口
     * 当用户请求发送验证码时，此方法会被调用
     *
     * @param phone 用户输入的手机号码，用于接收验证码
     * @return 返回一个R对象，通常包含发送验证码操作的结果状态
     */
    @PostMapping("/code")
    public R code(@RequestParam("phone") String phone) {
        return userService.sendCode(phone);
    }

    /**
     * 处理用户登录请求
     * 该方法通过POST请求处理用户的登录逻辑
     *
     * @param loginFormDto 用户登录信息
     * @return 返回登录结果，包括是否成功及可能的错误信息
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginFormDto loginFormDto) {
        return userService.login(loginFormDto);
    }
}
