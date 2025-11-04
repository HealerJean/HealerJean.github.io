package com.healerjean.proj.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * 异常类工具
 *
 * @author zhangyujin
 * @date 2025/4/8
 */
@Slf4j
public class EUtils {

    /**
     * 校验数据，则抛出自定义异常（检查布尔表达式是否为 true，若为 false 则抛出自定义异常）
     *
     * @param expression 要检查的布尔表达式
     * @param e          异常信息
     */
    public static void checkCondition(boolean expression, RuntimeException e) {
        if (expression) {
            return;
        }
        throw e;
    }


    /**
     * toUncheckedException
     */
    public static RuntimeException toUncheckedException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    @FunctionalInterface
    interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    static <T, R> Function<T, R> wrap(ThrowingFunction<T, R, Exception> throwingFunction) {
        return t -> {
            try {
                return throwingFunction.apply(t);
            } catch (Exception e) {
                log.warn("req:{} fail ", t, e);
                return null;
            }
        };
    }


    /**
     * 获取异常消息
     *
     * @param length，小于等于 0 时，不进行长度限制
     */
    public static String getExceptionMessage(Exception e, Integer length) {
        String msg;
        if (e instanceof NullPointerException) {
            msg = "java.lang.NullPointerException";
        } else {
            msg = e.getMessage();
        }
        if (length == null || length <= 0) {
            return msg;
        }
        if (msg.length() > length) {
            msg = msg.substring(0, length);
        }
        return msg;
    }


}
