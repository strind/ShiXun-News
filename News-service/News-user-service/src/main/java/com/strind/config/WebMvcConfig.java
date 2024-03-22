package com.strind.config;

import com.strind.interceptor.AppTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author strind
 * @version 1.0
 * @description webmvc 配置类
 * @date 2024/3/21 20:49
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加新的拦截器
        registry.addInterceptor(new AppTokenInterceptor()).addPathPatterns("/**");
    }
}
