package com.healerjean.proj.utils;

import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * RetryUtils
 *
 * @author zhangyujin
 * @date 2025/2/26
 */
@Slf4j
public class RetryUtils {

    /**
     * 带有重试机制的函数调用方法
     *
     * @param supplier 要执行的函数，使用 Supplier 函数式接口，可返回任意类型的值
     * @param expected 预期的返回值
     * @param <T>      返回值的泛型类型
     * @return 最后一次调用函数的结果
     */
    public static <T> T callWithRetry(Supplier<T> supplier, T expected, int retryTime, int timeOut) {
        long startTime = System.currentTimeMillis();
        T result = null;
        int retryCount = 0;
        while (true) {
            retryCount++;
            CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(supplier, ThreadPoolUtils.DEFAULT_THREAD_POOL_EXECUTOR);
            try {
                result = completableFuture.get();
            } catch (Exception e) {
                log.error("callWithRetry error", e);
            }

            // 超时结束
            boolean isTimedOut = System.currentTimeMillis() - startTime >= timeOut * 1000L;
            if (isTimedOut) {
                break;
            }

            // 预期结束
            if (expected.equals(result)) {
                break;
            }

            // 重试
            Uninterruptibles.sleepUninterruptibly(retryTime, TimeUnit.SECONDS);
        }

        int costTime = (int) (System.currentTimeMillis() - startTime);
        log.info("callWithRetry costTime={}ms, retryCount={}, result={}", costTime, retryCount, result);
        return result;
    }






}
