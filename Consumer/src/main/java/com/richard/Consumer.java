package com.richard;

import com.richard.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        Long start = System.currentTimeMillis();
        String result = helloService.sayHello("Richard");
        Long end = System.currentTimeMillis();
        System.out.println("This is RPC Request Time Cost: " + (end - start));
        System.out.println(result);

    }
}
