package com.hlj.jvm.JvmBin.JvmJconsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description 线程死循环和wait演示
 * @Author HealerJean
 * @Date 2018/4/10  下午6:52.
 */

public class JConsoleThreadWhileTest {
    /**
     * 线程一直跑while
     */
    public static void createBusyThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 状态: RUNNABLE 会在空循环上用尽全部执行时间直到线程切换，这种等待会消耗较多的CPU资源
                while (true) {

                }
            }
        }, "testBusyThread");
        thread.start();
    }

    /**
     * 锁等待测试
     *
     * @param lock
     */
    public static void createLockThread(final Object lock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        // 线程等待着lock对象的notify或notifyAll方法的出现，线程这时间处于waiting状态，在被唤醒前不会被分配执行时间。
                        // 处理活锁状态，只要lock对象的notify或notifyAll方法出现，这个线程便能激活断续执行，
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "testLockThread");
        thread.start();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        // 控制台输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();// 线程一直跑while
        br.readLine();
        Object obj = new Object();
        createLockThread(obj);// 锁等待测试
        Thread.sleep(10000000);
    }
}