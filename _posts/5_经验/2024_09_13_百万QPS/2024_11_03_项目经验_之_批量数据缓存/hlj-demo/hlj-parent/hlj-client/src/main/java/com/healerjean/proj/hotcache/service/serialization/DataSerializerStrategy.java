package com.healerjean.proj.hotcache.service.serialization;

// com.snapshots.serializer.DataSerializer.java

import com.healerjean.proj.hotcache.enums.DataSerializerStrategyEnum;

import java.io.IOException;

/**
 * 通用序列化接口，定义数据的读写行为。
 * 支持多种格式（Protobuf/JSON），实现解耦与插件化。
 *
 * @param <T> 要序列化的数据类型。
 */
public interface DataSerializerStrategy<T> {

    /**
     * 策略名称
     */
    DataSerializerStrategyEnum getDataSerializerStrategyEnum();


    /**
     * 将单条记录写入输出流（支持流式写入）。
     *
     * @param record 要写入的对象。
     * @param out 输出流。
     * @throws IOException 写入失败时抛出异常。
     */
    void write(T record, java.io.OutputStream out) throws java.io.IOException;

    /**
     * 从输入流中反序列化一条记录。
     *
     * @param in 输入流。
     * @return 反序列化后的对象。
     * @throws IOException 读取失败或格式错误时抛出。
     */
    T read(java.io.InputStream in) throws java.io.IOException;

    /**
     * 将对象序列化为字节数组。
     *
     * @param record 待序列化的对象。
     * @return 字节数组表示。
     * @throws IOException 序列化失败时抛出。
     */
    byte[] serialize(T record) throws java.io.IOException;

    /**
     * 将字节数组反序列化为对象。
     *
     * @param data 字节数据。
     * @return 反序列化结果。
     * @throws IOException 反序列化失败时抛出。
     */
    T deserialize(byte[] data) throws java.io.IOException;
}