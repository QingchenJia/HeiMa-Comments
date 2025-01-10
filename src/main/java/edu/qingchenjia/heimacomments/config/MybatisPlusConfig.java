package edu.qingchenjia.heimacomments.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    /**
     * 配置Mybatis-Plus拦截器
     * <p>
     * 该方法通过@Bean注解声明了一个Bean，类型为MybatisPlusInterceptor
     * 其主要作用是向Mybatis-Plus中添加内置的分页拦截器，以便在执行查询时自动进行分页处理
     *
     * @return MybatisPlusInterceptor 返回配置好的Mybatis-Plus拦截器实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建MybatisPlusInterceptor实例
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 向拦截器中添加分页内置拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 返回配置好的拦截器实例
        return mybatisPlusInterceptor;
    }
}
