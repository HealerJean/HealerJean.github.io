package com.healerjean.proj.a_test.serializeable.utils;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.a_test.serializeable.base.SerializationType;
import com.healerjean.proj.a_test.serializeable.base.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * JsonSerializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public class JsonSerializer implements Serializer {

    /**
     * INSTANCE
     */
    private static volatile Serializer INSTANCE;

    /**
     * 构造函数
     */
    private JsonSerializer() {
    }

    /**
     * 获取单例实例
     *
     * @return 返回单例实例
     */
    public static Serializer getInstance() {
        if (INSTANCE == null) {
            synchronized (JsonSerializer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JsonSerializer();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(json, clazz);
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public SerializationType protocolType() {
        return SerializationType.JSON;
    }

}
