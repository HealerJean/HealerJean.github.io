package com.hlj.proj.aspect.business;

import java.lang.annotation.*;

/**
 * @author zhangyujin
 * @date 2022/1/17  6:52 下午.
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecordAnnotation {

    /**
     * 操作日志
     */
    String content() default "";

    /**
     * 操作日志的执行人
     */
    String operator() default "";

    /**
     * 操作日志绑定的业务对象标识
     */
    String bizNo();

    /**
     * 操作日志的种类
     */
    String category() default "";

    /**
     * 扩展参数，记录操作日志的修改详情
     */
    String detail() default "";

    /**
     * 记录日志的条件
     */
    String condition() default "";
}