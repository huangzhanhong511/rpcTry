package com.richard.proxy;

import com.richard.common.Invocation;
import com.richard.protocol.http.httpClient.HttpClient;
import com.richard.register.zookeeper.ZookeeperServiceDiscovery;
import com.richard.register.zookeeper.ZookeeperServiceRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(interfaceClass.getName(),method.getName(),method.getParameterTypes(),args);

                HttpClient httpClient = new HttpClient();

                ZookeeperServiceRegistry registry = new ZookeeperServiceRegistry();
                ZookeeperServiceDiscovery discovery = new ZookeeperServiceDiscovery(registry.getClient());
                String serviceAddress = discovery.discoveryService("TestZookeeper");
                System.out.println("This is serviceAddress: " + serviceAddress);

                String[] addressParts = serviceAddress.split(":"); // 分割字符串
                String host = addressParts[0]; // 主机名，例如 "localhost"
                int port = Integer.parseInt(addressParts[1]); // 端口号，例如 8080

                // 打印解析后的主机名和端口号，方便调试
                System.out.println("Parsed host: " + host);
                System.out.println("Parsed port: " + port);

                // 调用 httpClient.send 方法
                return httpClient.send(host, port, invocation);
                //return httpClient.send("localhost",8080,invocation);
            }
        });
        return (T) proxyInstance;
    }
}
