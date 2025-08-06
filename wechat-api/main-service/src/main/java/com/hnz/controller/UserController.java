package com.hnz.controller;

import com.hnz.bo.ModifyUserBO;
import com.hnz.result.R;
import com.hnz.service.UsersService;
import jakarta.annotation.Resource;
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
@RequestMapping("/userinfo")
public class UserController {
    @Resource
    private UsersService usersService;
    @PostMapping("/modify")
    public R modify(@RequestBody ModifyUserBO modifyUserBO) {
        usersService.modifyUserInfo(modifyUserBO);
        return R.ok();
    }
}
