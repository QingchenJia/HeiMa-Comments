package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.entity.User;
import edu.qingchenjia.heimacomments.mapper.UserMapper;
import edu.qingchenjia.heimacomments.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 发送验证码接口实现方法
     * <p>
     * 该方法用于生成一个6位数字的验证码，并将其与给定的手机号关联，存储在会话中
     * 如果手机号格式不正确，则返回失败响应
     *
     * @param request HttpServletRequest对象，用于获取和设置会话属性
     * @param phone   用户手机号，用于生成验证码并进行格式验证
     * @return R      返回一个响应对象，指示验证码生成和存储操作的结果
     */
    @Override
    public R sendCode(HttpServletRequest request, String phone) {
        // 验证手机号格式，如果格式不正确，返回错误提示
        if (!Validator.isMobile(phone)) {
            return R.fail("手机号格式不正确");
        }

        // 生成6位数字的验证码
        String code = RandomUtil.randomNumbers(6);
        // 将手机号和验证码关联，并存储在会话中
        request.getSession().setAttribute(phone, code);
        // 记录日志信息，输出验证码
        log.info("验证码: {}", code);

        // 返回成功响应，包含验证码
        return R.ok(code);
    }

    /**
     * 登录方法
     *
     * @param request      HTTP请求对象，用于获取会话中的验证码
     * @param loginFormDto 用户输入的登录信息
     * @return 返回登录结果，包括是否成功
     */
    @Override
    public R login(HttpServletRequest request, LoginFormDto loginFormDto) {
        // 获取用户输入的手机号和验证码
        String phone = loginFormDto.getPhone();
        String code = loginFormDto.getCode();

        // 验证手机号格式
        if (!Validator.isMobile(phone)) {
            return R.fail("手机号格式不正确");
        }

        // 从会话中获取正确的验证码
        Object rightCode = request.getSession().getAttribute(phone);

        // 检查用户输入的验证码是否为空
        if (StrUtil.isEmpty(code)) {
            return R.fail("验证码已失效");
        }

        // 验证用户输入的验证码是否正确
        if (!rightCode.equals(code)) {
            return R.fail("验证码错误");
        }

        // 查询数据库中是否存在该手机号的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User dbUser = getOne(queryWrapper);

        // 如果用户不存在，则创建新用户
        dbUser = dbUser == null ? insertUser(phone) : dbUser;

        // 将用户信息存入会话
        request.getSession().setAttribute("user", dbUser.getId());

        // 返回登录成功
        return R.ok();
    }

    /**
     * 插入新用户
     * <p>
     * 创建一个新的用户实例，设置其电话号码和昵称，然后保存到数据库中
     * 电话号码是用户的主要标识之一，因此被作为参数传入
     * 昵称是随机生成的，以确保新用户在系统中有唯一的标识
     *
     * @param phone 用户的电话号码，用于注册新用户
     * @return 返回新创建并保存的用户对象
     */
    @Override
    public User insertUser(String phone) {
        // 创建一个新的用户实例
        User user = new User();
        // 设置用户电话号码
        user.setPhone(phone);
        // 随机生成一个6位数字的昵称，确保昵称的唯一性
        user.setNickName("用户" + RandomUtil.randomNumbers(6));
        // 保存用户到数据库
        save(user);

        // 返回新创建的用户
        return user;
    }
}