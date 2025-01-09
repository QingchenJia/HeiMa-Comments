package edu.qingchenjia.heimacomments.common;

import cn.hutool.core.util.ObjectUtil;
import edu.qingchenjia.heimacomments.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行拦截处理
     *
     * @param request  请求对象，包含请求相关的信息
     * @param response 响应对象，用于向客户端返回结果
     * @param handler  处理请求的处理器对象
     * @return boolean 返回值决定是否继续执行其他拦截器和处理器方法
     * 如果返回false，则不会继续执行其他拦截器和处理器方法
     * 如果返回true，则继续执行
     * <p>
     * 此方法主要用于权限验证，在请求处理前检查用户是否已经登录
     * 如果用户未登录，则返回未授权状态码，阻止后续操作
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前用户信息
        UserDto userDto = BaseContext.getCurrentUser();
        // 检查用户信息是否为空
        if (ObjectUtil.isEmpty(userDto)) {
            // 如果用户未登录，则设置响应状态码为未授权，并阻止后续操作
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 如果用户已登录，则继续执行后续操作
        return true;
    }
}
