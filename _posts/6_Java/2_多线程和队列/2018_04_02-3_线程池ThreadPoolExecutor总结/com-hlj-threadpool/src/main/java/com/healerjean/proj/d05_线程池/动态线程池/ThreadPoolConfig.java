package com.healerjean.proj.d05_线程池.动态线程池;

import lombok.Data;

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
public class ThreadPoolConfig implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6763948033008435707L;

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
    public ThreadPoolConfig() {
    }


    /**
     * ThreadPoolConfiguration
     *
     * @param name             name
     * @param corePoolSize     corePoolSize
     * @param maximumPoolSize  maximumPoolSize
     * @param keepAliveSeconds keepAliveSeconds
     * @param queueCapacity    queueCapacity
     * @param rejectPolicyEnum rejectPolicyEnum
     * @param queueTypeEnum    queueTypeEnum
     */
    public ThreadPoolConfig(String name, int corePoolSize, int maximumPoolSize, int keepAliveSeconds, int queueCapacity,
                            ThreadEnum.RejectPolicyEnum rejectPolicyEnum, ThreadEnum.QueueEnum queueTypeEnum) {
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveSeconds = keepAliveSeconds;
        this.queueCapacity = queueCapacity;
        this.rejectPolicyName = rejectPolicyEnum.getCode();
        buildRejectedExecutionHandler(rejectPolicyEnum);
        buildQueueType(queueTypeEnum);
    }



    /**
     * 拒绝策略映射
     *
     * @param rejectPolicyEnum rejectPolicyEnum
     */
    public void buildRejectedExecutionHandler(ThreadEnum.RejectPolicyEnum rejectPolicyEnum) {
        if (Objects.isNull(rejectPolicyEnum)) {
            this.rejectPolicy = new CallerRunsPolicy();
            return;
        }
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (rejectPolicyEnum) {
            case ABORT_POLICY:
                rejectedExecutionHandler = new AbortPolicy();
                break;
            case DISCARD_POLICY:
                rejectedExecutionHandler = new DiscardPolicy();
                break;
            case DISCARD_OLDEST_POLICY:
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
     * @param queueTypeEnum queueTypeEnum
     */
    public void buildQueueType(ThreadEnum.QueueEnum queueTypeEnum) {
        if (ThreadEnum.QueueEnum.SYNCHRONOUS_QUEUE == queueTypeEnum) {
            this.queueType = new SynchronousQueue<>();
            return;
        }
        //默认类型：自定义无界缓存等待队列
        this.queueType = new ResizeableCapacityLinkedBlockingQueue<>(this.queueCapacity);
    }
}