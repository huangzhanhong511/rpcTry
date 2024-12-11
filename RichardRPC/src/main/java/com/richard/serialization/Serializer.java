package com.richard.serialization;

/**
*序列化接口，所有的序列化都要实现这个接口
 */
public interface Serializer {

    /**
     * @param obj 你需要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * @param bytes 传入的需要反序列化的字节数组
     * @param clazz 需要反序列化成的目标类
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
