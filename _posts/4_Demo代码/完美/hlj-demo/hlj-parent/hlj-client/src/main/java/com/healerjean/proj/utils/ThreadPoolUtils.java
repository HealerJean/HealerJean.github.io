package com.healerjean.proj.utils;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具
 *
 * @author zhangyujin
 * @date 2023-07-05 09:07:42
 */
@Slf4j
public class ThreadPoolUtils {

    /**
     * 默认线程池工具 - DEFAULT_THREAD_POOL_TASK_EXECUTOR
     */
    public static ThreadPoolTaskExecutor DEFAULT_THREAD_POOL_TASK_EXECUTOR;

    /**
     * 线程池工具 - DEFAULT_THREAD_POOL_EXECUTOR
     */
    public static ThreadPoolExecutor DEFAULT_THREAD_POOL_EXECUTOR;

    /**
     * 定时任务，只有1个线程
     */
    public static ScheduledExecutorService SCHEDULED_THREAD_POOL_EXECUTOR;

    static {
        DEFAULT_THREAD_POOL_TASK_EXECUTOR = buildDefaultThreadPoolTaskExecutor();
        DEFAULT_THREAD_POOL_EXECUTOR = buildDefaultThreadPoolExecutor();
        SCHEDULED_THREAD_POOL_EXECUTOR = buildScheduledThreadPoolExecutor();
    }


    /**
     * buildScheduledThreadPoolExecutor
     *
     * @return {@link ScheduledExecutorService}
     */
    private static ScheduledExecutorService buildScheduledThreadPoolExecutor() {
       return new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("scheduledThreadPoolExecutor-%d").build());
    }

    /**
     * 构建默认线程池-ThreadPoolExecutor
     *
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor buildDefaultThreadPoolExecutor() {
        int corePoolSize = 1000;
        int maximumPoolSize = 1000;
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


    /**
     * 超时
     * @param task
     * @return
     */
    public static T schdule(Callable<T> task){
        final CountDownLatch latch = new CountDownLatch(1);
        final T[] resultHolder = (T[]) new Object[1];

        // 每 3 秒执行一次任务
        ScheduledFuture<?> scheduledFuture = SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(() -> {
            try {
                Future<T> future = Executors.newSingleThreadExecutor().submit(task);
                resultHolder[0] = future.get();
                if (resultHolder[0] != null) {
                    latch.countDown();
                }
            } catch (Exception e) {
                log.error("method error", e);
            }
        }, 0, 3, TimeUnit.SECONDS);

        try {
            // 等待 30 秒超时
            boolean isCompleted = latch.await(30, TimeUnit.SECONDS);
            if (isCompleted) {
                return resultHolder[0];
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 取消任务调度
            scheduledFuture.cancel(true);
            SCHEDULED_THREAD_POOL_EXECUTOR.shutdown();
        }
        return null;
    }

}
