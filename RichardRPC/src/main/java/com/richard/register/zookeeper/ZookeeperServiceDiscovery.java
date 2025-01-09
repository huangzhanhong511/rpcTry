package com.richard.register.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import java.util.List;
import java.util.Random;

public class ZookeeperServiceDiscovery {

    private static final String REGISTRY_PATH = "/rpc_services";
    private CuratorFramework client;

    public ZookeeperServiceDiscovery(CuratorFramework client) {
        this.client = client;
    }

    public String discoveryService(String serviceName) throws Exception {
        String servicePath = REGISTRY_PATH + "/" + serviceName;

        try {
            // 检查路径是否存在
            if (client.checkExists().forPath(servicePath) == null) {
                throw new RuntimeException("Service path not found: " + servicePath);
            }

            // 获取子节点（服务实例）
            List<String> addressList = client.getChildren().forPath(servicePath);
            if (addressList == null || addressList.isEmpty()) {
                throw new RuntimeException("No available service found for " + serviceName);
            }
            for (String address : addressList) {
                System.out.println(address);
            }

            // 随机负载均衡策略
            String selectedInstance = addressList.get(new Random().nextInt(addressList.size()));
            System.out.println("Selected instance: " + selectedInstance);

//            // 如果需要从节点值中获取更多信息，可以在此处解析
//            byte[] data = client.getData().forPath(servicePath + "/" + selectedInstance);
//            String serviceAddress = new String(data); // 节点值作为服务地址
//
//            System.out.println("Discovered service instance: " + serviceAddress);
            return selectedInstance;

        } catch (Exception e) {
            System.err.println("Error discovering service: " + e.getMessage());
            throw e;
        }
    }
}
