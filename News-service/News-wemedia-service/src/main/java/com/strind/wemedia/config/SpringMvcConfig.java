package com.strind.wemedia.config;

import com.strind.wemedia.interceptor.WemediaTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/23 11:11
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WemediaTokenInterceptor()).addPathPatterns("/**");
    }

}
