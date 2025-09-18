package com.hnz.service.impl;


import com.hnz.base.BaseInfoProperties;
import com.hnz.entity.ChatMessage;
import com.hnz.mapper.ChatMessageMapper;
import com.hnz.netty.ChatMsg;
import com.hnz.service.ChatMessageService;
import jakarta.annotation.Resource;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 聊天信息存储表 服务实现类
 * </p>
 */
@Service
public class ChatMessageServiceImpl extends BaseInfoProperties implements ChatMessageService {
    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Transactional
    @Override
    public void saveMsg(ChatMsg chatMsg) {
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMsg, chatMessage);
        chatMessage.setId(chatMsg.getMsgId());
        chatMessageMapper.insert(chatMessage);
    }
}
