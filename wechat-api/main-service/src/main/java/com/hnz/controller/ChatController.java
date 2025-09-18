package com.hnz.controller;

import com.hnz.base.BaseInfoProperties;
import com.hnz.result.R;
import com.hnz.service.ChatMessageService;
import jakarta.annotation.Resource;
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
    @Resource
    private ChatMessageService chatMessageService;

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
    @PostMapping("list/{sendId}/{receiveId}")
    public R list(@PathVariable(value = "sendId") String sendId, @PathVariable(value = "receiveId") String receiveId, Integer  page, Integer pageSize) {
       if (page == null) page = 1;
       if (pageSize == null)  pageSize = 10;
       return R.ok(chatMessageService.queryMsgList(sendId, receiveId, page, pageSize));
    }
    @PostMapping("signRead/{msgId}")
    public R signRead(@PathVariable(value = "msgId") String msgId) {
        chatMessageService.updateMsgSignRead(msgId);
        return R.ok();
    }
}
