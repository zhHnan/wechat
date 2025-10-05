package com.hnz.utils;

import lombok.Getter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：CuratorConfig
 * @Date：2025/10/5 16:48
 * @Filename：CuratorConfig
 */
public class CuratorConfig {

    private static final String HOST = "10.85.49.237:2181";
    private static final String NAMESPACE = "hnz-im";
    private static final Integer CONNECTION_TIMEOUT_MS = 30 * 1000;
//    会话超时时间
    private static final Integer SESSION_TIMEOUT_MS = 3 * 1000;
//    每次重试之间的等待时间
    private static final Integer SLEEP_MS_BETWEEN_RETRIES = 2 * 1000;
//    最大重试次数
    private static final Integer MAX_RETRIES = 3;

    @Getter
    private static CuratorFramework client;
    static {
//        定义重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(SLEEP_MS_BETWEEN_RETRIES, MAX_RETRIES);
        client = CuratorFrameworkFactory.builder()
                .connectString(HOST)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                .retryPolicy(retryPolicy)
                .namespace(NAMESPACE)
                .build();
//        启动客户端
        client.start();
    }
}
