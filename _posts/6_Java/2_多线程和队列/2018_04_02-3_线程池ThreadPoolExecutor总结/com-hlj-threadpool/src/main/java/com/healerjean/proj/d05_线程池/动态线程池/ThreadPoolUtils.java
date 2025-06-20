package com.healerjean.proj.d05_线程池.动态线程池;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ThreadPoolUtils
 *
 * @author zhangyujin
 * @date 2023/6/13  18:17
 */
@Slf4j
@Component
public class ThreadPoolUtils {

    /**
     * 统一配置中心ducc
     */
    @Resource
    private DuccThreadPoolProp dcThreadPoolProp;

    /**
     * 锁
     */
    private final static ReentrantLock LOCK = new ReentrantLock();


    /**
     * 默认线程池
     */
    public static ThreadPoolExecutor DEFAULT_THREAD_POOL_EXECUTOR;

    /**
     * 自定义线程池 rpc
     */
    public static ThreadPoolExecutor RPC_THREAD_POOL_EXECUTOR;


    /**
     * 初始化线程池并启动定时监测任务
     */
    @PostConstruct
    private void init() {
        DEFAULT_THREAD_POOL_EXECUTOR = buildThreadPoolExecutor(ThreadEnum.ThreadPoolEnum.DEFAULT);
        RPC_THREAD_POOL_EXECUTOR = buildThreadPoolExecutor(ThreadEnum.ThreadPoolEnum.RPC);

        //定时任务
        Runnable poolThreadTask = () -> {
            updateExecutorConfig(DEFAULT_THREAD_POOL_EXECUTOR, ThreadEnum.ThreadPoolEnum.DEFAULT);
            updateExecutorConfig(RPC_THREAD_POOL_EXECUTOR, ThreadEnum.ThreadPoolEnum.RPC);
        };

        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(
                1,
                new BasicThreadFactory.Builder().namingPattern("thread-pool-monitor-task-%d").daemon(true).build());
        scheduledExecutor.scheduleAtFixedRate(poolThreadTask,
                5000,
                1000,
                TimeUnit.MILLISECONDS);
    }


    /**
     * 更新线程池参数并上报ump
     *
     * @param executor       executor
     * @param threadPoolEnum threadPoolEnum
     */
    private void updateExecutorConfig(ThreadPoolExecutor executor, ThreadEnum.ThreadPoolEnum threadPoolEnum) {
        String poolName = threadPoolEnum.getCode();
        log.info("updateExecutorConfig start");
        LOCK.lock();
        try {
            ThreadPoolConfig conf = getThreadPoolConfigFromDucc(poolName);
            if (conf.getCorePoolSize() != 0 && conf.getCorePoolSize() != executor.getCorePoolSize()) {
                executor.setCorePoolSize(conf.getCorePoolSize());
            }
            if (conf.getMaximumPoolSize() != 0 && conf.getMaximumPoolSize() != executor.getMaximumPoolSize()) {
                executor.setMaximumPoolSize(conf.getMaximumPoolSize());
            }
            if (conf.getKeepAliveSeconds() != 0 && conf.getKeepAliveSeconds() != executor.getKeepAliveTime(TimeUnit.SECONDS)) {
                executor.setKeepAliveTime(conf.getKeepAliveSeconds(), TimeUnit.SECONDS);
            }
            if (executor.getQueue() instanceof ResizeableCapacityLinkedBlockingQueue) {
                ResizeableCapacityLinkedBlockingQueue queue = (ResizeableCapacityLinkedBlockingQueue) executor.getQueue();
                int capacity = queue.size() + queue.remainingCapacity();
                if (conf.getQueueCapacity() != 0 && conf.getQueueCapacity() != capacity) {
                    queue.setCapacity(conf.getQueueCapacity());
                }
            }
            if (conf.getRejectPolicy() != null && conf.getRejectPolicy() != executor.getRejectedExecutionHandler()) {
                executor.setRejectedExecutionHandler(conf.getRejectPolicy());
            }
            //统计线程池信息
            Map<String, Number> dataMap = buildCustomerProperty(executor);
            // log.debug("ThreadPoolUtils updateExecutorConfig poolName:{},dataMap:{}", poolName, dataMap);
            // Profiler.sourceDataByNum(UmpConstants.THREAD_POOL_UMP_KEY + poolName, dataMap);
        } catch (Exception e) {
            log.error("updateExecutorConfig error:{}", e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
        log.info("updateExecutorConfig end");
    }


    /**
     * 自定义上报属性，字段值与ump监控配置"自定义字段"一致
     *
     * @return Map<String, Number>
     */
    private  Map<String, Number> buildCustomerProperty(ThreadPoolExecutor executor ) {
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
        return dataMap;
    }


    /**
     * 根据名称获取线程池配置
     *
     * @param poolName poolName
     * @return ThreadPoolConfiguration
     */
    private ThreadPoolConfig getThreadPoolConfigFromDucc(String poolName) {
        String threadPoolConfigs = dcThreadPoolProp.getThreadPoolConfigs();
        log.debug("[ThreadPoolUtils#getThreadPoolConfFromDucc] configStr:{}", threadPoolConfigs);
        List<ThreadPoolConfig> confList = JSONObject.parseArray(threadPoolConfigs, ThreadPoolConfig.class);
        for (ThreadPoolConfig conf : confList) {
            if (poolName.equals(conf.getName())) {
                //拒绝策略映射
                ThreadEnum.RejectPolicyEnum rejectPolicyEnum = ThreadEnum.RejectPolicyEnum.toRejectPolicyEnum(conf.getRejectPolicyName());
                conf.buildRejectedExecutionHandler(rejectPolicyEnum);
                ThreadEnum.QueueEnum queueEnum = ThreadEnum.QueueEnum.toQueueEnum(conf.getQueueTypeName());
                //队列类型
                conf.buildQueueType(queueEnum);
                return conf;
            }
        }
        return getDefaultConf();
    }


    /**
     * getDefaultConf
     *
     * @return ThreadPoolConfiguration
     */
    private static ThreadPoolConfig getDefaultConf() {
        return new ThreadPoolConfig(
                ThreadEnum.ThreadPoolEnum.DEFAULT.getCode(),
                20,
                20,
                60,
                1024, ThreadEnum.RejectPolicyEnum.CALLER_RUNS_POLICY,
                ThreadEnum.QueueEnum.RESIZEABLE_CAPACITY_LINKED_BLOCKING_QUEUE);
    }


    /**
     * 根据名称配置构建线程池
     *
     * @param threadPoolEnum threadPoolEnum
     * @return ThreadPoolExecutor
     */
    private ThreadPoolExecutor buildThreadPoolExecutor(ThreadEnum.ThreadPoolEnum threadPoolEnum) {
        ThreadPoolConfig threadPoolConfig = getThreadPoolConfigFromDucc(threadPoolEnum.getCode());
        log.info("[ThreadPoolUtils#buildThreadPoolExecutor] threadPoolConf:{}", threadPoolConfig);
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                threadPoolConfig.getMaximumPoolSize(),
                threadPoolConfig.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                threadPoolConfig.getQueueType(),
                r -> new Thread(r, threadPoolEnum.getCode()),
                threadPoolConfig.getRejectPolicy()
        );
    }


}
