package com.hnz.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.hnz.base.BaseInfoProperties;
import com.hnz.bo.CommentBO;
import com.hnz.result.R;
import com.hnz.service.CommentService;
import com.hnz.vo.CommentVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：CommentController
 * @Date：2025/9/1 16:29
 * @Filename：CommentController
 */
@RestController
@RequestMapping("comment")
public class CommentController extends BaseInfoProperties {

    @Resource
    private CommentService commentService;

    @PostMapping("create")
    public R create(@RequestBody CommentBO friendCircleBO,
                    HttpServletRequest request) {
        CommentVO commentVO = commentService.createComment(friendCircleBO);
        return R.ok(commentVO);
    }

    @PostMapping("query")
    public R create(String friendCircleId) {
        return R.ok(commentService.queryAll(friendCircleId));
    }

    @PostMapping("delete")
    public R delete(String commentUserId,
                                  String commentId,
                                  String friendCircleId) {

        if (StringUtils.isBlank(commentUserId) ||
                StringUtils.isBlank(commentId) ||
                StringUtils.isBlank(friendCircleId)
        ) {
            return R.error();
        }

        commentService.deleteComment(commentUserId, commentId, friendCircleId);
        return R.ok();
    }

}

