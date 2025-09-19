package com.hnz.netty;

import com.hnz.utils.JedisPoolUtils;
import com.hnz.websocket.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：ChatServer
 * @Date：2025/9/2 15:45
 * @Filename：ChatServer
 */
public class ChatServer2 {
    public static void main(String[] args) {
//         定义主、从线程组
//         主线程池：处理连接请求，但是不做业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
//         从线程组：处理业务请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        netty服务启动的时候从redis中查找端口，若没有则选择默认的875
        Integer nettyPort = selectPort(nettyDefaultPort);
        try {
//         构建服务器端启动对象
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new WebSocketServerInitializer());
//        绑定端口，并启动
            ChannelFuture channelFuture = server.bind(nettyPort).sync();
//        监听关闭的channel
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static final Integer nettyDefaultPort = 875;
    public static final String initOnlineCounts = "0";
    public static Integer selectPort(Integer port){
        String portKey = "netty_port";
        Jedis jedis = JedisPoolUtils.getJedis();
        Map<String, String> portMap = jedis.hgetAll(portKey);

        List<Integer> portList = portMap.keySet().stream().map(Integer::valueOf).toList();
        if(portList.isEmpty()){
            jedis.hset(portKey, String.valueOf(port), initOnlineCounts);
            return port;
        }
        Optional<Integer> maxPort = portList.stream().max(Integer::compareTo);
        int curPort = maxPort.get() + 10;
        jedis.hset(portKey, String.valueOf(curPort), initOnlineCounts);
        return curPort;
    }
}
