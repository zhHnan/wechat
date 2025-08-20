package com.hnz.controller;

import com.hnz.base.BaseInfoProperties;
import com.hnz.bo.NewFriendRequestBO;
import com.hnz.result.R;
import com.hnz.service.FriendRequestService;
import com.hnz.utils.PagedGridResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
public class FriendRequestController extends BaseInfoProperties {
    @Resource
    private FriendRequestService friendRequestService;

    @PostMapping("add")
    public R add(@RequestBody @Valid NewFriendRequestBO friendRequestBO) {
        friendRequestService.addNewFriendRequest(friendRequestBO);
        return R.ok();
    }
    @PostMapping("queryNew")
    public R queryNew(@RequestParam(defaultValue = "1", name = "page") Integer page, @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        PagedGridResult pagedGridResult = friendRequestService.queryNewFriendList(userId, page, pageSize);
        return R.ok(pagedGridResult);
    }
    @PostMapping("pass")
    public R pass(String friendRequestId, String friendRemark) {
        friendRequestService.passNewFriend(friendRequestId, friendRemark);
        return R.ok();
    }

}
