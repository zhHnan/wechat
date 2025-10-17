package com.hnz.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：JedisPoolUtils
 * @Date：2025/9/19 15:54
 * @Filename：jedis 连接池配置类
 */
public class JedisPoolUtils {
    private static final JedisPool JEDIS_POOL;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
//        最大连接数
        config.setMaxTotal(10);
//        最大空闲连接数
        config.setMaxIdle(10);
//        最小空闲连接数
        config.setMinIdle(5);
//        设置连接池获取连接的最大等待时间(毫秒)
        config.setMaxWait(Duration.ofMillis(1500));
        JEDIS_POOL = new JedisPool(config, "127.0.0.1", 5379, 1000);
    }

    public static Jedis getJedis() {
        return JEDIS_POOL.getResource();
    }
}
