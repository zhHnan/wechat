package com.hnz.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：CuratorConfig
 * @Date：2025/10/5 16:48
 * @Filename：CuratorConfig
 */

@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "zookeeper.curator")
public class CuratorConfig {
    private String host;
    private String namespace;
    private Integer connectionTimeoutMs;
//    会话超时时间
    private Integer sessionTimeoutMs;
//    每次重试之间的等待时间
    private Integer sleepMsBetweenRetries;
//    最大重试次数
    private Integer maxRetries;

    @Bean("curatorClient")
    public CuratorFramework curatorClient(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(sleepMsBetweenRetries, maxRetries);
        CuratorFramework curatorClient = CuratorFrameworkFactory.builder()
                .connectString(host)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy)
                .namespace(namespace)
                .build();
//        启动客户端
        curatorClient.start();
//        try {
//            curatorClient.create().creatingParentsIfNeeded().forPath("/springboot/test", "test".getBytes());
//        } catch (Exception e) {
//            log.error("创建节点失败",  e);
//        }
        return curatorClient;
    }
}
