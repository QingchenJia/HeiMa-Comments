package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.UserInfo;
import edu.qingchenjia.heimacomments.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public R<String> code(@RequestParam("phone") String phone) {
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
    public R<String> login(@RequestBody LoginFormDto loginFormDto) {
        return userService.login(loginFormDto);
    }

    /**
     * 处理用户注销请求
     * <p>
     * 该方法通过Post请求映射到/logout端点，接收HttpServletRequest对象作为参数，
     * 并调用userService的logout方法处理用户注销逻辑
     *
     * @param request HttpServletRequest对象，包含用户的请求信息
     * @return 返回一个R<String>对象，包含注销操作的结果信息
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    /**
     * 获取当前用户信息的接口
     * <p>
     * 该方法通过GET请求映射到"/me"，用于获取当前用户的信息
     * 它没有输入参数，返回一个UserDto对象，其中包含用户的相关信息
     *
     * @return R<UserDto> 包含用户信息的响应对象
     */
    @GetMapping("/me")
    public R<UserDto> me() {
        return userService.me();
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID，通过URL路径变量获取
     * @return 返回一个包裹了UserInfo对象的响应实体，包含用户详细信息
     */
    @GetMapping("/info/{id}")
    public R<UserInfo> info(@PathVariable("id") Long id) {
        return userService.info(id);
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param id 用户ID，通过URL路径变量获取
     * @return 返回一个用户对象，包含用户详细信息
     */
    @GetMapping("/{id}")
    public R<UserDto> queryOne(@PathVariable("id") Long id) {
        return userService.queryUserById(id);
    }

    /**
     * 处理用户签到请求
     * <p>
     * 该方法通过POST请求处理用户的签到操作它没有接收任何参数，
     * 并且返回一个响应对象，表示签到的结果
     *
     * @return 返回一个响应对象，包含签到操作的结果信息
     */
    @PostMapping("/sign")
    public R<?> sign() {
        return userService.sign();
    }
}
