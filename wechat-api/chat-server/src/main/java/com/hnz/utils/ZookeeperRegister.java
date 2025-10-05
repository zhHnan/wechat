package com.hnz.utils;

import com.hnz.netty.NettyServerNode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：ZookeeperRegister
 * @Date：2025/10/5 17:00
 * @Filename：ZookeeperRegister
 */
public class ZookeeperRegister {

    public static void registerNettyServer(String nodeName, String ip, Integer port) throws Exception {
        CuratorFramework client = CuratorConfig.getClient();
        String path = "/" + nodeName;
        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
        }else {
            System.out.println("节点已存在:" + stat);
        }
//        创建对应的临时节点，值可以存放在线人数
        NettyServerNode nettyServerNode = new NettyServerNode();
        nettyServerNode.setIp(ip);
        nettyServerNode.setPort(port);
        String value = JsonUtils.objectToJson(nettyServerNode);
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path + "/im-", value.getBytes());
    }

    public static String getLocalIp() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("本机ip:" + hostAddress);
        return hostAddress;
    }
    public static void incrementOnlineCounts(NettyServerNode nettyServerNode)throws Exception{
        dealOnlineCounts(nettyServerNode, 1);
    }
    public static void decrementOnlineCounts(NettyServerNode nettyServerNode) throws Exception{
        dealOnlineCounts(nettyServerNode, -1);
    }
//    处理在线人数的增减
    public static void dealOnlineCounts(NettyServerNode nettyServerNode, Integer counts)throws Exception{
        CuratorFramework zkClient = CuratorConfig.getClient();
//        分布式锁
        InterProcessReadWriteLock rwLock = new InterProcessReadWriteLock(zkClient, "/rw-lock");
        rwLock.writeLock().acquire();
        try {
            String path = "/server-list";
            List<String> nodes = zkClient.getChildren().forPath(path);
            for (String node : nodes) {
                String value = new String(zkClient.getData().forPath(path + "/" + node));
                NettyServerNode pendingNode = JsonUtils.jsonToPojo(value, NettyServerNode.class);
                if (pendingNode != null && pendingNode.getIp().equals(nettyServerNode.getIp()) && pendingNode.getPort().equals(nettyServerNode.getPort())) {
                    pendingNode.setOnlineCounts(pendingNode.getOnlineCounts() + counts);
                    zkClient.setData().forPath(path + "/" + node, Objects.requireNonNull(JsonUtils.objectToJson(pendingNode)).getBytes());
                }
            }
        } finally {
            rwLock.writeLock().release();
        }

    }
}
