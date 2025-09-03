package com.hnz.service;

import com.hnz.bo.CommentBO;
import com.hnz.vo.CommentVO;

import java.util.List;

/**
 * <p>
 * 文章评论表 服务类
 * </p>
 */
public interface CommentService {
    /**
     * 创建发表评论
     * @param commentBO
     */
    CommentVO createComment(CommentBO commentBO);

    /**
     * 查询朋友圈的列表
     * @param friendCircleId
     * @return
     */
    List<CommentVO> queryAll(String friendCircleId);

    /**
     * 删除朋友圈的评论
     * @param commentUserId
     * @param commentId
     * @param friendCircleId
     */
    void deleteComment(String commentUserId, String commentId, String friendCircleId);

}
