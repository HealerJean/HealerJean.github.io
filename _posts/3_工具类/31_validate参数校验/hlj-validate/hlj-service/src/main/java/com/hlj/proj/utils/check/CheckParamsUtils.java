package com.hlj.proj.utils.check;

import com.hlj.proj.utils.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CheckParamsUtils
 *
 * @author zhangyujin
 * @date 2024-09-30 11:09:13
 */
@Slf4j
public class CheckParamsUtils {


    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(CheckParamsUtils.class);

    /**
     * ALL_PARAMS
     */
    public static final String ALL_PARAMS = "AllParams";

    /**
     * ANY_PARAMS
     */
    public static final String ANY_PARAMS = "AnyParams";

    /**
     * 校验参数类
     *
     * @param type 校验类型
     * @param obj  传入的参数类
     * @param str  需要校验的参数列表
     */
    public static boolean checkParams(String type, Object obj, String... str) {
        try {
            if (ALL_PARAMS.equals(type)) {
                return checkParamsAll(obj, str);
            }

            if (ANY_PARAMS.equals(type)) {
                return checkParamsAny(obj, str);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        logger.info("<验参工具类> 无此验参类型！");
        return false;
    }


    /**
     * 校验参数类
     *
     * @param type 校验类型
     * @param obj  传入的参数类
     * @param fn   需要校验的参数列表
     */
    @SafeVarargs
    public static <T, R> boolean checkParams(String type, Object obj, LambdaUtils.LambdaFunction<T, R>... fn) {
        try {

            String[] strs = Arrays.stream(fn).map(LambdaUtils::convertToFieldName).toArray(String[]::new);
            if (ALL_PARAMS.equals(type)) {
                return checkParamsAll(obj, strs);
            }

            if (ANY_PARAMS.equals(type)) {
                return checkParamsAny(obj, strs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        logger.info("<验参工具类> 无此验参类型！");
        return false;
    }

    /**
     * 校验任意传入参数不为空即可
     *
     * @param obj  obj
     * @param strs strs
     * @return
     */
    private static boolean checkParamsAny(Object obj, String[] strs) {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();

        Set<String> strSets = Arrays.stream(strs).collect(Collectors.toSet());
        List<Field> filterFields = Arrays.stream(fields)
                .filter(field -> strSets.contains(field.getName()))
                .collect(Collectors.toList());

        for (Field field : filterFields) {
            if (checkFieldValue(obj, field)) {
                return true;
            }
        }
        return true;
    }


    /**
     * 校验所有传入参数
     *
     * @param obj  被检查的对象
     * @param strs 需要检查的字段名数组
     * @return 如果所有指定字段都有非空值，则返回 true；否则返回 false
     */
    private static boolean checkParamsAll(Object obj, String... strs) {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();

        Set<String> strSets = Arrays.stream(strs).collect(Collectors.toSet());
        List<Field> filterFields = Arrays.stream(fields)
                .filter(field -> strSets.contains(field.getName()))
                .collect(Collectors.toList());

        for (Field field : filterFields) {
            if (!checkFieldValue(obj, field)) {
                return false;
            }
        }
        return true;
    }

    /**
     * checkFieldValue
     *
     * @param obj   obj
     * @param field field
     * @return {@link boolean}
     */
    private static boolean checkFieldValue(Object obj, Field field) {
        try {
            String methodName = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
            Method getter = obj.getClass().getMethod(methodName);
            Object value = getter.invoke(obj);
            return value != null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}

