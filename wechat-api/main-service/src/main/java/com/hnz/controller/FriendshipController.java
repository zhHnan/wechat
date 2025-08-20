package com.hnz.controller;

import com.hnz.entity.Friendship;
import com.hnz.result.R;
import com.hnz.service.FriendshipService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hnz.base.BaseInfoProperties.HEADER_USER_ID;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：FriendshipController
 * @Date：2025/8/20 21:17
 * @Filename：FriendshipController
 */
@RestController
@RequestMapping("friendship")
@Slf4j
public class FriendshipController {

    @Resource
    private FriendshipService friendshipService;
    @PostMapping("getFriendship")
    public R pass(String friendId, HttpServletRequest request) {

        String myId = request.getHeader(HEADER_USER_ID);

        Friendship friendship = friendshipService.getFriendship(myId, friendId);
        return R.ok(friendship);
    }
}
