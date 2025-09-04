package com.hnz.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HttpHandler
 * @Date：2025/9/2 16:03
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(io.netty.channel.ChannelHandlerContext ctx, Object evt) throws Exception {
        //        判断evt 是不是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲）
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    System.out.println("读空闲");
                    break;
                case WRITER_IDLE:
                    System.out.println("写空闲");
                    break;
                case ALL_IDLE:
                    System.out.println("ALL_IDLE,关闭前clients的数量：" + ChatHandler.clients.size());
                    Channel channel = ctx.channel();
                    channel.close();
                    System.out.println("ALL_IDLE,关闭后clients的数量：" + ChatHandler.clients.size());
            }
        }
    }
}
