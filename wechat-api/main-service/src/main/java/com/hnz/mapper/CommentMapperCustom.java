package com.hnz.mapper;

import com.hnz.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：CommentMapperCustom
 * @Date：2025/9/1 15:30
 * @Filename：CommentMapperCustom
 */

@Mapper
public interface CommentMapperCustom {
    List<CommentVO> queryFriendCircleComments(@Param("paramMap") Map<String, Object> map);

}
