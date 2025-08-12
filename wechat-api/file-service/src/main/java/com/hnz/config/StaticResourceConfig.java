package com.hnz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：StaticResourceConfig
 * @Date：2025/8/12 19:21
 * @Filename：StaticResourceConfig
 */

@Configuration
public class StaticResourceConfig extends WebMvcConfigurationSupport {
    /**
     * 添加静态资源映射路径，图片、视频等都放在classpath:static下
     * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:/temp/");
        super.addResourceHandlers(registry);
    }

}
