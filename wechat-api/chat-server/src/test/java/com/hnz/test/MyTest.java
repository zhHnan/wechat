package com.hnz.test;

import com.hnz.utils.JedisPoolUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：MyTest
 * @Date：2025/9/19 16:39
 * @Filename：MyTest
 */
public class MyTest {

    @Test
    public void testJedisPool() {
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.set("hello", "world");
        System.out.println(jedis.get("hello"));
    }
    @Test
    public void testGetNettyPort() {
        Integer res = selectPort(nettyDefaultPort);
        System.out.println(res);
    }

    public static final Integer nettyDefaultPort = 875;
    public static final String initOnlineCounts = "0";
    public static Integer selectPort(Integer port){
        String portKey = "netty_port";
        Jedis jedis = JedisPoolUtils.getJedis();
        Map<String, String> portMap = jedis.hgetAll(portKey);

        List<Integer> portList = portMap.keySet().stream().map(Integer::valueOf).toList();
        Integer nettyPort;
        if(portList.isEmpty()){
            jedis.hset(portKey, String.valueOf(port), initOnlineCounts);
            nettyPort = port;
            return nettyPort;
        }
        Optional<Integer> maxPort = portList.stream().max(Integer::compareTo);
        int curPort = maxPort.get() + 10;
        jedis.hset(portKey, String.valueOf(curPort), initOnlineCounts);
        nettyPort = curPort;
        return nettyPort;
    }
}
