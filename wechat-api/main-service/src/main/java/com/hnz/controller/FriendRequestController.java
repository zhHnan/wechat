package com.hnz.controller;

import com.hnz.bo.NewFriendRequestBO;
import com.hnz.result.R;
import com.hnz.service.FriendRequestService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("friendRequest")
@Slf4j
public class FriendRequestController {
    @Resource
    private FriendRequestService friendRequestService;

    @PostMapping("add")
    public R add(@RequestBody @Valid NewFriendRequestBO friendRequestBO) {

        friendRequestService.addNewFriendRequest(friendRequestBO);
        return R.ok();
    }
}
