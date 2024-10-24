package com.healerjean.proj.a_test.serializeable.utils;

import com.healerjean.proj.a_test.serializeable.base.SerializationType;
import com.healerjean.proj.a_test.serializeable.base.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProtostuffSerializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public class ProtostuffSerializer implements Serializer {

    /**
     * INSTANCE
     */
    private static volatile Serializer INSTANCE;

    /**
     * 缓存Schema
     */
    private static Map<Class<?>, Schema<?>> schemaCache;

    /**
     * 构造函数
     */
    private ProtostuffSerializer() {
    }

    /**
     * 获取单例实例
     *
     * @return 返回单例实例
     */
    public static Serializer getInstance() {
        if (INSTANCE == null) {
            synchronized (ProtostuffSerializer.class) {
                if (INSTANCE == null) {
                    schemaCache = new ConcurrentHashMap<>();
                    INSTANCE = new ProtostuffSerializer();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public byte[] serialize(Object obj) {
        // serialize
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtostuffIOUtil.toByteArray(obj, this.registerSchema(obj.getClass()), buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = this.registerSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public SerializationType protocolType() {
        return SerializationType.PROTOSTUFF;
    }

    public Schema registerSchema(Class clazz) {
        Schema schema = schemaCache.get(clazz);
        if (schema == null) {
            // this is lazily created and cached by RuntimeSchema
            // so its safe to call RuntimeSchema.getSchema(Foo.class) over and over
            // The getSchema method is also thread-safe
            schema = RuntimeSchema.getSchema(clazz);
            schemaCache.put(clazz, schema);
        }
        return schema;
    }

    @Override
    public void unregister() {
        schemaCache.clear();
        schemaCache = null;
    }
}
