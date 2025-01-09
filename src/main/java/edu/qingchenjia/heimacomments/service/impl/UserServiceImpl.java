package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.User;
import edu.qingchenjia.heimacomments.mapper.UserMapper;
import edu.qingchenjia.heimacomments.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送验证码到指定手机
     *
     * @param phone 手机号，需要发送验证码的用户手机号
     * @return 返回发送结果，包括验证码
     * <p>
     * 本方法首先验证手机号格式，然后生成6位数字的验证码，
     * 并将验证码与手机号关联存储在Redis中，用于后续验证
     * 同时，验证码会被记录在日志中，以便调试和审计
     */
    @Override
    public R sendCode(String phone) {
        // 验证手机号格式，如果格式不正确，返回错误提示
        if (!Validator.isMobile(phone)) {
            return R.fail("手机号格式不正确");
        }

        // 生成6位数字的验证码
        String code = RandomUtil.randomNumbers(6);
        // 将验证码与手机号关联存储在Redis中，设置过期时间
        stringRedisTemplate.opsForValue().set(Constant.REDIS_LOGIN_CODE_KEY + phone, code, Constant.REDIS_LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 记录日志信息，输出验证码
        log.info("验证码: {}", code);

        // 返回成功响应，包含验证码
        return R.ok(code);
    }

    /**
     * 用户登录方法，通过手机号和验证码进行验证。
     *
     * @param loginFormDto 包含用户登录信息的表单数据传输对象，包括手机号和验证码。
     * @return R 对象，包含登录结果信息。如果登录成功，返回包含token的成功响应；如果登录失败，返回相应的错误信息。
     */
    @Override
    public R login(LoginFormDto loginFormDto) {
        // 获取用户输入的手机号和验证码
        String phone = loginFormDto.getPhone();
        String code = loginFormDto.getCode();

        // 验证手机号格式
        if (!Validator.isMobile(phone)) {
            return R.fail("手机号格式不正确");
        }

        // 从Redis中获取正确的验证码
        String rightCode = stringRedisTemplate.opsForValue().get(Constant.REDIS_LOGIN_CODE_KEY + phone);

        // 检查用户输入的验证码是否为空
        if (StrUtil.isBlank(code)) {
            return R.fail("验证码已失效");
        }

        // 验证用户输入的验证码是否正确
        if (!StrUtil.equals(code, rightCode)) {
            return R.fail("验证码错误");
        }

        // 验证码验证成功后，删除Redis中的验证码
        stringRedisTemplate.delete(Constant.REDIS_LOGIN_CODE_KEY + phone);

        // 查询数据库中是否存在该手机号的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User dbUser = getOne(queryWrapper);

        // 如果用户不存在，则创建新用户
        dbUser = dbUser == null ? insertUser(phone) : dbUser;

        // 将用户信息封装到UserDto对象中
        UserDto userDto = BeanUtil.copyProperties(dbUser, UserDto.class);

        // 将UserDto对象转换为Map对象
        Map<String, Object> userMap = BeanUtil.beanToMap(
                userDto,
                new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(false)
                        .setFieldValueEditor((String s, Object object) -> object.toString())
        );

        // 生成唯一的token
        String token = UUID.randomUUID().toString(true);
        // 将用户信息保存到Redis中
        stringRedisTemplate.opsForHash().putAll(Constant.REDIS_LOGIN_TOKEN_KEY + token, userMap);
        // 设置token的过期时间
        stringRedisTemplate.expire(Constant.REDIS_LOGIN_TOKEN_KEY + token, Constant.REDIS_LOGIN_TOKEN_TTL, TimeUnit.MINUTES);

        // 返回成功响应，包含token
        return R.ok(token);
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
        user.setNickName(Constant.USER_DEFAULT_NICKNAME + RandomUtil.randomNumbers(6));
        // 保存用户到数据库
        save(user);

        // 返回新创建的用户
        return user;
    }
}