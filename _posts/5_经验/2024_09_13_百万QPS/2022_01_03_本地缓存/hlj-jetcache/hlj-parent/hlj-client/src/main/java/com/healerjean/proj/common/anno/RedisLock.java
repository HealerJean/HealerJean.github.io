package com.healerjean.proj.common.anno;

import com.healerjean.proj.common.contants.RedisConstants;

import java.lang.annotation.*;

/**
 * @author zhangyujin
 * @date 2023/5/26  11:42
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisLock {

    /**
     * 锁枚举
     *
     * @return RedisConstants.LockEnum
     */
    RedisConstants.LockEnum value();
}