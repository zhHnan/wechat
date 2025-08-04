package com.hnz.controller;

import com.hnz.base.BaseInfoProperties;
import com.hnz.result.R;
import com.hnz.utils.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/7/2 19:29
 * @Filename：HelloController
 */

@RestController
@RequestMapping("/auth")
@Slf4j
public class PassportController extends BaseInfoProperties {
    @GetMapping("getSmsCode")
    public R getSmsCode(String mobile, HttpServletRequest request) {
        if (StringUtils.isBlank(mobile)) {
            return R.errorMsg("手机号不能为空");
        }
        String userIp = IPUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, mobile);
        String code = String.format("%06d", (int)(Math.random() * 999999));

//        存入到redis
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);
        log.info("存入redis：{}", redis.get(MOBILE_SMSCODE + ":" + mobile));
        return R.ok();
    }
}
