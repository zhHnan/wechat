package com.hnz.mapper;

import com.hnz.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 好友请求记录表 Mapper 接口
 * </p>
 */

@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

}
