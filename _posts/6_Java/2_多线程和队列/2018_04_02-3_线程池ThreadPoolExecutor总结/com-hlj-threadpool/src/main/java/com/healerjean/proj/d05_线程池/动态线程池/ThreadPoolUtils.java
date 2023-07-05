package com.healerjean.proj.d05_线程池.动态线程池;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
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
     * @param executor executor
     * @return Map<String, Number>
     */
    private Map<String, Number> buildCustomerProperty(ThreadPoolExecutor executor) {
        Map<String, Number> dataMap = new HashMap<>(16);
        dataMap.put("corePoolSize", executor.getCorePoolSize());
        dataMap.put("maximumPoolSize", executor.getMaximumPoolSize());
        dataMap.put("poolSize", executor.getPoolSize());
        dataMap.put("activeCount", executor.getActiveCount());
        dataMap.put("queueCapacity", executor.getQueue().size() + executor.getQueue().remainingCapacity());
        dataMap.put("queueSize", executor.getQueue().size());
        dataMap.put("remainingCapacity", executor.getQueue().remainingCapacity());
        dataMap.put("completedTaskCount", executor.getCompletedTaskCount());
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
