package com.hnz.websocket;

import com.a3test.component.idworker.IdWorkerConfigBean;
import com.a3test.component.idworker.Snowflake;
import com.hnz.enums.MsgTypeEnum;
import com.hnz.mq.MessagePublisher;
import com.hnz.netty.ChatMsg;
import com.hnz.netty.DataContent;
import com.hnz.result.R;
import com.hnz.utils.JsonUtils;
import com.hnz.utils.LocalDateUtils;
import com.hnz.utils.OkHttpUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.util.List;
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
//        判断双方是否为黑名单
        R r = OkHttpUtil.get("http://127.0.0.1:8092/friendship/isBlack?friendId1st=" + receiverId + "&friendId2nd=" + senderId);
        if (r == null || (Boolean) r.getData()) {
            return;
        }
        chatMsg.setChatTime(LocalDateTime.now());
        Integer msgType = chatMsg.getMsgType();

        Channel channel = ctx.channel();
        String curChannelId = channel.id().asLongText();
//        判断消息类型，根据不同的类型处理不同业务
        if(Objects.equals(msgType, MsgTypeEnum.CONNECT_INIT.type)){
//            当websocket连接初始化时，把channel和用户id关联
            UserChannelSession.putUserChannelIdRelation(curChannelId, senderId);
            UserChannelSession.putMultiChannels(senderId, channel);
        }else if (Objects.equals(msgType, MsgTypeEnum.WORDS.type) || Objects.equals(msgType, MsgTypeEnum.IMAGE.type)
                || Objects.equals(msgType, MsgTypeEnum.VIDEO.type) || Objects.equals(msgType, MsgTypeEnum.VOICE.type)){
//            mq异步解耦，保存信息到数据库，由于数据库无法获得信息的主键id，所以使用snowflake算法生成id
            Snowflake snowflake = new Snowflake(new IdWorkerConfigBean());
            chatMsg.setMsgId(snowflake.nextId());

//            发送消息
            List<Channel> receiverChannels = UserChannelSession.getMultiChannels(receiverId);
            if (receiverChannels == null || receiverChannels.isEmpty()){
//                用户离线
                System.out.println("接收方没有在线");
                chatMsg.setIsReceiverOnLine(false);
            }else {
                chatMsg.setIsReceiverOnLine(true);
                sendMsgToChannel(dataContent,msgType, chatMsg, receiverChannels);
            }
        }
//        同步消息给同一账号的其他通道
        List<Channel> myOtherChannels = UserChannelSession.getMyOtherChannels(senderId, curChannelId);
        if (myOtherChannels != null && !myOtherChannels.isEmpty()) {
            sendMsgToChannel(dataContent,msgType, chatMsg, myOtherChannels);
        }
//        把聊天信息作为mq的消息发送给消费者进行消费处理（保存到数据库）
        MessagePublisher.sendMsgToSave(chatMsg);
        UserChannelSession.outputMulti();
//        channel.writeAndFlush(new TextWebSocketFrame(content));
    }

    private void sendMsgToChannel(DataContent dataContent,Integer MsgType, ChatMsg chatMsg, List<Channel> myOtherChannels) {
        for (Channel myOtherChannel : myOtherChannels) {
            Channel findChan = clients.find(myOtherChannel.id());
            if (findChan != null){
                if (Objects.equals(MsgType, MsgTypeEnum.VOICE.type)){
                    chatMsg.setIsRead(false);
                }
                dataContent.setChatMsg(chatMsg);
                String format = LocalDateUtils.format(chatMsg.getChatTime(), LocalDateUtils.DATETIME_PATTERN_2);
                dataContent.setChatTime(format);
                findChan.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        String curChannelId = channel.id().asLongText();
        System.out.println("服务端发生异常："+curChannelId);
        UserChannelSession.removeUselessChannel(UserChannelSession.getUserIdByChannelId(curChannelId), curChannelId);
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
        UserChannelSession.removeUselessChannel(UserChannelSession.getUserIdByChannelId(curChannelId), curChannelId);
        clients.remove(channel);
    }
}
