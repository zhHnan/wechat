package com.hnz.service;

import com.hnz.entity.Friendship;

public interface FriendshipService {

    Friendship getFriendship(String myId, String friendId);
}
