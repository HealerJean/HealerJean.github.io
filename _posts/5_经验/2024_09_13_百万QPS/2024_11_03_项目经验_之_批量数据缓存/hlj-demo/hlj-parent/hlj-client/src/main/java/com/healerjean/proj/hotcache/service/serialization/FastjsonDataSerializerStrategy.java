package com.healerjean.proj.hotcache.service.serialization;

/**
 * FastjsonSerializer
 *
 * @author zhangyujin
 * @date 2025/11/3
 */

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.hotcache.enums.DataSerializerStrategyEnum;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FastjsonDataSerializerStrategy<T> implements DataSerializerStrategy<T> {
    private final Class<T> clazz;

    @Override
    public DataSerializerStrategyEnum getDataSerializerStrategyEnum() {
        return DataSerializerStrategyEnum.FAST_JSON;
    }

    public FastjsonDataSerializerStrategy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void write(T record, OutputStream out) throws IOException {
        byte[] bytes = serialize(record);
        out.write(bytes);
        out.write('\n');
    }

    @Override
    public T read(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1 && b != '\n') {
            buffer.write(b);
        }
        if (buffer.size() == 0) return null;
        return JSON.parseObject(buffer.toByteArray(), clazz);
    }

    @Override
    public byte[] serialize(T record) throws IOException {
        return JSON.toJSONBytes(record);
    }

    @Override
    public T deserialize(byte[] data) throws IOException {
        return JSON.parseObject(data, clazz);
    }
}