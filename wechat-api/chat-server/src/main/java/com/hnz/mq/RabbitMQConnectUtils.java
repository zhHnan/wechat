package com.hnz.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RabbitMQConnectUtils {

    private final List<Connection> connections = new ArrayList<>();
    private final int maxConnection = 20;

    // 开发环境 dev
    private final String host = "10.85.49.237";
    private final int port = 5672;
    private final String username = "hnz";
    private final String password = "admin123";
    private final String virtualHost = "wechat";

    // 生产环境 prod
    //private final String host = "";
    //private final int port = 5672;
    //private final String username = "123";
    //private final String password = "123";
    //private final String virtualHost = "123";

    public ConnectionFactory factory;

    public ConnectionFactory getRabbitMqConnection() {
        return getFactory();
    }

    public ConnectionFactory getFactory() {
        initFactory();
        return factory;
    }

    private void initFactory() {
        try {
            if (factory == null) {
                factory = new ConnectionFactory();
                factory.setHost(host);
                factory.setPort(port);
                factory.setUsername(username);
                factory.setPassword(password);
                factory.setVirtualHost(virtualHost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String message, String queue) throws Exception {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.basicPublish("", queue, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("utf-8"));
        channel.close();
        setConnection(connection);
    }

    public void sendMsg(String message, String exchange, String routingKey) throws Exception {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("utf-8"));
        channel.close();
        setConnection(connection);
    }

    public GetResponse basicGet(String queue, boolean autoAck) throws Exception {
        GetResponse getResponse = null;
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        getResponse = channel.basicGet(queue, autoAck);
        channel.close();
        setConnection(connection);
        return getResponse;
    }

    public Connection getConnection() throws Exception {
        return getAndSetConnection(true, null);
    }

    public void setConnection(Connection connection) throws Exception {
        getAndSetConnection(false, connection);
    }
    public void listen(String exchangeName, String queueName) throws Exception{
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
//        发布订阅模式
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
//        队列绑定到交换机
        channel.queueBind(queueName, exchangeName, "");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) {
                String exchange = envelope.getExchange();
                if (exchange.equalsIgnoreCase(exchangeName)) {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("消费者收到消息：" + message);
                }
            }
        };
        /*
         queue: 队列名称
          autoAck: 是否自动确认, true 自动确认, false 手动确认
          callback: 消费者
        */
        channel.basicConsume(queueName, true, consumer);
    }
    private synchronized Connection getAndSetConnection(boolean isGet, Connection connection) throws Exception {
        getRabbitMqConnection();

        if (isGet) {
            if (connections.isEmpty()) {
                return factory.newConnection();
            }
            Connection newConnection = connections.get(0);
            connections.remove(0);
            if (newConnection.isOpen()) {
                return newConnection;
            } else {
                return factory.newConnection();
            }
        } else {
            if (connections.size() < maxConnection) {
                connections.add(connection);
            }
            return null;
        }
    }

}
