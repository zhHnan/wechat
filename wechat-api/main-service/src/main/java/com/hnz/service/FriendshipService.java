package com.hnz.service;

import com.hnz.entity.Friendship;

public interface FriendshipService {

    Friendship getFriendship(String myId, String friendId);

    Object queryMyFriends(String myId, boolean b);

    void updateFriendRemark(String myId, String friendId, String friendRemark);
}
