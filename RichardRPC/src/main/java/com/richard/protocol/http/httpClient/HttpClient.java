package com.richard.protocol.http.httpClient;

import com.richard.common.Invocation;
import com.richard.compress.gzip.GzipCompress;
import com.richard.serialization.kyro.KyroSerializer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpClient {
    KyroSerializer kyroSerializer = new KyroSerializer();
    GzipCompress gzipCompress = new GzipCompress();

    public String send(String hostname, Integer port, Invocation invocation) {
        try {
            URL url = new URL("http", hostname,port,"/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            //配置
            Long sendTime = System.currentTimeMillis();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(invocation);
            oos.flush();
            oos.close();
            //使用kryo和gzip做序列化和反序列化
//            byte[] serializedAndCompressedData = requestSerializeAndCompress(invocation);
//            outputStream.write(serializedAndCompressedData);

            InputStream inputStream = httpURLConnection.getInputStream();
            Long receiveTime = System.currentTimeMillis();
            System.out.println("This is netWork time:"+(receiveTime - sendTime));
//            byte[] responseBytes = IOUtils.toByteArray(inputStream);
//            String result = requestDeserializeAndDecompress(responseBytes, String.class);
            String result = IOUtils.toString(inputStream);
            return result;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] requestSerializeAndCompress(Invocation invocation) {
        byte[] serializedBytes = kyroSerializer.serialize(invocation);
        return gzipCompress.compress(serializedBytes);
    }

    public <T> T requestDeserializeAndDecompress(byte[] serializedBytes, Class<T> interfaceClass) {
        byte[] deCompressedBytes = gzipCompress.decompress(serializedBytes);
        return kyroSerializer.deserialize(deCompressedBytes,interfaceClass);
    }
}
