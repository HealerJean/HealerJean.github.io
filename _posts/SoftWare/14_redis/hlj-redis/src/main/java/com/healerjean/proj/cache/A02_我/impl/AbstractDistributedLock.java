package com.healerjean.proj.cache.A02_我.impl;

/**
 * @author HealerJean
 * @date 2020/12/14  11:23.
 * @description
 */

import com.healerjean.proj.cache.A02_我.DistributedLock;

/**
 * redis锁，原地址https://gitee.com/itopener/springboot/tree/master
 */
public abstract class AbstractDistributedLock implements DistributedLock {


    @Override
    public boolean lock(String key) {
        return lock(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, int retryTimes) {
        return lock(key, TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, int retryTimes, long sleepMillis) {
        return lock(key, TIMEOUT_MILLIS, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key, long expire) {
        return lock(key, expire, RETRY_TIMES, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes) {
        return lock(key, expire, retryTimes, SLEEP_MILLIS);
    }

}
