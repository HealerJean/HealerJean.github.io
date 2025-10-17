package com.healerjean.proj.common.anno;


import com.healerjean.proj.common.contants.RedisConstants;

import java.lang.annotation.*;

/**
 * 缓存配置
 *
 * @date 2020/3/31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisCache {

    /**
     * 缓存枚举值
     *
     * @return 缓存枚举值
     */
    RedisConstants.CacheEnum value();

    /**
     * 是否开启缓存
     *
     * @return 默认开启
     */
    boolean enabled() default true;
}
