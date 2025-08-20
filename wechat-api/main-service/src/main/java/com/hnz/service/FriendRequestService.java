package com.hnz.service;

import com.hnz.bo.NewFriendRequestBO;
import com.hnz.utils.PagedGridResult;

public interface FriendRequestService {


    void addNewFriendRequest(NewFriendRequestBO newFriendRequestBO);
    PagedGridResult queryNewFriendList(String userId, Integer page, Integer pageSize);

    void passNewFriend(String friendRequestId, String friendRemark);
}
