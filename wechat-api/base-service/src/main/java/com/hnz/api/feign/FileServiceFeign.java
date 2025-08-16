package com.hnz.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：UserInfoServiceFeign
 * @Date：2025/8/14 21:40
 * @Filename：UserInfoServiceFeign
 */

@FeignClient(value = "file-service")
public interface FileServiceFeign {
    @PostMapping("/file/generatorQrCode")
    String generatorQrCode(@RequestParam("wechatNumber") String wechatNumber, @RequestParam("userId") String userId);

}
