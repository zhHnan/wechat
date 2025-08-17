package com.hnz.mapper;

import com.hnz.entity.Friendship;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 朋友关系表 Mapper 接口
 * </p>
 */

@Mapper
public interface FriendshipMapper extends BaseMapper<Friendship> {

}
