package com.lzx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lzx.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("博客", "https://www.baidu.com", "money4965@qq.com");
        return new ApiInfoBuilder()
                .title("文档标题1")
                .description("文档描述2")
                // 联系方式
                .contact(contact)
                // 版本
                .version("1.1.1")
                .build();
    }
}