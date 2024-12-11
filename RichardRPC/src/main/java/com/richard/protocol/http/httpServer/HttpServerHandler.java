package com.richard.protocol.http.httpServer;

import com.richard.common.Invocation;
import com.richard.compress.gzip.GzipCompress;
import com.richard.register.LocalRegister;
import com.richard.serialization.kyro.KyroSerializer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {

    KyroSerializer kyroSerializer = new KyroSerializer();
    GzipCompress gzipCompress = new GzipCompress();

    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        //处理请求-->接口,方法,方法参数
        try {
//            byte[] data = req.getInputStream().readAllBytes();
//            Invocation invocation = requestDeserializeAndDecompress(data, Invocation.class);

            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();

            String interfaceName = invocation.getInterfaceName();
            Class classImpl = LocalRegister.get(interfaceName);
            Method method = classImpl.getMethod(invocation.getMethodName(),invocation.getParameterTypes());
            String result = (String) method.invoke(classImpl.getDeclaredConstructor().newInstance(),invocation.getParameters());

//            byte[] responseData = requestSerializeAndCompress(result);
//
//            // 设置响应头和返回序列化后的数据
//            resp.setContentType("application/octet-stream");
//            resp.getOutputStream().write(responseData);
            IOUtils.write(result,resp.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] requestSerializeAndCompress(Object object) {
        byte[] serializedBytes = kyroSerializer.serialize(object);
        return gzipCompress.compress(serializedBytes);
    }

    public <T> T requestDeserializeAndDecompress(byte[] serializedBytes, Class<T> interfaceClass) {
        byte[] deCompressedBytes = gzipCompress.decompress(serializedBytes);
        return kyroSerializer.deserialize(deCompressedBytes,interfaceClass);
    }
}