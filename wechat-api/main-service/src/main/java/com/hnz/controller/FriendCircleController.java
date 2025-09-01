package com.hnz.controller;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.hnz.base.BaseInfoProperties;
import com.hnz.bo.FriendCircleBO;
import com.hnz.entity.FriendCircleLiked;
import com.hnz.result.R;
import com.hnz.service.CommentService;
import com.hnz.service.FriendCircleService;
import com.hnz.utils.PagedGridResult;
import com.hnz.vo.CommentVO;
import com.hnz.vo.FriendCircleVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("friendCircle")
public class FriendCircleController extends BaseInfoProperties {

    @Resource
    private FriendCircleService friendCircleService;

    @Resource
    private CommentService commentService;

    @PostMapping("publish")
    public R publish(@RequestBody FriendCircleBO friendCircleBO,
                     HttpServletRequest request) {

        String userId = request.getHeader(HEADER_USER_ID);

        friendCircleBO.setUserId(userId);
        friendCircleBO.setPublishTime(LocalDateTime.now());

        friendCircleService.publish(friendCircleBO);

        return R.ok();
    }

    @PostMapping("queryList")
    public R publish(String userId,
       @RequestParam(defaultValue = "1", name = "page") Integer page,
       @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {

        if (StringUtils.isBlank(userId)) return R.error();

        PagedGridResult gridResult = friendCircleService.queryList(userId, page, pageSize);

        List<FriendCircleVO> list = (List<FriendCircleVO>)gridResult.getRows();
        for (FriendCircleVO f : list) {
            String friendCircleId = f.getFriendCircleId();
            List<FriendCircleLiked> likedList = friendCircleService.queryLikedFriends(friendCircleId);
            f.setLikedFriends(likedList);

            boolean res = friendCircleService.doILike(friendCircleId, userId);
            f.setDoILike(res);

            List<CommentVO> commentList = commentService.queryAll(friendCircleId);
            f.setCommentList(commentList);
        }

        return R.ok(gridResult);
    }

    @PostMapping("like")
    public R like(String friendCircleId,
                                HttpServletRequest request) {

        String userId = request.getHeader(HEADER_USER_ID);
        friendCircleService.like(friendCircleId, userId);

        return R.ok();
    }

    @PostMapping("unlike")
    public R unlike(String friendCircleId,
                                  HttpServletRequest request) {

        String userId = request.getHeader(HEADER_USER_ID);
        friendCircleService.unlike(friendCircleId, userId);

        return R.ok();
    }

    @PostMapping("likedFriends")
    public R likedFriends(String friendCircleId,
                                  HttpServletRequest request) {
        List<FriendCircleLiked> likedList =
                friendCircleService.queryLikedFriends(friendCircleId);
        return R.ok(likedList);
    }

    @PostMapping("delete")
    public R delete(String friendCircleId,
                                  HttpServletRequest request) {

        String userId = request.getHeader(HEADER_USER_ID);
        friendCircleService.delete(friendCircleId, userId);

        return R.ok();
    }
}
