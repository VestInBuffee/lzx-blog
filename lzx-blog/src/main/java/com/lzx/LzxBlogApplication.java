package com.lzx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.lzx.mapper")
@EnableScheduling
@EnableSwagger2
public class LzxBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LzxBlogApplication.class, args);
    }
}
