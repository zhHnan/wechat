package com.hnz.service;

import com.hnz.entity.Friendship;
import com.hnz.enums.YesOrNo;

public interface FriendshipService {

    Friendship getFriendship(String myId, String friendId);

    Object queryMyFriends(String myId, boolean b);

    void updateFriendRemark(String myId, String friendId, String friendRemark);

    void updateBlackList(String myId, String friendId, YesOrNo yesOrNo);

    void delete(String myId, String friendId);

    boolean isBlackEachOther(String friendId1st, String friendId2nd);
}
