package com.hnz.controller;

import com.hnz.base.BaseInfoProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/7/3 15:16
 * @Filename：HelloController
 */

@RestController
@RequestMapping("/g")
public class HelloController extends BaseInfoProperties {
    @GetMapping("/hello")
    public String hello(String key, String value) {
        redis.set(key, value);
        return redis.get(key);
    }
}
