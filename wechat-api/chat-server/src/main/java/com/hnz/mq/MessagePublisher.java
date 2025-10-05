package com.hnz.mq;

import com.hnz.netty.ChatMsg;
import com.hnz.utils.JsonUtils;

import java.util.Objects;

public class MessagePublisher {

    //    定义一个交换机
    public static final String EXCHANGE_NAME = "wechat.exchange";
    //    定义一个队列
    public static final String QUEUE_NAME = "wechat.queue";
    //    路由地址
    public static final String ROUTING_KEY = "hnz.wechat.test.send";
    //    发送信息到消息队列接受并保存到数据库的路由地址
    public static final String ROUTING_KEY_WECHAT_MSG_SEND = "hnz.wechat.WECHAT.msg.send";

    public static void sendMsgToSave(ChatMsg msg) throws Exception {
        RabbitMQConnectUtils connectUtils = new RabbitMQConnectUtils();
        connectUtils.sendMsg(Objects.requireNonNull(JsonUtils.objectToJson(msg)), EXCHANGE_NAME, ROUTING_KEY_WECHAT_MSG_SEND);
    }
    public static void sendMsgToNettyServers(String msg) throws Exception {
        RabbitMQConnectUtils connectUtils = new RabbitMQConnectUtils();
        String fanoutExchange = "fanout_exchange_hnz";
        connectUtils.sendMsg(msg, fanoutExchange, "");
    }

}
