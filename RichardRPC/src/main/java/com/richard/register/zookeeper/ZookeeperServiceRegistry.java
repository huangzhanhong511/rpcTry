package com.richard.register.zookeeper;

import lombok.Getter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

@Getter
public class ZookeeperServiceRegistry {

    private static final String ZK_ADDRESS = "localhost:2181";
    private static final String REGISTRY_PATH = "/rpc_services";

    private CuratorFramework client;
    public ZookeeperServiceRegistry() {
        client = CuratorFrameworkFactory.builder()
                .connectString(ZK_ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
    }

    public void registerService(String serviceName, String serviceAddress) throws Exception {
        // 构造服务注册路径，例如 /registryPath/serviceName/instance-UUID
        String path = REGISTRY_PATH + "/" + serviceName + "/" + serviceAddress;

        try {
            // 创建临时节点 (Ephemeral) 存储服务实例信息
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL) // 临时节点
                    .forPath(path);

            System.out.println("Service registered successfully: " + path);
        } catch (Exception e) {
            System.err.println("Service registration failed: " + e.getMessage());
            throw e; // 可以选择记录日志或重试
        }
    }

    public void unregisterService(String serviceName) throws Exception {
        String path = REGISTRY_PATH + "/" + serviceName;
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    public void close() throws Exception {
        client.close();
    }

}
