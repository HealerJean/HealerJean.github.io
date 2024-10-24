package com.healerjean.proj.a_test.serializeable.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.healerjean.proj.a_test.serializeable.base.SerializationType;
import com.healerjean.proj.a_test.serializeable.base.SerializeException;
import com.healerjean.proj.a_test.serializeable.base.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * HessianSerializer
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
public class HessianSerializer implements Serializer {

    /**
     * INSTANCE
     */
    private static volatile Serializer INSTANCE;

    /**
     * 构造方法
     */
    private HessianSerializer() {
    }

    /**
     * 获取序列化器实例
     *
     * @return 返回序列化器实例
     */
    public static Serializer getInstance() {
        if (INSTANCE == null) {
            synchronized (HessianSerializer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HessianSerializer();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            Hessian2Input input = new Hessian2Input(is);
            return (T) input.readObject();
        } catch (Exception e) {
            throw new SerializeException("hessian反序列化出现异常", e);
        }
    }

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Hessian2Output output = new Hessian2Output(os);
            output.writeObject(obj);
            output.flush();
            return os.toByteArray();
        } catch (Exception e) {
            throw new SerializeException("hessian序列化出现异常", e);
        }
    }

    @Override
    public SerializationType protocolType() {
        return SerializationType.HESSIAN;
    }

}

