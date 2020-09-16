package com.healerjean.proj.a_test.json.jackson.d03_选择性过滤;

import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author HealerJean
 * @date 2020/9/16  14:47.
 * @description
 */
public class ReflectionFieldNameUtils {

    public static <T> String getFieldName(PropertyFunction<T, ?> propertyFunction) {
        try {
            // 通过获取对象方法，判断是否存在该方法
            Method method = propertyFunction.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            // 利用jdk的SerializedLambda 解析方法引用
            java.lang.invoke.SerializedLambda serializedLambda = (SerializedLambda) method.invoke(propertyFunction);
            String getter = serializedLambda.getImplMethodName();
            return resolveFieldName(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith("is")) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        return firstToLowerCase(getMethodName);
    }

    private static String firstToLowerCase(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);

    }

}
