package com.healerjean.proj.a_test.serializeable.test;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SerializableBean
 *
 * @author zhangyujin
 * @date 2024/10/22
 */
@Data
public final class SerializableBean implements Serializable {

    private static final long serialVersionUID = 12345L;

    private static String staticField;

    private String strVal;

    private Integer intVal;

    private Long longVal;

    private BigDecimal bigDecimal;

    private Date date;

    private Class<?> genericClazz;

    private Class<?> interfaceClazz;

    public static void setStaticField(String staticField) {
        SerializableBean.staticField = staticField;
    }

    public static String getStaticField() {
        return staticField;
    }

    @Override
    public String toString() {
        return "SerializableBean{" +
                "staticField='" + staticField + '\'' +
                ", strVal='" + strVal + '\'' +
                ", intVal=" + intVal +
                ", longVal=" + longVal +
                ", bigDecimal=" + bigDecimal +
                ", date=" + date +
                ", genericClazz=" + genericClazz +
                ", interfaceClazz=" + interfaceClazz +
                '}';
    }
}
