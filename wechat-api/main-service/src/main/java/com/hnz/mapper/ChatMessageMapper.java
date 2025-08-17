package com.hnz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hnz.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 聊天信息存储表 Mapper 接口
 * </p>
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

}
