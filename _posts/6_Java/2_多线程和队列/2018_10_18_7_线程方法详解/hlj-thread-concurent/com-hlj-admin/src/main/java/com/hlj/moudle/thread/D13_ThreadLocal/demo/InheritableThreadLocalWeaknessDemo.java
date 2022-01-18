package com.hlj.moudle.thread.D13_ThreadLocal.demo;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示InheritableThreadLocal的缺陷
 */
public class InheritableThreadLocalWeaknessDemo {

    private static final InheritableThreadLocal<Integer> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);


    @Test
    public void test() {
        //模拟同时10个web请求，一个请求一个线程
        for (int i = 0; i < 10; i++) {
            new TomcatThread(i).start();
        }


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class TomcatThread extends Thread {
        private int index;

        public TomcatThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            String curThreadName = Thread.currentThread().getName();
            System.out.println(curThreadName + ":" + index);
            INHERITABLE_THREAD_LOCAL.set(index);
            threadPool.submit(new BusinessThread(curThreadName));
        }
    }

    static class BusinessThread implements Runnable {
        private String parentThreadName;

        public BusinessThread(String parentThreadName) {
            this.parentThreadName = parentThreadName;
        }

        @Override
        public void run() {
            System.out.println("parent:" + parentThreadName + ":" + INHERITABLE_THREAD_LOCAL.get());
        }
    }
}
