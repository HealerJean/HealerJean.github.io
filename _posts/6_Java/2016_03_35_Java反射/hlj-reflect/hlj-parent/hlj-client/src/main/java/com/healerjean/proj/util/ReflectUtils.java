package com.healerjean.proj.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

/**
 * ReflectUtils
 *
 * @author zhangyujin
 * @date 2023/7/2$  19:03$
 */
@Slf4j
public final class ReflectUtils {

    /**
     * SETTER_PREFIX
     */
    private static final String SETTER_PREFIX = "set";
    /**
     * GETTER_PREFIX
     */
    private static final String GETTER_PREFIX = "get";
    /**
     * CGLIB_CLASS_SEPARATOR
     */
    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, ".")){
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
        }
        return object;
    }

    /**
     * 反射获取Class字段列表
     *
     * @param clazz Class<?>
     * @return Class字段列表
     */
    public static List<String> getFieldNameList(Class<?> clazz) {
        List<String> fieldNameList = new ArrayList<>();
        getFieldList(clazz).forEach(f -> fieldNameList.add(f.getName()));
        return fieldNameList;
    }

    /**
     * 获取class所有Field对象
     *
     * @param clazz Class
     * @return Field列表
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (null != clazz) {
            Field[] fieldArr = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fieldArr));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}