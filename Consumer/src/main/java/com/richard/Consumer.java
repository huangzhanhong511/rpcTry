package com.richard;

import com.richard.common.Invocation;
import com.richard.protocol.HttpClient;
import com.richard.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("Richard");
        System.out.println(result);

    }
}
