package com.hnz.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：RabbitMQConfig
 * @Date：2025/9/12 19:46
 * @Filename：RabbitMQConfig
 */

@Configuration
public class RabbitMQConfig {
    //    定义一个交换机
    public static final String EXCHANGE_NAME = "wechat.exchange";
    //    定义一个队列
    public static final String QUEUE_NAME = "wechat.queue";
    //    发送信息到消息队列接受并保存到数据库的路由地址
    public static final String ROUTING_KEY_WECHAT_MSG_SEND = "hnz.wechat.WECHAT.msg.send";
//    创建交换机
    @Bean(EXCHANGE_NAME)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable( true).build();
    }
//    创建队列
    @Bean(QUEUE_NAME)
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
//    队列绑定交换机
    @Bean
    public Binding binding(@Qualifier(EXCHANGE_NAME)Exchange exchange, @Qualifier(QUEUE_NAME)Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("hnz.wechat.#").noargs();
    }
}
