package com.healerjean.proj.cache.A02_我;

/**
 * @author HealerJean
 * @date 2020/12/14  11:21.
 * @description
 */
public interface DistributedLock {


    static final long TIMEOUT_MILLIS = 30000; //超时时间

    static final int RETRY_TIMES = Integer.MAX_VALUE; //重试次数

    static final long SLEEP_MILLIS = 500; //重试时 线程休眠次数

    boolean lock(String key);

    boolean lock(String key, int retryTimes);

    boolean lock(String key, int retryTimes, long sleepMillis);

    boolean lock(String key, long expire);

    boolean lock(String key, long expire, int retryTimes);

    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    boolean releaseLock(String key);
}
