package com.healerjean.proj.a_test.serializeable.base;

/**
 * Serializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public interface Serializer {

    /**
     * 对对象进行编码
     *
     * @param obj 要编码的对象
     * @return 编码后的字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 将字节数组解码为指定类型的对象
     *
     * @param bytes 要解码的字节数组
     * @param clazz 要解码成的对象的类型
     * @return 解码后得到的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    /**
     * 获取序列化协议类型
     *
     * @return 序列化协议类型
     */
    SerializationType protocolType();

    /**
     * 序列化器实例销毁时做些什么
     */
    default void unregister() {

    }

}



