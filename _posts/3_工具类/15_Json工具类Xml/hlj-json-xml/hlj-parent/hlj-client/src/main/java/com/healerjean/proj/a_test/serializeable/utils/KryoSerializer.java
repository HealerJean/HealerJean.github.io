package com.healerjean.proj.a_test.serializeable.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.healerjean.proj.a_test.serializeable.base.SerializationType;
import com.healerjean.proj.a_test.serializeable.base.SerializeException;
import com.healerjean.proj.a_test.serializeable.base.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * KryoSerializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public class KryoSerializer implements Serializer {

    /**
     * INSTANCE
     */
    private static volatile KryoSerializer INSTANCE;

    private final KryoPool pool;

    /**
     * 构造函数
     */
    private KryoSerializer() {
        this.pool = new KryoPool.Builder(() -> {
            Kryo kryo = new Kryo();
            // Kryo 配置
            kryo.setReferences(false);
            kryo.setRegistrationRequired(false);
            return kryo;
        }).build();
    }

    /**
     * 获取单例实例
     *
     * @return 返回实例
     */
    public static KryoSerializer getInstance() {
        if (INSTANCE == null) {
            synchronized (KryoSerializer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new KryoSerializer();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public byte[] serialize(Object obj) {
        Kryo kryo = pool.borrow();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Output output = new Output(bos);
            kryo.writeObject(output, obj);
            output.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("kryo序列化出现异常", e);
        } finally {
            pool.release(kryo);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = pool.borrow();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return kryo.readObject(input, clazz);
        } finally {
            pool.release(kryo);
        }
    }

    @Override
    public SerializationType protocolType() {
        return SerializationType.KRYO;
    }

}

