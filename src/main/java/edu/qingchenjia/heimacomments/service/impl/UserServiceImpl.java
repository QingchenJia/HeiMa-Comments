package edu.qingchenjia.heimacomments.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.BaseContext;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.User;
import edu.qingchenjia.heimacomments.entity.UserInfo;
import edu.qingchenjia.heimacomments.mapper.UserMapper;
import edu.qingchenjia.heimacomments.service.UserInfoService;
import edu.qingchenjia.heimacomments.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserInfoService userInfoService;

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
    public R<String> sendCode(String phone) {
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
    public R<String> login(LoginFormDto loginFormDto) {
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
                        .setFieldValueEditor((String fieldName, Object fieldValue) -> Convert.toStr(fieldValue))
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

    /**
     * 退出登录功能
     * 通过删除Redis中的登录状态来实现用户退出登录
     *
     * @param request HTTP请求对象，用于获取请求头中的Token信息
     * @return 返回一个表示操作结果的R对象，此处专注于退出登录操作的成功提示
     */
    @Override
    public R<String> logout(HttpServletRequest request) {
        // 从请求头中获取Token
        String token = request.getHeader("authorization");
        // 拼接Redis中登录状态的Key
        String key = Constant.REDIS_LOGIN_TOKEN_KEY + token;

        // 删除Redis中的登录状态，以实现用户退出登录
        stringRedisTemplate.delete(key);

        // 返回退出登录成功的信息
        return R.ok("退出登录成功");
    }

    /**
     * 获取当前用户信息
     * <p>
     * 该方法用于获取当前上下文中认证通过的用户信息，并以UserDto对象的形式返回
     * 主要用于需要当前用户详细信息的场景，以便于在系统中进行用户相关的操作或展示
     *
     * @return R<UserDto> 返回一个包装了UserDto对象的响应，表示当前用户的信息
     */
    @Override
    public R<UserDto> me() {
        // 从上下文中获取当前用户信息，并直接返回封装了用户信息的响应对象
        UserDto userDto = BaseContext.getCurrentUser();
        return R.ok(userDto);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID，用于查询用户信息
     * @return 返回一个Result对象，包含查询到的用户信息
     */
    @Override
    public R<UserInfo> info(Long id) {
        // 通过用户ID从数据库中获取用户信息
        UserInfo dbUserInfo = userInfoService.getById(id);
        // 返回查询结果，封装在Result对象中
        return R.ok(dbUserInfo);
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param id 用户ID，用于唯一标识数据库中的用户
     * @return 返回一个封装了用户信息的R对象如果用户不存在，R对象将表示一个空的结果
     */
    @Override
    public R<UserDto> queryUserById(Long id) {
        // 从数据库中获取用户信息
        User dbUser = getById(id);
        // 将用户信息转换为DTO（数据传输对象）以供返回
        UserDto userDto = BeanUtil.copyProperties(dbUser, UserDto.class);

        // 返回一个表示操作成功并包含用户DTO的响应对象
        return R.ok(userDto);
    }

    /**
     * 签名方法
     * <p>
     * 该方法用于处理用户签名操作它根据当前用户和当前日期，
     * 在Redis中存储签名信息，以实现每日一签的功能
     *
     * @return 返回签名结果，通常是一个表示成功或失败的响应对象
     */
    @Override
    public R<?> sign() {
        // 获取当前用户信息
        UserDto userDto = BaseContext.getCurrentUser();
        // 生成Redis键的后缀部分，格式为yyyyMM
        String keySuffix = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMM"));
        // 构造Redis键，包含用户ID和日期后缀
        String key = Constant.REDIS_SIGN_USER_KEY + userDto.getId() + ":" + keySuffix;

        // 在Redis中设置签名信息，使用当前日期的天作为位索引，表示该天已签名
        stringRedisTemplate.opsForValue()
                .setBit(key, LocalDateTime.now().getDayOfMonth() - 1, true);

        // 返回成功响应
        return R.ok();
    }
}