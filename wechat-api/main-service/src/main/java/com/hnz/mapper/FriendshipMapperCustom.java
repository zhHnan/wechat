package com.hnz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnz.entity.Friendship;
import com.hnz.vo.ContactsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 朋友关系表 Mapper 接口
 * </p>
 */

@Mapper
public interface FriendshipMapperCustom extends BaseMapper<Friendship> {
    List<ContactsVO> queryMyFriends(@Param("paramMap") Map<String, Object> paramMap);
}
