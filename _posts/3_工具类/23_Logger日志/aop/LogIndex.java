package com.hlj.proj.aspect;

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
     * 是否打印入参日志，默认是
     */
    boolean reqFlag() default true;

    /**
     * 是否打印出参日志，默认是
     */
    boolean resFlag() default true;

}
