package com.hnz.websocket;

import com.hnz.enums.MsgTypeEnum;
import com.hnz.netty.ChatMsg;
import com.hnz.netty.DataContent;
import com.hnz.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HttpHandler
 * @Date：2025/9/2 16:03
 * @Filename：HttpHandler
 */
//SimpleChannelInboundHandler 泛型参数为接收到的数据类型
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        System.out.println("接收到的内容："+content);
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        if (dataContent == null) {
            return;
        }
        ChatMsg chatMsg = dataContent.getChatMsg();
        String msgText = chatMsg.getMsg();
        String receiverId = chatMsg.getReceiverId();
        String senderId = chatMsg.getSenderId();
        chatMsg.setChatTime(LocalDateTime.now());
        Integer msgType = chatMsg.getMsgType();

        Channel channel = ctx.channel();
        String curChannelId = channel.id().asLongText();
        String curChannelIdShort = channel.id().asShortText();
//        System.out.println("当前通道的id："+curChannelId + "，短id："+curChannelIdShort);
//        判断消息类型，根据不同的类型处理不同业务
        if(Objects.equals(msgType, MsgTypeEnum.CONNECT_INIT.getType())){
//            当websocket连接初始化时，把channel和用户id关联
            UserChannelSession.putUserChannelIdRelation(curChannelId, senderId);
            UserChannelSession.putMultiChannels(senderId, channel);
        }
        UserChannelSession.outputMulti();
//        channel.writeAndFlush(new TextWebSocketFrame(content));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        String curChannelId = channel.id().asLongText();
        System.out.println("服务端发生异常："+curChannelId);
        ctx.channel().close();
        clients.remove(channel);

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String curChannelId = channel.id().asLongText();
        System.out.println("客户端建立连接，当前通道的长id："+curChannelId);
//        存入到group中
        clients.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String curChannelId = channel.id().asLongText();
        System.out.println("客户端关闭连接，当前通道的长id："+curChannelId);
        clients.remove(channel);
    }
}
