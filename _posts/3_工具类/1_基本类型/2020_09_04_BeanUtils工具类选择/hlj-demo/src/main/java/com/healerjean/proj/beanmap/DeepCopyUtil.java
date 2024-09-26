package com.healerjean.proj.beanmap;

import java.io.*;

/**
 * DeepCopyUtil
 *
 * @author zhangyujin
 * @date 2024/9/10
 */
public class DeepCopyUtil {



    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T object) {
        if (object == null) {
            return null;
        }

        // 确保对象实现了Serializable接口
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("The object must implement Serializable");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);
             ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            oos.writeObject(object);
            return (T) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
