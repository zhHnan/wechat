package com.hnz.controller;

import com.hnz.config.RabbitMQConfig;
import com.hnz.netty.ChatMsg;
import com.hnz.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/9/12 20:22
 * @Filename：HelloController
 */

@RestController
@RequestMapping("userinfo")
public class HelloController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("mq")
    public Object mq() {
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setMsg("hello world");
        String msg = JsonUtils.objectToJson(chatMsg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_WECHAT_MSG_SEND, msg);
        return "ok";
    }
}
