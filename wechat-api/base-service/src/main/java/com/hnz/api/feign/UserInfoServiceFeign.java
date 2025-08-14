package com.hnz.api.feign;

import com.hnz.result.R;
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

@FeignClient(value = "main-service")
public interface UserInfoServiceFeign {
    @PostMapping("/userinfo/updateFace")
    R updateFace(@RequestParam("userId") String userId, @RequestParam("face") String face);

}
