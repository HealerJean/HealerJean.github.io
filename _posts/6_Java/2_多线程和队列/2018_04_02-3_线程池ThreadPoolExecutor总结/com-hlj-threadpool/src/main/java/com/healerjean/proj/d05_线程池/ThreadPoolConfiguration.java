package com.healerjean.proj.d05_线程池;


import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;


/**
 * 线程池配置属性
 *
 * @author zhangyujin
 * @date 2023/6/13  18:01
 */
@Data
public class ThreadPoolConfiguration implements Serializable {

    /**
     * 线程名称
     */
    private String name;
    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maximumPoolSize;
    /**
     * 存活时间（秒）
     */
    private int keepAliveSeconds;
    /**
     * 队列长度
     */
    private int queueCapacity;
    /**
     * 拒绝策略名称
     */
    private String rejectPolicyName;

    /**
     *
     * 拒绝策略
     */
    private RejectedExecutionHandler rejectPolicy;

    /**
     * 队列类型名称
     */
    private String queueTypeName;

    /**
     * 队列类型
     */
    private BlockingQueue<Runnable> queueType;

    /**
     *
     */
    public ThreadPoolConfiguration() {
    }


    /**
     * ThreadPoolConfiguration
     *
     * @param name             name
     * @param corePoolSize     corePoolSize
     * @param maximumPoolSize  maximumPoolSize
     * @param keepAliveSeconds keepAliveSeconds
     * @param queueCapacity    queueCapacity
     * @param rejectPolicyName rejectPolicyName
     * @param queueTypeName    queueTypeName
     */
    public ThreadPoolConfiguration(String name, int corePoolSize, int maximumPoolSize, int keepAliveSeconds, int queueCapacity,
                                   String rejectPolicyName, String queueTypeName) {
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveSeconds = keepAliveSeconds;
        this.queueCapacity = queueCapacity;
        this.rejectPolicyName = rejectPolicyName;
        buildRejectedExecutionHandler(rejectPolicyName);
        buildQueueType(queueTypeName);
    }

    /**
     * name相同就认为是相同的配置信息
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (o instanceof ThreadPoolConfiguration) {
            ThreadPoolConfiguration that = (ThreadPoolConfiguration) o;
            return Objects.equals(name, that.name);
        }
        return false;
    }



    /**
     * 拒绝策略映射
     *
     * @param rejectPolicyName rejectPolicyName
     */
    public void buildRejectedExecutionHandler(String rejectPolicyName) {
        if (StringUtils.isEmpty(rejectPolicyName)) {
            this.rejectPolicy = new CallerRunsPolicy();
            return;
        }
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (rejectPolicyName) {
            case ThreadPoolConstants.ABORT_POLICY:
                rejectedExecutionHandler = new AbortPolicy();
                break;
            case ThreadPoolConstants.DISCARD_POLICY:
                rejectedExecutionHandler = new DiscardPolicy();
                break;
            case ThreadPoolConstants.DISCARD_OLDEST_POLICY:
                rejectedExecutionHandler = new DiscardOldestPolicy();
                break;
            default:
                rejectedExecutionHandler = new CallerRunsPolicy();
        }
        this.rejectPolicy = rejectedExecutionHandler;
    }

    /**
     * 队列类型映射
     *
     * @param queueTypeName queueTypeName
     */
    public void buildQueueType(String queueTypeName) {
        if (ThreadPoolConstants.QUEUE_TYPE_SYNCHRONOUS_QUEUE.equals(queueTypeName)) {
            this.queueType = new SynchronousQueue<>();
            return;
        }
        //默认类型：自定义无界缓存等待队列
        this.queueType = new ResizeableCapacityLinkedBlockingQueue<>(this.queueCapacity);
    }
}
