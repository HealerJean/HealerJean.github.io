package com.healerjean.proj.localcache.guavacache.exer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.healerjean.proj.localcache.guavacache.exer.dto.CacheDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuavaCacheService {

    private static Executor executor =  Executors.newFixedThreadPool(10);
    private static LoadingCache<Integer, Optional<CacheDTO>> cache = null;

    public static void initCache() {
        log.info("线程名：{}, LoadingCache初始化", Thread.currentThread().getName());

        cache = CacheBuilder.newBuilder()
                //设置并发级别为8，
                .concurrencyLevel(8)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置最大存储
                .maximumSize(100)
                //是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
                .recordStats()
                // 缓存清除策略
                //  expireAfterWrite 写缓存后多久过期
                //  expireAfterAccess 缓存项在给定时间内没有被读/写访问
                .expireAfterWrite(5, TimeUnit.SECONDS)
                // 定时刷新，只阻塞当前数据加载线程，其他线程返回旧值。
                .refreshAfterWrite(7, TimeUnit.SECONDS)

                //设置缓存的移除通知
                .removalListener(notification -> {
                    log.info("缓存移除通知：key:{}, value{}, case:{}", notification.getKey(), notification.getValue(), notification.getCause());
                })

                .build(new CacheLoader<Integer, Optional<CacheDTO>>() {
                    @Override
                    public Optional<CacheDTO> load(Integer id) throws Exception {
                        log.info("线程名：{}, load 开始", Thread.currentThread().getName());
                        CacheDTO cacheDTO = new CacheDTO()
                                .setId(1)
                                .setName("HealerJean");
                        log.info("线程名：{}, load 结束", Thread.currentThread().getName());
                        return Optional.ofNullable(cacheDTO);

                    }

                    @Override
                    public ListenableFuture<Optional<CacheDTO>> reload(Integer id, Optional<CacheDTO> oldValu) throws Exception {
                        log.info("线程名：{}, reload 开始", Thread.currentThread().getName());
                        ListenableFutureTask<Optional<CacheDTO>> futureTask = ListenableFutureTask.create(() -> {
                            Random random = new Random();
                            CacheDTO cacheDTO = new CacheDTO()
                                    .setId(random.nextInt())
                                    .setName("HealerJean");
                            return Optional.ofNullable(cacheDTO);
                        });
                        executor.execute(futureTask);
                        log.info("线程名：{}, reload 已执行", Thread.currentThread().getName());
                        return futureTask;
                    }

                });

        log.info("线程名：{}, LoadingCache 结束", Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        initCache();


        try {
            Optional<CacheDTO> cacheDTO = cache.get(1);
        } catch (ExecutionException e) {
            log.info(" cache.get(1) ", e);

        }

        //模拟线程并发
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    CacheDTO cacheDTO = cache.get(1).orElse(null);
                    log.info("main1 线程名:{}  时间:{} cache:{}", Thread.currentThread().getName(), LocalDateTime.now(), cacheDTO);
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (Exception ignored) {
            }
        }).start();


        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    CacheDTO cacheDTO = cache.get(0, () -> {
                        CacheDTO cacheDTO1 = new CacheDTO()
                                .setId(0)
                                .setName("NULL");
                        return Optional.ofNullable(cacheDTO1);
                    }).orElse(null);


                    log.info("main2 线程名:{}  时间:{} cache:{}", Thread.currentThread().getName(), LocalDateTime.now(), cacheDTO);
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (Exception ignored) {
            }
        }).start();


        log.info("缓存统计信息：{}", cache.stats());
    }

}