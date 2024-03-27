package com.strind.wemedia;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author strind
 * @version 1.0
 * @description 自媒体端微服务
 * @date 2024/3/22 16:10
 */
@SpringBootApplication
@EnableDubbo
public class WemediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WemediaApplication.class,args);
    }

}

