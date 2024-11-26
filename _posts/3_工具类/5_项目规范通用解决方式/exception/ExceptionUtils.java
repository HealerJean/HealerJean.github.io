package com.healerjean.proj.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ExceptionUtils
 *
 * @author zhangyujin
 * @date 2024/11/19
 */
public final class ExceptionUtils<T, R> {

    /**
     * ignoreUnLog
     */
    public static <T, R> R ignoreErrorUnLog(Function<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception ignore) {

        }
        return null;
    }

    /**
     * ignoreLog
     */
    public static <T, R> R ignoreErrorLog(Function<T, R> function, T t, Logger log, String msg) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            log.error("error, {}", msg, e);
        }
        return null;
    }

    /**
     * throwRuntimeException
     */
    public static <T, R> R throwRuntimeException(Function<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * ignoreUnLog
     */
    public static <T> void ignoreErrorUnLog(Consumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Exception ignore) {

        }
    }


    /**
     * ignoreLog
     */
    public static <T> void ignoreErrorLog(Consumer<T> consumer, T t, Logger log, String msg) {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            log.error("error, {}", msg, e);

        }
    }


    /**
     * ignoreLog
     */
    public static <T> void throwRuntimeException(Consumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void test(){
        Consumer<String> objectConsumer = System.out::println;
        ExceptionUtils.ignoreErrorUnLog(objectConsumer, "ok");
    }
}
