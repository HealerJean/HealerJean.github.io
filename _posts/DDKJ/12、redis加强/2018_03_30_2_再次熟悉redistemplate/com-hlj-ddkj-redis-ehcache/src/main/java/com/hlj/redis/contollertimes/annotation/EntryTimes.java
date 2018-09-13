package com.hlj.redis.contollertimes.annotation;

import java.lang.annotation.*;

/**
 * @Desc: 防止一直非法请求，,在每次执行完一次成功的请求之后再发起下一次请求
 * @Date:  2018/9/10 下午9:13.
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
