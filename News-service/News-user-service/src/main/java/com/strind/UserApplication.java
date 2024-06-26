package com.strind;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author strind
 * @version 1.0
 * @description app端用户微服务
 * @date 2024/3/21 11:39
 */
@SpringBootApplication
@EnableDubbo
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }

}
