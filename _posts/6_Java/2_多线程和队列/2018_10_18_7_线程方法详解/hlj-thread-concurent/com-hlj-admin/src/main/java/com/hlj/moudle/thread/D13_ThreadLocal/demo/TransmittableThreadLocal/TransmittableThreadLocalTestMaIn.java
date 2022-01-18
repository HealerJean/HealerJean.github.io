package com.hlj.moudle.thread.D13_ThreadLocal.demo.TransmittableThreadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransmittableThreadLocalTestMaIn {

    /**
     * 需要注意的是，使用TTL的时候，要想传递的值不出问题，线程池必须得用TTL加一层代理
     */
    private static ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));

    private static TransmittableThreadLocal ttl = new TransmittableThreadLocal<>();

    /**
     * 1、线程池的方式
     */
    @Test
    public void test() {

        new Thread(() -> {

            String mainThreadName = Thread.currentThread().getName();

            ttl.set(1);

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            sleep(1L); //确保上面的会在tl.set执行之前执行
            ttl.set(2); // 等上面的线程池第一次启用完了，父线程再给自己赋值

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));

        }).start();


        sleep(5000L);
    }


    /**
     * 2、普通异步线程
     */
    @Test
    public void test2() {

        new Thread(() -> {

            String mainThreadName = Thread.currentThread().getName();

            ttl.set(1);

            new Thread(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            }).start();

            new Thread(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            }).start();

            new Thread(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            }).start();

            sleep(1L); //确保上面的会在tl.set执行之前执行
            ttl.set(2); // 等上面的线程池第一次启用完了，父线程再给自己赋值

            new Thread(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            }).start();

            new Thread(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            }).start();

            new Thread(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            }).start();

            System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));

        }).start();


        sleep(5000L);

    }


    /**
     * 1、主线程remove，不会影响子线程的值
     */
    @Test
    public void test3() {

        new Thread(() -> {
            String mainThreadName = Thread.currentThread().getName();

            ttl.set(1);

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
                sleep(4000L);
                System.out.println(String.format("主线程remove,本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
                //主线程remove,本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-pool-1-thread-1, 变量值=1
                //可以看到主线程remove了，子线程还有值
            });

            sleep(2000L);//确保子线程进入sleep 主线程才remove
            ttl.remove();
            System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));
            sleep(10000L); //等待主线程执行

        }).start();
        sleep(20000L);
    }


    /**
     * 1、子线程remove，不会影响子线程的值
     */
    @Test
    public void test4() {

        new Thread(() -> {
            String mainThreadName = Thread.currentThread().getName();

            ttl.set(1);

            executorService.execute(() -> {
                sleep(1L);
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
                ttl.remove();
                System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
            });

            sleep(2000L);//确保子线程执行结束
            System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));

        }).start();
        sleep(20000L);
    }


    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

