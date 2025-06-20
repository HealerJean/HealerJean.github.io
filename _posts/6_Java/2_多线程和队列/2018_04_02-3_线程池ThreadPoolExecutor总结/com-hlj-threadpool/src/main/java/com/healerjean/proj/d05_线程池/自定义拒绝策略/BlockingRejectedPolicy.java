package com.healerjean.proj.d05_线程池.自定义拒绝策略;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 阻塞拒绝策略
 *
 * @author zhangyujin
 * @date 2025/4/24
 */
public class BlockingRejectedPolicy implements RejectedExecutionHandler {

    /**
     * queue
     */
    private final BlockingQueue<Runnable> queue;

    /**
     * BlockingRejectedPolicy
     *
     * @param queue queue
     */
    public BlockingRejectedPolicy(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            // 阻塞等待，直到队列有空闲位置（类似队列的 put 操作）
            queue.put(r);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Task interrupted: " + r, e);
        }
    }
}