package edu.qingchenjia.heimacomments.common;

import edu.qingchenjia.heimacomments.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行预处理
     *
     * @param request  HttpServletRequest对象，用于获取请求信息
     * @param response HttpServletResponse对象，用于设置响应信息
     * @param handler  处理请求的处理器对象
     * @return boolean    返回true继续执行下一个拦截器或处理器，返回false中断执行
     * @throws Exception 如果预处理过程中发生异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从session中获取用户信息
        Object userDto = request.getSession().getAttribute("user");

        // 如果用户信息为空，表示用户未登录或session已过期
        if (Objects.isNull(userDto)) {
            // 设置响应状态码为未授权
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 中断执行，用户需要重新登录
            return false;
        }

        // 将用户信息设置到上下文中，供后续操作使用
        BaseContext.setCurrentUser((UserDto) userDto);
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
