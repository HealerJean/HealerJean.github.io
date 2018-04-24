package com.hlj.many.datasourse.data;

import java.lang.annotation.*;

/**
 * Created by fengchuanbo on 2017/5/8.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    /**
     * 使用哪个数据源，默认itry
     * @return
     */
    DataSource value() default DataSource.ONE;

}
