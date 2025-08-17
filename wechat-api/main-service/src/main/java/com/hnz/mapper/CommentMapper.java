package com.hnz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnz.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章评论表 Mapper 接口
 * </p>
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
