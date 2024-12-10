package com.richard.protocol;

import com.richard.common.Invocation;
import com.richard.register.LocalRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {

    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        //处理请求-->接口,方法,方法参数
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();

            String interfaceName = invocation.getInterfaceName();
            Class classImpl = LocalRegister.get(interfaceName);
            Method method = classImpl.getMethod(invocation.getMethodName(),invocation.getParameterTypes());
            String result = (String) method.invoke(classImpl.getDeclaredConstructor().newInstance(),invocation.getParameters());

            IOUtils.write(result,resp.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }

    }
}
