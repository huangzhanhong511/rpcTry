package com.richard;

import com.richard.protocol.http.httpServer.HttpServer;
import com.richard.register.LocalRegister;
import com.richard.register.MapRemoteRegister;

import java.net.MalformedURLException;
import java.net.URL;

public class Provider {
    public static void main(String[] args) throws MalformedURLException {

        //本地注册
        LocalRegister.register(HelloService.class.getName(), HelloServiceImpl.class);

        //注册中心注册
//        URL url = new URL("http://localhost:8080/");
//        MapRemoteRegister.regist(HelloService.class.getName(),url);


        //接收网络请求，Http，Socket,TCP, UDP（更好的是提供多种选择让用户去选择）
        HttpServer server = new HttpServer();
        server.start("localhost", 8080);
    }
}
