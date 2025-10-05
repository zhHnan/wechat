package com.hnz.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：NettyServerNode
 * @Date：2025/10/5 17:17
 * @Filename：NettyServerNode
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NettyServerNode {

    private String ip;
    private Integer port;
    private Integer onlineCounts = 0;
}
