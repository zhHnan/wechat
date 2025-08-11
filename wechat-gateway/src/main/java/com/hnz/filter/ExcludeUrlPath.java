package com.hnz.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：excludeUrlPath
 * @Date：2025/8/11 21:02
 * @Filename：excludeUrlPath
 */

@Component
@Data
@PropertySource("classpath:excludeUrlPath.properties")
@ConfigurationProperties(prefix = "exclude")
public class ExcludeUrlPath {
    private List<String> urls;
}
