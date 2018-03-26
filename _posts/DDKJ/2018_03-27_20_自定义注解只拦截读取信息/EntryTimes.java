package com.duodian.admore.zhaobutong.annotation;

import java.lang.annotation.*;

/**
 * Created by fengchuanbo on 2017/5/25.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntryTimes {

    /**
     * 方法允许进入的次数
     * @return
     */
    int value() default 1;

    /**
     * 可以的前缀
     * @return
     */
    String prefix() default "";

}
