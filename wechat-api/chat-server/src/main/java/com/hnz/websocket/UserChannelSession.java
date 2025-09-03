package com.hnz.websocket;


import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：UserChannelSession
 * @Date：2025/9/3 15:36
 * @Filename：UserChannelSession
 */
public class UserChannelSession {
//    多端同时接收消息，允许同一账号多个设备同时在线
    private static Map<String, List<Channel>> multiChannels = new HashMap<>();
//    用于记录用户id和客户端channel长id的关联关系
    private static Map<String, String> userChannelIdRelation = new HashMap<>();
    public static void putUserChannelIdRelation(String channelId, String userId) {
        userChannelIdRelation.put(channelId, userId);
    }
    public static String getUserIdByChannelId(String channelId) {
        return userChannelIdRelation.get(channelId);
    }
    public static void putMultiChannels(String userId, Channel channel) {
        List<Channel> channels = getMultiChannels(userId);
        if (channels == null || channels.isEmpty()) {
            channels = new ArrayList<>();
        }
        channels.add(channel);
        multiChannels.put(userId, channels);
    }
    public static void removeUselessChannel(String userId, String channelId) {
        List<Channel> channels = getMultiChannels(userId);
        if (channels == null && channels.isEmpty()) {
            return;
        }
        channels.removeIf(channel -> channel.id().asLongText().equals(channelId));
        multiChannels.put(userId, channels);
    }
    public static List<Channel> getMultiChannels(String userId) {
        return multiChannels.get(userId);
    }
    public static void outputMulti() {
        System.out.println("==========================");
        for (Map.Entry<String, List<Channel>> entry : multiChannels.entrySet()) {
            System.out.println("----------------------");
            System.out.println("userId:"+entry.getKey());
            entry.getValue().forEach(c -> System.out.println("\t\t channelId:"+c.id().asLongText()));
            System.out.println("----------------------");
        }
        System.out.println("==========================");
    }
}
