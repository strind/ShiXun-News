package com.strind;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author strind
 * @version 1.0
 * @description 文章微服务
 * @date 2024/3/21 20:56
 */
@SpringBootApplication
@EnableDubbo
public class ArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class,args);
    }

}
