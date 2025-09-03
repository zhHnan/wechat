package com.hnz.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HttpServerInitializer
 * @Date：2025/9/2 15:56
 * @Filename：HttpServerInitializer
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

//    初始化器，channel注册后会执行相应的初始化方法
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        通过socketChannel获取pipeline，然后添加handle
        ChannelPipeline pipeline = socketChannel.pipeline();
//        netty 提供的httpServerCodec，用于解析http请求，当请求到服务器时，会自动解码，将http请求解码为http对象
        pipeline.addLast(new HttpServerCodec());
//        添加对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
//        对http请求进行聚合，聚合成FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
//        对 websocket 支持
//        websocket 服务器端处理类，用于处理websocket连接业务
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new ChatHandler());
    }
}
