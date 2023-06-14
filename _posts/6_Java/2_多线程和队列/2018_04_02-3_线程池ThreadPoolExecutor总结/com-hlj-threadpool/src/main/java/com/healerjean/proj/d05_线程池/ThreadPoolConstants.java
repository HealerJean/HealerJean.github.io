package com.healerjean.proj.d05_线程池;


/**
 * ThreadPoolConstants
 *
 * @author zhangyujin
 * @date 2023/6/13  18:14
 */
public class ThreadPoolConstants {

    /**
     * CONSTANT_0
     */
    final static int CONSTANT_0 = 0;

    /**
     * CONSTANT_1
     */
    final static int CONSTANT_1 = 1;

    /**
     * CONSTANT_5
     */
    final static int CONSTANT_5 = 5;

    /**
     * CONSTANT_16
     */
    final static int CONSTANT_16 = 16;

    /**
     * CONSTANT_20
     */
    final static int CONSTANT_20 = 20;

    /**
     * CONSTANT_60
     */
    final static int CONSTANT_60 = 60;

    /**
     * CONSTANT_1024
     */
    final static int CONSTANT_1024 = 1024;

    /**
     * 拒绝策略
     * 丢弃任务并抛出RejectedExecutionException异常
     */
    final static String ABORT_POLICY = "AbortPolicy";

    /**
     * 拒绝策略
     * 丢弃任务，但是不抛出异常。
     */
    final static String DISCARD_POLICY = "DiscardPolicy";

    /**
     * 拒绝策略
     * 丢弃队列最前面的任务，然后重新提交被拒绝的任务。
     */
    final static String DISCARD_OLDEST_POLICY = "DiscardOldestPolicy";

    /**
     * 拒绝策略
     * 由调用线程处理该任务
     */
    final static String CALLER_RUNS_POLICY = "CallerRunsPolicy";

    /**
     * 队列类型：
     * 无缓冲等待队列
     */
    final static String QUEUE_TYPE_SYNCHRONOUS_QUEUE = "SynchronousQueue";

    /**
     * 队列类型：
     * 无界缓存等待队列
     */
    final static String QUEUE_TYPE_RESIZEABLE_CAPACITY_LINKED_BLOCKING_QUEUE = "LinkedBlockingQueue";

    /**
     * 默认线程池名称
     */
    final static String POOL_NAME_DEFAULT = "default";

    /**
     * rpc线程池名称
     */
    final static String POOL_NAME_RPC = "rpc";
}
