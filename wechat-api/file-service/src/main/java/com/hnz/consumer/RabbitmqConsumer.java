package com.hnz.consumer;

import com.hnz.config.RabbitMQConfig;
import com.hnz.netty.ChatMsg;
import com.hnz.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：RabbitmqConsumer
 * @Date：2025/9/13 10:08
 * @Filename：RabbitmqConsumer
 */

@Slf4j
public class RabbitmqConsumer {
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_NAME})
    public void watchQueue(String payload, Message msg){
        log.info("接收到的消息：{}",payload);
        String receivedRoutingKey = msg.getMessageProperties().getReceivedRoutingKey();
        log.info("接收到的路由key：{}",receivedRoutingKey);
        if(receivedRoutingKey.equals(RabbitMQConfig.ROUTING_KEY_WECHAT_MSG_SEND)){
            ChatMsg chatMsg = JsonUtils.jsonToPojo(payload, ChatMsg.class);
            log.info("接收到的消息：{}",chatMsg.toString());
        }
    }
}
