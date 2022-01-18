package com.hlj.moudle.thread.D13_ThreadLocal.bean;

/**
 * 模拟业务处理类
 *
 * @author: chenyin
 * @date: 2019-10-22 13:56
 */
public class BusinessService {
    /**
     * 模拟同步处理业务
     */
    public void doBusiness() {
        System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalHolder.getUser());
    }
    /**
     * 模拟异步处理业务
     */
    public void doBusinessAsync() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalHolder.getUser());
        }).start();

    }

}
