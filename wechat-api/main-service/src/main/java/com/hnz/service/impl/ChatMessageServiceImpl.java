package com.hnz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnz.base.BaseInfoProperties;
import com.hnz.entity.ChatMessage;
import com.hnz.mapper.ChatMessageMapper;
import com.hnz.netty.ChatMsg;
import com.hnz.service.ChatMessageService;
import com.hnz.utils.PagedGridResult;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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

        String receiverId = chatMsg.getReceiverId();
        String senderId = chatMsg.getSenderId();
//        通过redis累加信息接收者的对应记录
        redis.incrementHash(CHAT_MSG_LIST + ":" + receiverId, senderId, 1);
    }

    @Override
    public PagedGridResult queryMsgList(String sendId, String receiveId, Integer page, Integer pageSize) {
        Page<ChatMessage> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.or(qw -> qw.eq("sender_id", sendId).eq("receiver_id", receiveId));
        queryWrapper.or(qw -> qw.eq("sender_id", receiveId).eq("receiver_id", sendId));
        queryWrapper.orderByDesc("chat_time");
        chatMessageMapper.selectPage(pageInfo, queryWrapper);
        List<ChatMessage> list = pageInfo.getRecords().stream().sorted(
                Comparator.comparing(ChatMessage::getChatTime)
        ).toList();
        pageInfo.setRecords(list);
        return setterPagedGridPlus(pageInfo);
    }

    @Transactional
    @Override
    public void updateMsgSignRead(String msgId) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(msgId);
        chatMessage.setIsRead(true);
        chatMessageMapper.updateById(chatMessage);
    }
}
