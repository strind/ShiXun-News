package com.strind.config;

import com.strind.interceptor.AppTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author strind
 * @version 1.0
 * @description mvc 配置
 * @date 2024/3/21 21:02
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所以请求
        registry.addInterceptor(new AppTokenInterceptor()).addPathPatterns("/**");
    }
}
