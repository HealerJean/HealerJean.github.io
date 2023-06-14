package com.healerjean.proj.d05_线程池;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
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
     * ump key前缀（根据项目自行更改）
     */
    private static final String UMP_MONITOR_KEY_PREFIX = "www.baidu.com";


    /**
     * 统一配置中心ducc
     */
    private static DuccConfiguration duccConfiguration;


    /**
     * setDuccConfiguration
     *
     * @param duccConfiguration duccConfiguration
     */
    public static void setDuccConfiguration(DuccConfiguration duccConfiguration) {
        ThreadPoolUtils.duccConfiguration = duccConfiguration;
    }


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
        DEFAULT_THREAD_POOL_EXECUTOR = buildThreadPoolExecutor(ThreadPoolConstants.POOL_NAME_DEFAULT);
        RPC_THREAD_POOL_EXECUTOR = buildThreadPoolExecutor(ThreadPoolConstants.POOL_NAME_RPC);

        //定时任务
        Runnable poolThreadTask = () -> {
            updateExecutorConfig(DEFAULT_THREAD_POOL_EXECUTOR, ThreadPoolConstants.POOL_NAME_DEFAULT);
            updateExecutorConfig(RPC_THREAD_POOL_EXECUTOR, ThreadPoolConstants.POOL_NAME_RPC);
        };


        ScheduledExecutorService scheduledExecutor =new ScheduledThreadPoolExecutor(
                        ThreadPoolConstants.CONSTANT_1,
                        new BasicThreadFactory.Builder().namingPattern("thread-pool-monitor-task-%d").daemon(true).build());
        scheduledExecutor.scheduleAtFixedRate(poolThreadTask,
                TimeUnit.SECONDS.toMillis(ThreadPoolConstants.CONSTANT_5),
                TimeUnit.SECONDS.toMillis(ThreadPoolConstants.CONSTANT_1),
                TimeUnit.MILLISECONDS);
    }

    /**
     * 更新线程池参数并上报ump
     *
     * @param executor
     * @param poolName
     */
    private void updateExecutorConfig(ThreadPoolExecutor executor, String poolName) {
        log.info("updateExecutorConfig start");
        LOCK.lock();
        try {
            ThreadPoolConfiguration conf = getThreadPoolConfigFromDucc(poolName);
            if (conf.getCorePoolSize() != ThreadPoolConstants.CONSTANT_0 &&
                    conf.getCorePoolSize() != executor.getCorePoolSize()) {
                executor.setCorePoolSize(conf.getCorePoolSize());
            }
            if (conf.getMaximumPoolSize() != ThreadPoolConstants.CONSTANT_0 &&
                    conf.getMaximumPoolSize() != executor.getMaximumPoolSize()) {
                executor.setMaximumPoolSize(conf.getMaximumPoolSize());
            }
            if (conf.getKeepAliveSeconds() != ThreadPoolConstants.CONSTANT_0 &&
                    conf.getKeepAliveSeconds() != executor.getKeepAliveTime(TimeUnit.SECONDS)) {
                executor.setKeepAliveTime(conf.getKeepAliveSeconds(), TimeUnit.SECONDS);
            }
            if (executor.getQueue() instanceof ResizeableCapacityLinkedBlockingQueue) {
                ResizeableCapacityLinkedBlockingQueue queue =
                        (ResizeableCapacityLinkedBlockingQueue) executor.getQueue();
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
            log.debug("ThreadPoolUtils updateExecutorConfig poolName:{},dataMap:{}", poolName, dataMap);
            //上报ump todo zhangyujin1
            // Profiler.sourceDataByNum(UMP_MONITOR_KEY_PREFIX + poolName, dataMap);
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
        Map<String, Number> dataMap = new HashMap<>(ThreadPoolConstants.CONSTANT_16);
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
    private static ThreadPoolConfiguration getThreadPoolConfigFromDucc(String poolName) {
        String configStr = duccConfiguration.getThreadPoolConfig();
        log.debug("getThreadPoolConfFromDucc configStr:{}", configStr);
        List<ThreadPoolConfiguration> confList = JSONObject.parseArray(configStr, ThreadPoolConfiguration.class);
        for (ThreadPoolConfiguration conf : confList) {
            //get ducc config by poolName
            if (poolName.equals(conf.getName())) {
                //拒绝策略映射
                conf.buildRejectedExecutionHandler(conf.getRejectPolicyName());
                //队列类型
                conf.buildQueueType(conf.getQueueTypeName());
                return conf;
            }
        }
        return getDefaultConf();
    }

    /**
     * 获取默认配置
     *
     * @return
     */
    private static ThreadPoolConfiguration getDefaultConf() {
        return new ThreadPoolConfiguration(ThreadPoolConstants.POOL_NAME_DEFAULT, ThreadPoolConstants.CONSTANT_20,
                ThreadPoolConstants.CONSTANT_20, ThreadPoolConstants.CONSTANT_60,
                ThreadPoolConstants.CONSTANT_1024, ThreadPoolConstants.CALLER_RUNS_POLICY,
                ThreadPoolConstants.QUEUE_TYPE_RESIZEABLE_CAPACITY_LINKED_BLOCKING_QUEUE);
    }

    ////////////////////////////////////////本地测试/////////////////////////////////////////////////////////////////////////

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    private static ThreadPoolExecutor buildThreadPoolExecutor() {
        return new ThreadPoolExecutor(2,
                5,
                60,
                TimeUnit.SECONDS,
                new ResizeableCapacityLinkedBlockingQueue<>(10),
                new NamedThreadFactory("测试")
        );
    }

    /**
     * @throws InterruptedException
     */
    private static void test() throws InterruptedException {
        ThreadPoolExecutor executor = buildThreadPoolExecutor();
        for (int i = 0; i < IntConstants.CONSTANTS_15; i++) {
            executor.submit(() -> {
                threadPollStatus(executor, "创建任务");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info(new Date() + Thread.currentThread().getName() + " finished===");
            });
        }
        threadPollStatus(executor, "改变之前");
        executor.setCorePoolSize(10);
        executor.setMaximumPoolSize(10);
        executor.setKeepAliveTime(1, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        if (executor.getQueue() instanceof ResizeableCapacityLinkedBlockingQueue) {
            ResizeableCapacityLinkedBlockingQueue queue = (ResizeableCapacityLinkedBlockingQueue) executor.getQueue();
            queue.setCapacity(100);
        }
        threadPollStatus(executor, "改变之后");
        Thread.sleep(30000);
        threadPollStatus(executor, "最终？");
    }

    /**
     * @param executor
     * @param name
     */
    private static void threadPollStatus(ThreadPoolExecutor executor, String name) {
        log.info(new Date() + Thread.currentThread().getName() + "-" + name + "-:" +
                ",核心线程数：" + executor.getCorePoolSize() +
                ",活动线程数：" + executor.getActiveCount() +
                ",当前线程数：" + executor.getPoolSize() +
                ",最大线程数：" + executor.getMaximumPoolSize() +
                ",历史最大线程数：" + executor.getLargestPoolSize() +
                ",线程池活跃度：" + divide(executor.getActiveCount(), executor.getMaximumPoolSize()) +
                ",任务完成数：" + executor.getCompletedTaskCount() +
                ",队列大小：" + (executor.getQueue().size() + executor.getQueue().remainingCapacity()) +
                ",当前排队线程数：" + executor.getQueue().size() +
                ",队列剩余大小：" + executor.getQueue().remainingCapacity() +
                ",队列使用度：" + divide(executor.getQueue().size(),
                executor.getQueue().size() + executor.getQueue().remainingCapacity())

        );
    }

    /**
     * @param num1
     * @param num2
     * @return
     */
    private static String divide(int num1, int num2) {
        return String.format("%1.2f%%", Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }


    /**
     * 根据名称配置构建线程池
     *
     * @param poolName poolName
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor buildThreadPoolExecutor(String poolName) {
        ThreadPoolConfiguration threadPoolConfiguration = getThreadPoolConfigFromDucc(poolName);
        log.info("buildThreadPoolExecutor threadPoolConf:{}", threadPoolConfiguration);
        return new ThreadPoolExecutor(threadPoolConfiguration.getCorePoolSize(),
                threadPoolConfiguration.getMaximumPoolSize(),
                threadPoolConfiguration.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                threadPoolConfiguration.getQueueType(),
                r -> new Thread(r, poolName),
                threadPoolConfiguration.getRejectPolicy()
        );
    }

}
