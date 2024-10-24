package com.healerjean.proj.a_test.serializeable.test;

import com.caucho.hessian.io.SerializerFactory;
import com.healerjean.proj.a_test.serializeable.base.Serializer;
import com.healerjean.proj.a_test.serializeable.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SerializerTest
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
@Slf4j
public class SerializerTest {

    private static SerializableBean serializableBean;

    @BeforeClass
    public static void beforeClass() {
        serializableBean = new SerializableBean();
        serializableBean.setStrVal("serializableBean");
        serializableBean.setIntVal(1);
        serializableBean.setLongVal(2L);
        serializableBean.setBigDecimal(new BigDecimal("999"));
        serializableBean.setDate(new Date());
        serializableBean.setInterfaceClazz(Serializer.class);
        serializableBean.setGenericClazz(SerializerFactory.class);
        SerializableBean.setStaticField("staticField");
    }

    @Test
    public void testSerializer() {
        testSerializer(JdkSerializer.getInstance());
        testSerializer(HessianSerializer.getInstance());
        testSerializer(JsonSerializer.getInstance());
        testSerializer(KryoSerializer.getInstance());
        testSerializer(ProtostuffSerializer.getInstance());
        testSerializer(MsgPackSerializer.getInstance());
    }

    /**
     * 测试序列化器的方法
     *
     * @param serializer 序列化器对象
     * @return void
     */
    private void testSerializer(Serializer serializer) {
        System.out.println("################");
        System.out.println();

        final String name = serializer.getClass().getName();
        log.info(String.format("---------------- %s ----------------", name));
        byte[] bytes = serializer.serialize(serializableBean);
        // log.info("code result size: {}, bytes: {}", bytes.length, Arrays.toString(bytes));
        System.out.println("serialize result size: " + bytes.length);
        SerializableBean decode = serializer.deserialize(bytes, SerializableBean.class);
        System.out.println("deserialize result: " + decode);
        serializer.unregister();
        // log.info(String.format("---------------- %s ----------------\n", name));
        System.out.println("################");
    }
}
