package com.hnz.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.http.MediaType;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HttpHandler
 * @Date：2025/9/2 16:03
 * @Filename：HttpHandler
 */
//SimpleChannelInboundHandler 泛型参数为接收到的数据类型
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        Channel channel = ctx.channel();
        if (msg instanceof HttpRequest){
            //        打印客户端远程地址
            System.out.println(channel.remoteAddress());
//        定义缓冲区发送的消息，读写数据均通过缓冲区进行数据交换
            ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
//        构建http的响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.writeAndFlush(response);
        }
    }
}
