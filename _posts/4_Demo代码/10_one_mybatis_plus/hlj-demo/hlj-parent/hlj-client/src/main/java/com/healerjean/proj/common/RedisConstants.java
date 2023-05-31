package com.healerjean.proj.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RedisConstants
 * @author zhangyujin
 * @date 2023/5/26  11:42
 */
public class RedisConstants {
    /**
     * 工程名/应用名
     * 以下两个应用SERVER_NAME使用同一个
     */
    private static final String SERVER_NAME = "projectName";
    /**
     * 分隔符，尽量使用官方推荐的英文冒号
     */
    public static final String SPLIT = ":";
    /**
     * Redis锁的标记
     */
    private static final String LOCK = "L";
    /**
     * Redis缓存的标记
     */
    private static final String CACHE = "C";

    /**
     * Redis锁的Key
     */
    @AllArgsConstructor
    @Getter
    public enum LockEnum {
        /**
         * Redis锁的Key
         */
        COMMON("COMMON", "公共LOCK", 60 * 10),
        SAAS_USER_CREATE("SAAS:USER:CREATE", "SaaS创建用户锁", 60),
        ;
        /**
         * key
         */
        private final String code;
        /**
         * 含义
         */
        private final String name;
        /**
         * 超时时长
         */
        private final Integer expireSec;

        /**
         * KEY中追加业务的属性
         *
         * @param args 业务属性
         * @return 得到的Key
         */
        public String join(Object... args) {
            StringBuilder sb = new StringBuilder(SERVER_NAME).append(SPLIT)
                    .append(LOCK).append(SPLIT).append(code);
            if (null == args || args.length < 1) {
                return sb.toString();
            }
            for (Object arg : args) {
                sb.append(SPLIT).append(arg);
            }
            return sb.toString();
        }
    }

    /**
     * Redis的缓存Key
     */
    @AllArgsConstructor
    @Getter
    public enum CacheEnum {
        /**
         * Redis的缓存Key
         */
        COMMON("COMMON", "公共CACHE", 60 * 10),
        USER("USER", "用户相关缓存", 60 * 60 * 2),
        ;
        /**
         * key
         */
        private final String code;
        /**
         * 含义
         */
        private final String name;
        /**
         * 超时时长
         * 单位秒
         */
        private final Integer expireSec;

        /**
         * KEY中追加业务的属性
         *
         * @param args 业务属性
         * @return 得到的Key
         */
        public String join(Object... args) {
            StringBuilder sb = new StringBuilder(SERVER_NAME).append(SPLIT)
                    .append(CACHE).append(SPLIT).append(code);
            if (null == args || args.length < 1) {
                return sb.toString();
            }
            for (Object arg : args) {
                sb.append(SPLIT).append(arg);
            }
            return sb.toString();
        }
    }
}
