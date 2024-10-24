package com.healerjean.proj.a_test.serializeable.utils;

import com.healerjean.proj.a_test.serializeable.base.SerializationType;
import com.healerjean.proj.a_test.serializeable.base.SerializeException;
import com.healerjean.proj.a_test.serializeable.base.Serializer;

import java.io.*;

/**
 * JdkSerializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public class JdkSerializer implements Serializer {

    /**
     * INSTANCE
     */
    private static volatile Serializer INSTANCE;

    /**
     * 私有构造函数
     */
    private JdkSerializer() {
    }

    /**
     * 获取序列化器实例
     *
     * @return 返回序列化器实例
     */
    public static Serializer getInstance() {
        if (INSTANCE == null) {
            synchronized (JdkSerializer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JdkSerializer();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("all")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new SerializeException("jdk反序列化出现异常", e);
        }
    }

    @Override
    public SerializationType protocolType() {
        return SerializationType.JDK;
    }

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("jdk序列化出现异常", e);
        }
    }

}
