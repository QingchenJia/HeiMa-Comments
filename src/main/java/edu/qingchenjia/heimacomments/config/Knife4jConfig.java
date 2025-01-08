package edu.qingchenjia.heimacomments.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    /**
     * 配置自定义的OpenAPI信息
     * <p>
     * 此方法用于生成和配置应用程序的API文档信息，包括标题、版本、描述和联系人信息
     * 它使用Spring框架的@Bean注解来定义一个Bean，这样Spring就可以自动管理这个OpenAPI实例
     *
     * @return OpenAPI 实例，包含应用的API文档信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("HeiMaComments")
                .version("1.0")
                .description("HeiMaComments项目-API接口文档")
                .contact(new Contact()
                        .name("Qingchen Jia")
                        .url("https://github.com/QingchenJia"))
        );
    }
}