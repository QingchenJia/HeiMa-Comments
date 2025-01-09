package edu.qingchenjia.heimacomments.config;

import edu.qingchenjia.heimacomments.common.LoginInterceptor;
import edu.qingchenjia.heimacomments.common.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 配置拦截器
     * <p>
     * 该方法用于向Spring MVC的InterceptorRegistry中添加拦截器，
     * 以便在请求处理之前或之后执行特定的逻辑
     *
     * @param registry InterceptorRegistry实例，用于注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册刷新令牌拦截器，对所有路径生效，并设置其顺序为0
        registry.addInterceptor(refreshTokenInterceptor)
                .addPathPatterns("/**")
                .order(0);

        // 注册登录拦截器，并排除特定路径以避免进行登录检查
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/shop/**",
                        "/voucher/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/blog/hot",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/favicon.ico"
                )
                .order(1);
    }
}
