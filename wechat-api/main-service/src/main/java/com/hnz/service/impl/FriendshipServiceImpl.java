package com.hnz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnz.entity.Friendship;
import com.hnz.mapper.FriendshipMapper;
import com.hnz.service.FriendshipService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 朋友关系表 服务实现类
 * </p>
 */
@Service
public class FriendshipServiceImpl extends ServiceImpl<FriendshipMapper, Friendship> implements FriendshipService {

}
