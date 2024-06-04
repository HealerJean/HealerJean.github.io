package com.healerjean.proj.common.anno;

import java.lang.annotation.*;


/**
 * Es 索引库名
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EsIndex {

    /**
     * 中文方法名
     */
    String value();

}