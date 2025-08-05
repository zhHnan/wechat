package com.hnz.config;

import com.hnz.controller.interceptor.SmsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：InterceptorConfig
 * @Date：2025/7/4 18:32
 * @Filename：InterceptorConfig
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * @desc 在容器中注册短信拦截器  */
    @Bean
    public SmsInterceptor smsInterceptor() {
        return new SmsInterceptor();
    }

    /**
     * @description  注册拦截器，并且指定拦截的路由，否则不生效
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smsInterceptor()).addPathPatterns("/passport/getSmsCode");
    }
}
