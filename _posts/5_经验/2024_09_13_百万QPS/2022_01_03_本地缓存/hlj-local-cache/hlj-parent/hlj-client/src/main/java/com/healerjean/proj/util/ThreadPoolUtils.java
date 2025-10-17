package com.healerjean.proj.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具
 *
 * @author zhangyujin
 * @date 2023-07-05 09:07:42
 */
@Slf4j
@Component
public class ThreadPoolUtils {

    /**
     * 默认线程池工具 - DEFAULT_THREAD_POOL_TASK_EXECUTOR
     */
    public static ThreadPoolTaskExecutor DEFAULT_THREAD_POOL_TASK_EXECUTOR;

    /**
     * 线程池工具 - DEFAULT_THREAD_POOL_EXECUTOR
     */
    public static ThreadPoolExecutor DEFAULT_THREAD_POOL_EXECUTOR;


    static {
        DEFAULT_THREAD_POOL_TASK_EXECUTOR = buildDefaultThreadPoolTaskExecutor();
        DEFAULT_THREAD_POOL_EXECUTOR = buildDefaultThreadPoolExecutor();
    }

    /**
     * 构建默认线程池-ThreadPoolExecutor
     *
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor buildDefaultThreadPoolExecutor() {
        int corePoolSize = 10;
        int maximumPoolSize = 100;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        AtomicInteger tag = new AtomicInteger(1);
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(500);
        ThreadFactory threadFactory = r -> new Thread(r, "defaultThreadPoolExecutor" + tag.incrementAndGet());
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 构建默认线程池-ThreadPoolTaskExecutor
     *
     * @return ThreadPoolTaskExecutor
     */
    private static ThreadPoolTaskExecutor buildDefaultThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        taskExecutor.setCorePoolSize(10);
        // 最大线程数：只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(100);
        // 线程池维护线程所允许的空闲时间：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(60);
        // 缓存队列：用来缓冲执行任务的队列
        taskExecutor.setQueueCapacity(500);
        // 线程工厂
        AtomicInteger tag = new AtomicInteger(1);
        taskExecutor.setThreadFactory(r -> new Thread(r, "defaultThreadPoolTaskExecutor" + tag.incrementAndGet()));
        // 拒绝策略由调用线程处理该任务
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // defaultThreadPoolTaskExecutor
        taskExecutor.setThreadNamePrefix("defaultThreadPoolTaskExecutor");

        // 调度器shutdown被调用时等待当前被调度的任务完成：用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间:如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.initialize();
        log.info("Executor - threadPoolTaskExecutor injected!");
        return taskExecutor;
    }


}
