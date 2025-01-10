package edu.qingchenjia.heimacomments.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import edu.qingchenjia.heimacomments.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 在请求处理之前进行拦截处理
     *
     * @param request  HttpServletRequest对象，用于获取请求信息
     * @param response HttpServletResponse对象，用于设置响应信息
     * @param handler  处理器对象，表示请求将要被发送到的目标方法
     * @return boolean  返回值为true表示继续执行下一个拦截器或处理器方法，为false表示中断执行
     * @throws Exception 如果处理过程中发生异常，则抛出此异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取授权令牌
        String token = request.getHeader("authorization");

        // 如果令牌为空，则直接放行
        if (StrUtil.isBlank(token)) {
            return true;
        }

        // 构造Redis中登录令牌的键
        String key = Constant.REDIS_LOGIN_TOKEN_KEY + token;

        // 检查Redis中是否存在该登录令牌键
        if (!BooleanUtil.isTrue(stringRedisTemplate.hasKey(key))) {
            // 如果键不存在，返回true
            return true;
        }

        // 从Redis中获取与令牌关联的用户信息
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);

        // 创建一个用户DTO对象，用于存储从Redis中获取的用户信息
        UserDto userDto = new UserDto();
        // 将从Redis中获取的用户信息填充到用户DTO对象中
        userDto = BeanUtil.fillBeanWithMap(userMap, userDto, false);

        // 将用户信息设置到上下文中，供后续操作使用
        BaseContext.setCurrentUser(userDto);
        // 延长令牌的过期时间为30分钟
        stringRedisTemplate.expire(Constant.REDIS_LOGIN_TOKEN_KEY + token, 30, TimeUnit.MINUTES);
        // 继续执行下一个拦截器或处理器
        return true;
    }

    /**
     * 在请求完成后执行的操作
     *
     * @param request  HTTP请求对象，用于获取请求相关的信息
     * @param response HTTP响应对象，用于获取响应相关的信息
     * @param handler  处理请求的处理器对象，可以是任何对象，取决于具体的请求处理逻辑
     * @param ex       请求处理过程中发生的异常，如果没有异常，则为null
     * @throws Exception 根据具体实现可能会抛出的异常类型
     *                   <p>
     *                   此方法主要用于在请求处理完成后清理线程本地存储中的当前用户信息
     *                   以防止内存泄漏和确保线程安全
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeCurrentUser();
    }
}
