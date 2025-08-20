package com.hnz.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.hnz.entity.Friendship;
import com.hnz.enums.YesOrNo;
import com.hnz.result.R;
import com.hnz.service.FriendshipService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public R pass(@RequestParam("friendId") String friendId, HttpServletRequest request) {

        String myId = request.getHeader(HEADER_USER_ID);

        Friendship friendship = friendshipService.getFriendship(myId, friendId);
        return R.ok(friendship);
    }

    @PostMapping("queryMyFriends")
    public R queryMyFriends(HttpServletRequest request) {
        String myId = request.getHeader(HEADER_USER_ID);
        return R.ok(friendshipService.queryMyFriends(myId, false));
    }

    @PostMapping("queryMyBlackList")
    public R queryMyBlackList(HttpServletRequest request) {
        String myId = request.getHeader(HEADER_USER_ID);
        return R.ok(friendshipService.queryMyFriends(myId, true));
    }

    @PostMapping("updateFriendRemark")
    public R updateFriendRemark(HttpServletRequest request, @RequestParam("friendId") String friendId, @RequestParam("friendRemark") String friendRemark) {

        if (StringUtils.isBlank(friendId) || StringUtils.isBlank(friendRemark)) {
            return R.error();
        }

        String myId = request.getHeader(HEADER_USER_ID);
        friendshipService.updateFriendRemark(myId, friendId, friendRemark);
        return R.ok();
    }

    @PostMapping("tobeBlack")
    public R tobeBlack(HttpServletRequest request, @RequestParam("friendId") String friendId) {

        if (StringUtils.isBlank(friendId)) {
            return R.error();
        }

        String myId = request.getHeader(HEADER_USER_ID);
        friendshipService.updateBlackList(myId, friendId, YesOrNo.YES);
        return R.ok();
    }

    @PostMapping("moveOutBlack")
    public R moveOutBlack(HttpServletRequest request, @RequestParam("friendId") String friendId) {

        if (StringUtils.isBlank(friendId)) {
            return R.error();
        }

        String myId = request.getHeader(HEADER_USER_ID);
        friendshipService.updateBlackList(myId, friendId, YesOrNo.NO);
        return R.ok();
    }

    @PostMapping("delete")
    public R delete(HttpServletRequest request, @RequestParam("friendId") String friendId) {

        if (StringUtils.isBlank(friendId)) {
            return R.error();
        }

        String myId = request.getHeader(HEADER_USER_ID);
        friendshipService.delete(myId, friendId);
        return R.ok();
    }

    @GetMapping("isBlack")
    public R isBlack(@RequestParam("friendId1st") String friendId1st, @RequestParam("friendId2nd") String friendId2nd) {

        // 需要进行两次查询，A拉黑B，B拉黑A，AB相互拉黑
        // 只需要符合其中的一个条件，就表示双发发送消息不可送达
        return R.ok(friendshipService.isBlackEachOther(friendId1st, friendId2nd));
    }
}
