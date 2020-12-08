package com.hlj.threadpool.d03_队列.d01_普通队列;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @author HealerJean
 * @date 2020/12/8  13:49.
 * @description
 */
public class TestMain {

    @Test
    public void test1() {
        BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue();
        blockingQueue.add(null);
    }

    @Test
    public void test2() {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.add(null);
    }

    @Test
    public void test3() {
        BlockingQueue<Object> blockingQueue = new PriorityBlockingQueue<>();
        blockingQueue.add(null);
    }

    @Test
    public void test4() {
        BlockingQueue<Object> blockingQueue = new SynchronousQueue<>();
        blockingQueue.add(null);
    }


    @Test
    public void test5() {
        BlockingQueue<Object> blockingQueue = new DelayQueue();
        blockingQueue.add(null);
    }
}
