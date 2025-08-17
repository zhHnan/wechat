package com.hnz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnz.entity.FriendRequest;
import com.hnz.vo.NewFriendsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 好友请求记录表 Mapper 接口
 * </p>
 */

@Mapper
public interface FriendRequestMapperCustom extends BaseMapper<FriendRequest> {
    Page<NewFriendsVO> queryNewFriendList(@Param("page") Page<NewFriendsVO> page, @Param("paramMap")Map<String, Object> paramMap);
}
