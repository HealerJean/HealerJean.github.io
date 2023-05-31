package com.healerjean.proj.starter.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecordAnnotation {

    /**
     * 操作成功的的文本模版
     */
    String success();

    /**
     * 操作失败的的文本模版
     */
    String fail() default "";

    /**
     * 操作人
     */
    String operator() default "";

    /**
     * todo
     */
    String prefix();

    /**
     * 业务编号
     */
    String bizNo();

    /**
     * 日志分类
     */
    String category() default "";

    /**
     * 扩展参数，记录日志的详情数据
     */
    String detail() default "";

    /**
     * 记录日志的条件
     */
    String condition() default "";
}
