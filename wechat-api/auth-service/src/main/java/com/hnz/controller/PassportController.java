package com.hnz.controller;

import com.hnz.base.BaseInfoProperties;
import com.hnz.bo.RegistLoginBO;
import com.hnz.entity.Users;
import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import com.hnz.service.UsersService;
import com.hnz.utils.IPUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/passport")
@Slf4j
public class PassportController extends BaseInfoProperties {

    @Resource
    private UsersService usersService;

    @PostMapping("getSMSCode")
    public R getSmsCode(String mobile, HttpServletRequest request) {
        if (StringUtils.isBlank(mobile)) {
            return R.errorMsg("手机号不能为空");
        }
        String userIp = IPUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, mobile);
        String code = String.format("%06d", (int) (Math.random() * 999999));

//        存入到redis
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);
        log.info("存入redis：{}", redis.get(MOBILE_SMSCODE + ":" + mobile));
        return R.ok();
    }

    @PostMapping("regist")
    public R regist(@RequestBody RegistLoginBO registLoginB0, HttpServletRequest request) {
        String mobile = registLoginB0.getMobile();
        String code = registLoginB0.getSmsCode();
        String nickname = registLoginB0.getNickname();
//        从redis中获取验证码进行验证
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return R.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        Users user = usersService.queryMobileIfExist(mobile);
        if (ObjectUtils.isEmpty(user)) {
            user = usersService.createUsers(mobile, nickname);
        } else {
            return R.errorCustom(ResponseStatusEnum.USER_ALREADY_EXIST_ERROR);
        }
        redis.del(MOBILE_SMSCODE + ":" + mobile);
        return R.ok(user);
    }

    @PostMapping("login")
    public R login(@RequestBody RegistLoginBO registLoginB0, HttpServletRequest request) {
        String mobile = registLoginB0.getMobile();
        String code = registLoginB0.getSmsCode();
//        从redis中获取验证码进行验证
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return R.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        Users user = usersService.queryMobileIfExist(mobile);
        if (ObjectUtils.isEmpty(user)) {
            return R.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        redis.del(MOBILE_SMSCODE + ":" + mobile);
        return R.ok(user);
    }
}
