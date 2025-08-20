package com.hnz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnz.entity.FriendRequest;
import com.hnz.entity.Friendship;
import com.hnz.mapper.FriendshipMapper;
import com.hnz.service.FriendshipService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 朋友关系表 服务实现类
 * </p>
 */
@Service
public class FriendshipServiceImpl extends ServiceImpl<FriendshipMapper, Friendship> implements FriendshipService {
    @Resource
    private FriendshipMapper friendshipMapper;
//
//    @Resource
//    private FriendshipMapperCustom friendshipMapperCustom;
    @Override
    public Friendship getFriendship(String myId, String friendId) {

        QueryWrapper<Friendship> queryWrapper = new QueryWrapper<Friendship>()
                .eq("my_id", myId)
                .eq("friend_id", friendId);

        return friendshipMapper.selectOne(queryWrapper);
    }
}
