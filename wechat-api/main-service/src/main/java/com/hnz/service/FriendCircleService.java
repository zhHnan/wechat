package com.hnz.service;

import com.hnz.bo.FriendCircleBO;
import com.hnz.entity.FriendCircleLiked;
import com.hnz.utils.PagedGridResult;

import java.util.List;

/**
 * <p>
 * 朋友圈表 服务类
 * </p>
 */
public interface FriendCircleService {
    /**
     * 发布朋友圈图文数据，保存到数据库
     * @param friendCircleBO
     */
    void publish(FriendCircleBO friendCircleBO);

    /**
     * 分页查询朋友圈图文列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryList(String userId,
                              Integer page,
                              Integer pageSize);

    /**
     * 点赞朋友圈
     * @param friendCircleId
     * @param userId
     */
    void like(String friendCircleId, String userId);

    /**
     * 取消(删除)点赞朋友圈
     * @param friendCircleId
     * @param userId
     */
    void unlike(String friendCircleId, String userId);

    /**
     * 查询朋友圈的点赞列表
     * @param friendCircleId
     * @return
     */
    List<FriendCircleLiked> queryLikedFriends(String friendCircleId);

    /**
     * 判断当前用户是否点赞过朋友圈
     * @param friendCircleId
     * @param userId
     * @return
     */
    boolean doILike(String friendCircleId, String userId);

    /**
     * 删除朋友圈图文数据
     * @param friendCircleId
     * @param userId
     */
    void delete(String friendCircleId, String userId);
}
