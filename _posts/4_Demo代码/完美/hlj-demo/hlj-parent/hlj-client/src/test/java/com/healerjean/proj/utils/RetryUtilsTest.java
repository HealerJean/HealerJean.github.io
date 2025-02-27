package com.healerjean.proj.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * RetryUtilsTest
 *
 * @author zhangyujin
 * @date 2025/2/26
 */
class RetryUtilsTest {

    @Test
    void callWithRetry() {
        // 示例函数，返回一个 0 - 9 的随机整数
        Supplier<Integer> sampleFunction = () -> {
            int randomValue = ThreadLocalRandom.current().nextInt(10);
            System.out.println("Function returned: " + randomValue);
            return randomValue;
        };
        // 预期结果为 5
        Integer expected = 5;
        // 调用带有重试机制的函数
        Integer finalResult = RetryUtils.callWithRetry(sampleFunction, expected, 1, 10);
        System.out.println("Final result: " + finalResult);
    }
}