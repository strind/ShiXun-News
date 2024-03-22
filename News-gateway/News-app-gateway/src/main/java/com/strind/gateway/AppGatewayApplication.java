package com.strind.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author strind
 * @version 1.0
 * @description app 网关
 * @date 2024/3/22 9:51
 */
@SpringBootApplication
@EnableDiscoveryClient // nacos服务发现
public class AppGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppGatewayApplication.class,args);
    }
}
