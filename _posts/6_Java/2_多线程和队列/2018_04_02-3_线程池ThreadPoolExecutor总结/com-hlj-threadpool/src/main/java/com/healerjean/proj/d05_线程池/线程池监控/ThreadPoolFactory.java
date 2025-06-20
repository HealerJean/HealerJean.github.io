package com.healerjean.proj.d05_线程池.线程池监控;


import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.healerjean.proj.d05_线程池.自定义拒绝策略.BlockingRejectedPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池规范：创建线程要加监控，后续通过监控调整线程池参数，不能创建后一走了之，可以通过DUCC动态修改。但最好是DUCC配置调整结束后代中修改（http://juggle.jd.com/docs?g=com.jd.jade&a=jade&p=com.jd.jade.concurrent.ExecutorConfigSupport-ExecutorSetting）
 *
 * @author zhangyujin
 * @date 2024-09-29 05:09:52
 */
@Slf4j
public class ThreadPoolFactory {


    /**
     * 缓存刷新-运行时线程池
     */
    public static ThreadPoolExecutor CACHE_REFRESH_APP_RUN_THREAD_POOL;

    /**
     * 缓存刷新-项目启动线程池
     */
    public static ThreadPoolExecutor CACHE_REFRESH_APP_START_THREAD_POOL;


    static {
        CACHE_REFRESH_APP_RUN_THREAD_POOL = cacheRefreshAppRunThreadPool();
        CACHE_REFRESH_APP_START_THREAD_POOL = cacheRefreshAppStartThreadPool();

        // 定义监控任务
        Runnable monitorTask = () -> {
            monitorThreadPool("cacheRefreshRunThreadPool", CACHE_REFRESH_APP_RUN_THREAD_POOL);
            monitorThreadPool("cacheRefreshAppStartThreadPool", CACHE_REFRESH_APP_START_THREAD_POOL);
        };
        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("thread-pool-monitor-task-%d").daemon(true).build());
        scheduledExecutor.scheduleAtFixedRate(monitorTask, 5000, 1000, TimeUnit.MILLISECONDS);
    }


    /**
     * 缓存刷新-运行时线程池
     *
     * @return Executor
     */
    public static ThreadPoolExecutor cacheRefreshAppRunThreadPool() {
        int corePoolSize = 2;
        int maximumPoolSize = 2;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cacheRefreshAppStartThreadPool-%d").build();
        RejectedExecutionHandler handler = new BlockingRejectedPolicy (workQueue);
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    /**
     * 缓存刷新-项目启动线程池
     *
     * @return Executor
     */
    public static ThreadPoolExecutor cacheRefreshAppStartThreadPool() {
        int corePoolSize = 8;
        int maximumPoolSize = 8;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1000);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cacheRefreshAppStartThreadPool-%d").build();
        RejectedExecutionHandler handler = new BlockingRejectedPolicy (workQueue);
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    /**
     * 自定义上报属性，字段值与ump监控配置"自定义字段"一致
     *
     * @param executor executor
     * @return Map<String, Number>
     */
    private static Map<String, Number> monitorThreadPool(String poolName, ThreadPoolExecutor executor) {
        Map<String, Number> dataMap = new HashMap<>(16);
        // 核心线程数：返回线程池中核心线程的数量。核心线程是线程池中一直存在的线程，它们不会被销毁，除非线程池被关闭。
        dataMap.put("corePoolSize", executor.getCorePoolSize());
        // 最大线程数：返回线程池允许的最大线程数量。当工作队列满时，线程池可以创建的新线程的最大数量。
        dataMap.put("maximumPoolSize", executor.getMaximumPoolSize());
        // 当前线程数：返回线程池中当前线程的数量，包括空闲线程和活动线程
        dataMap.put("poolSize", executor.getPoolSize());
        // 活动线程数：返回当前正在执行任务的线程数量
        dataMap.put("activeCount", executor.getActiveCount());
        // 队列大小
        dataMap.put("queueCapacity", executor.getQueue().size() + executor.getQueue().remainingCapacity());
        // 当前排队线程数：当前等待执行的任务数量，即已经提交但尚未开始执行的任务数量。
        dataMap.put("queueSize", executor.getQueue().size());
        // 队列剩余容量：返回队列还能容纳的任务数量，即队列的剩余空间。
        dataMap.put("remainingCapacity", executor.getQueue().remainingCapacity());
        // 任务完成数：返回线程池已经成功完成的任务总数
        dataMap.put("completedTaskCount", executor.getCompletedTaskCount());
        // 历史最大线程数：返回线程池在其生命周期中曾经达到的最大线程数量
        dataMap.put("largestPoolSize", executor.getLargestPoolSize());

        String msg = MessageFormatter.format("monitor poolName:{},dataMap:{}", poolName, JSON.toJSONString(stringNumberMap)).getMessage();
        System.out.println(msg);
        return dataMap;
    }

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 10000000; i++) {
            CACHE_REFRESH_APP_RUN_THREAD_POOL.submit(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        Thread.sleep(1000000000000000000L);
    }




}
