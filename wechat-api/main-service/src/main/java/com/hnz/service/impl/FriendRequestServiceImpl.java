package com.hnz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnz.base.BaseInfoProperties;
import com.hnz.bo.NewFriendRequestBO;
import com.hnz.entity.FriendRequest;
import com.hnz.enums.FriendRequestVerifyStatus;
import com.hnz.mapper.FriendRequestMapper;
import com.hnz.mapper.FriendRequestMapperCustom;
import com.hnz.service.FriendRequestService;
import com.hnz.utils.PagedGridResult;
import com.hnz.vo.NewFriendsVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 好友请求记录表 服务实现类
 * </p>
 */
@Service
public class FriendRequestServiceImpl extends BaseInfoProperties implements FriendRequestService {

    @Resource
    private FriendRequestMapper friendRequestMapper;
    @Resource
    private FriendRequestMapperCustom friendRequestMapperCustom;

    @Transactional
    @Override
    public void addNewFriendRequest(NewFriendRequestBO newFriendRequestBO) {
//        先删除之前的好友请求
        QueryWrapper<FriendRequest> queryWrapper = new QueryWrapper<FriendRequest>()
                .eq("my_id", newFriendRequestBO.getMyId())
                .eq("friend_id", newFriendRequestBO.getFriendId());
        friendRequestMapper.delete(queryWrapper);
//        添加新的好友请求
        FriendRequest pendingFriend = new FriendRequest();
        BeanUtils.copyProperties(newFriendRequestBO, pendingFriend);
        pendingFriend.setRequestTime(LocalDateTime.now());
        pendingFriend.setVerifyStatus(FriendRequestVerifyStatus.WAIT.type);
        friendRequestMapper.insert(pendingFriend);
    }

    @Override
    public PagedGridResult queryNewFriendList(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("mySelfId", userId);

        Page<NewFriendsVO> pageInfo = new Page<>(page, pageSize);
        friendRequestMapperCustom.queryNewFriendList(pageInfo, map);

        return setterPagedGridPlus(pageInfo);
    }
}
