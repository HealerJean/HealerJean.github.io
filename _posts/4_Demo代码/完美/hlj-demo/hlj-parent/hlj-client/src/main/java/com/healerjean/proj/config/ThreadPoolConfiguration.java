package com.healerjean.proj.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadPoolConfiguration
 *
 * @author zhangyujin
 * @date 2024/10/10
 */
@Slf4j
public class ThreadPoolConfiguration {


    /**
     * 线程池任务
     */
    public final static ThreadPoolTaskExecutor THREAD_POOL_TASK_EXECUTOR;

    static {
        THREAD_POOL_TASK_EXECUTOR = threadPoolTaskExecutor();
    }

    private static ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        taskExecutor.setCorePoolSize(10);
        // 最大线程数：只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(100);
        // 缓存队列：用来缓冲执行任务的队列
        taskExecutor.setQueueCapacity(500);
        // 线程池维护线程所允许的空闲时间：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(60);
        // threadPoolTaskExecutor
        taskExecutor.setThreadNamePrefix("threadPoolTaskExecutor-");
        // 调度器shutdown被调用时等待当前被调度的任务完成：用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间:如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        log.info("Executor - threadPoolTaskExecutor injected!");
        return taskExecutor;
    }

}
