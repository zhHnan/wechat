package com.hnz.service;

import com.hnz.entity.ChatMessage;
import com.hnz.netty.ChatMsg;

/**
 * <p>
 * 聊天信息存储表 服务类
 * </p>
 */
public interface ChatMessageService {

    void saveMsg(ChatMsg chatMsg);

}
