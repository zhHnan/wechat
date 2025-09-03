package com.hnz.netty;

import com.hnz.websocket.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：ChatServer
 * @Date：2025/9/2 15:45
 * @Filename：ChatServer
 */
public class ChatServer {
    public static void main(String[] args) {
//         定义主、从线程组
//         主线程池：处理连接请求，但是不做业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
//         从线程组：处理业务请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
//         构建服务器端启动对象
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new WebSocketServerInitializer());
//        绑定端口，并启动
            ChannelFuture channelFuture = server.bind(875).sync();
//        监听关闭的channel
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
