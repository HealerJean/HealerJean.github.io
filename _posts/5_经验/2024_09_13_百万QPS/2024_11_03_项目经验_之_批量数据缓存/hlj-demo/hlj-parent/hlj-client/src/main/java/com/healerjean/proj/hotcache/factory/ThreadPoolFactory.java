package com.healerjean.proj.hotcache.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工厂，支持通过名称创建或获取预定义线程池。
 * 可配合配置中心动态重建线程池。
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
public class ThreadPoolFactory {

    private static final java.util.Map<String, ExecutorService> threadPools = new java.util.HashMap<>();

    static {
        // 预创建默认线程池
        createThreadPool("default-pool", 8);
    }

    public static ExecutorService getThreadPool(String poolName) {
        return threadPools.computeIfAbsent(poolName, name -> createThreadPool(name, 4));
    }

    private static ExecutorService createThreadPool(String poolName, int coreSize) {
        ExecutorService executor = Executors.newFixedThreadPool(
                coreSize,
                r -> {
                    Thread t = new Thread(r, "DataLoader-" + poolName);
                    t.setDaemon(true);
                    return t;
                }
        );
        threadPools.put(poolName, executor);
        return executor;
    }

    public static void shutdownAll() {
        for (ExecutorService executor : threadPools.values()) {
            executor.shutdown();
        }
    }
}