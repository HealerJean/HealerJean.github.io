package com.hlj.threadpool.d03_队列.d02_阻塞队列;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author HealerJean
 * @date 2020/12/8  14:11.
 * @description
 */
public class Order implements Delayed {


    /** 延迟时间  */
    private long delayTime;
    String name;

    public Order(String name, long delay, TimeUnit unit) {
        this.name = name;
        this.delayTime = System.currentTimeMillis() + (delay > 0 ? unit.toMillis(delay) : 0);
    }

    /** 用于设置延期时间 */
    @Override
    public long getDelay(TimeUnit unit) {
        return delayTime - System.currentTimeMillis();
    }

    /** 方法负责对队列中的元素进行排序 */
    @Override
    public int compareTo(Delayed o) {
        Order order = (Order) o;
        long diff = this.delayTime - order.delayTime;
        if (diff <= 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
