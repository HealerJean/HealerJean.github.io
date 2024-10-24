package com.healerjean.proj.a_test.serializeable.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healerjean.proj.a_test.serializeable.base.SerializationType;
import com.healerjean.proj.a_test.serializeable.base.SerializeException;
import com.healerjean.proj.a_test.serializeable.base.Serializer;
import org.msgpack.jackson.dataformat.MessagePackMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * MsgPackSerializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public class MsgPackSerializer implements Serializer {

    private static volatile MsgPackSerializer INSTANCE;

    private static ObjectMapper objectMapper;

    private MsgPackSerializer() {
    }

    /**
     * 获取序列化器实例
     *
     * @return 返回序列化器实例
     */
    public static Serializer getInstance() {
        if (INSTANCE == null) {
            synchronized (MsgPackSerializer.class) {
                if (INSTANCE == null) {
                    objectMapper = new MessagePackMapper();
                    INSTANCE = new MsgPackSerializer();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            objectMapper.writeValue(baos, obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("msgpack序列化异常", e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new SerializeException("反序列化异常", e);
        }
    }

    @Override
    public SerializationType protocolType() {
        return SerializationType.MSGPACK;
    }

}

