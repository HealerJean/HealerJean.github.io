package com.healerjean.proj.d05_线程池;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * TestMain
 *
 * @author zhangyujin
 * @date 2023/7/5$  09:52$
 */
public class TestMain {

    @Test
    public void testThreadPoolExecutor() {
        ThreadPoolExecutor defaultThreadPoolExecutor = ThreadPoolUtils.DEFAULT_THREAD_POOL_EXECUTOR;
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            defaultThreadPoolExecutor.submit(() -> System.out.println(finalI +  " " + Thread.currentThread().getName()));
        }
    }

    @Test
    public void testThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor defaultThreadPoolTaskExecutor = ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR;
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            defaultThreadPoolTaskExecutor.submit(() -> System.out.println(finalI + " " + Thread.currentThread().getName()));
        }
    }

}
