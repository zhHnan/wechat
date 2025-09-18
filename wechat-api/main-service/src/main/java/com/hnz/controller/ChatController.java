package com.hnz.controller;

import com.hnz.base.BaseInfoProperties;
import com.hnz.config.RabbitMQConfig;
import com.hnz.netty.ChatMsg;
import com.hnz.result.R;
import com.hnz.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/9/12 20:22
 * @Filename：HelloController
 */

@RestController
@RequestMapping("chat")
public class ChatController extends BaseInfoProperties {

    @PostMapping("getMyUnReadCounts")
    public R getMyUnReadCounts(@RequestParam(value = "myId") String myId) {
        Map<Object, Object> map = redis.hgetall(CHAT_MSG_LIST + ":" + myId);
        return R.ok(map);
    }
    @PostMapping("clearMyUnReadCounts")
    public R clearMyUnReadCounts(@RequestParam(value = "myId") String myId, @RequestParam(value = "oppositeId") String oppositeId) {
        redis.setHashValue(CHAT_MSG_LIST + ":" + myId, oppositeId, "0");
        return R.ok();
    }
}
