package com.healerjean.proj.config.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author chenck
 * @date 2020/6/30 17:19
 */
@Getter
@Setter
@Accessors(chain = true)
public class MultilevelCacheConfig {

    /**
     * 组合缓存-配置
     */
    private final CompositeConfig compositeConfig = new CompositeConfig();
    /**
     * 一级缓存-配置
     */
    private final CaffeineConfig caffeineConfig = new CaffeineConfig();

    /**
     * 二级缓存-配置
     */
    private final RedisConfig redisConfig = new RedisConfig();

    /**
     * 是否存储空值，设置为true时，可防止缓存穿透
     */
    private boolean allowNullValues = true;

    /**
     * 是否动态根据cacheName创建Cache的实现，默认true
     */
    private boolean dynamic = true;

    /**
     * 缓存名称集合
     */
    private Set<String> cacheNames = new HashSet<>();


    public interface Config {
    }

    /**
     * 组合缓存配置
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class CompositeConfig implements Config {

        /**
         * 是否全部启用一级缓存，默认false
         */
        private boolean level1AllSwitch = false;

        /**
         * 是否手动启用一级缓存，默认false
         */
        private boolean level1Switch = false;

        /**
         * 手动配置走一级缓存的缓存key集合，针对单个key维度
         */
        private Set<String> level1SwitchKeys = new HashSet<>();

        /**
         * 手动配置走一级缓存的缓存名字集合，针对cacheName维度
         */
        private Set<String> level1SwitchCacheNames = new HashSet<>();

    }

    /**
     * Caffeine specific cache properties.
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class CaffeineConfig implements Config {
        /**
         * 是否自动刷新过期缓存 true 表示是(默认)，false 表示否
         */
        private boolean autoRefreshExpireCache = false;

        /**
         * 缓存刷新调度线程池的大小
         * 默认为 CPU数 * 2
         */
        private Integer refreshPoolSize = Runtime.getRuntime().availableProcessors();

        /**
         * 初始化大小
         */
        private int initialCapacity;

        /**
         * 最大缓存对象个数，超过此数量时之前放入的缓存将失效
         */
        private long maximumSize;

        /**
         * 缓存刷新的频率(秒)
         */
        private Long refreshPeriod = 30L;

        /**
         * 同一个key的发布消息频率(毫秒)
         */
        private Long publishMsgPeriodMilliSeconds = 500L;

        /**
         * 访问后过期时间，单位秒
         */
        private long expireAfterAccess;

        /**
         * 写入后过期时间，单位秒
         */
        private long expireAfterWrite;

        /**
         * 写入后刷新时间，单位秒
         */
        private long refreshAfterWrite;

    }


    /**
     * Redis specific cache properties.
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class RedisConfig implements Config {

        /**
         * 全局过期时间，单位毫秒，默认不过期
         */
        private long defaultExpiration = 0;

        /**
         * 每个cacheName的过期时间，单位毫秒，优先级比defaultExpiration高
         */
        private Map<String, Long> expires = new HashMap<>();

        /**
         * 缓存更新时通知其他节点的topic名称
         */
        private String topic = "cache:redis:caffeine:topic";

    }

}
