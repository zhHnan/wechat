package com.hnz.config;

import com.hnz.netty.NettyServerNode;
import com.hnz.utils.JsonUtils;
import com.hnz.utils.RedisOperator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static final String PATH = "/server-list";
    @Autowired
    private RedisOperator redis;
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
//        注册监听事件
        addWatcher(curatorClient, PATH);
        return curatorClient;
    }

    public void addWatcher(CuratorFramework client, String path){
        CuratorCache builder = CuratorCache.build(client, path);
        builder.listenable().addListener((type, oldData, data) ->{
//            type:当前监听的事件类型
            switch (type.name()){
                case "NODE_CREATED":
                    log.info("子节点创建");
                    break;
                case "NODE_CHANGED":
                    log.info("节点内容变更");
                    break;
                case "NODE_DELETED":
                    log.info("节点被删除");
                    NettyServerNode oldNode = JsonUtils.jsonToPojo(new String(data.getData()), NettyServerNode.class);
                    System.out.println("节点被删除："+ oldData.getPath());
                    String oldPort = oldNode.getPort() + "";
                    String portKey = "netty_port";
                    redis.hdel(portKey, oldPort);
                    break;
                default:
                    log.info("未知事件");
                    break;
            }
        });
        builder.start();
    }
}
