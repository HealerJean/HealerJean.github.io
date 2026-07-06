package com.healerjean.proj.common.anno;

/**
 * @author zhangyujin
 * @date 2021/11/9  4:45 下午.
 * @description
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogIndex {

    /**
     * 接口描述
     */
    String value() default "";

    /**
     * 是否打印入参日志，默认是
     */
    boolean enableReq() default true;

    /**
     * 是否打印出参日志，默认是
     */
    boolean enableRes() default true;

    /**
     * 预估时间（毫秒）
     */
    int estimateTime() default -1;

}
