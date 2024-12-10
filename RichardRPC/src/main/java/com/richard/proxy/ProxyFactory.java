package com.richard.proxy;

import com.richard.LoadBalance;
import com.richard.common.Invocation;
import com.richard.protocol.HttpClient;
import com.richard.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.List;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(interfaceClass.getName(),method.getName(),method.getParameterTypes(),args);

                HttpClient httpClient = new HttpClient();

                //Service Found
//                List<URL> urls = MapRemoteRegister.get(interfaceClass.getName());
//
//                //Load Balance
//                URL targetUrl = LoadBalance.randomUrl(urls);
//
//                return httpClient.send(targetUrl.getHost(), targetUrl.getPort(), invocation);
                return httpClient.send("localhost",8080,invocation);
            }
        });
        return (T) proxyInstance;
    }
}
