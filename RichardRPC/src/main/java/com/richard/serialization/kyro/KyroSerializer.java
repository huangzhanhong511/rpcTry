package com.richard.serialization.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.richard.common.Invocation;
import com.richard.serialization.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

@Slf4j
public class KyroSerializer implements Serializer {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(Invocation.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = kryoThreadLocal.get();
        Input input = new Input(bytes);
        T object = kryo.readObject(input, clazz);
        input.close();
        return object;
    }
}
