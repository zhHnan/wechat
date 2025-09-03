package com.hnz.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HttpServerInitializer
 * @Date：2025/9/2 15:56
 * @Filename：HttpServerInitializer
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
//    初始化器，channel注册后会执行相应的初始化方法
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        通过socketChannel获取pipeline，然后添加handle
        ChannelPipeline pipeline = socketChannel.pipeline();
//        netty 提供的httpServerCodec，用于解析http请求，当请求到服务器时，会自动解码，将http请求解码为http对象
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpHandler", new HttpHandler());
    }
}
