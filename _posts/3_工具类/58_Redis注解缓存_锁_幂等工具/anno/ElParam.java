package com.healerjean.proj.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ElParam
 * EL参数配置，配置了该注解的参数，会读取对应的表达式值
 *
 * @date 2023/4/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ElParam {
    /**
     * el表达式
     * 1、如果是对象类型，需要用对象field作为key,则表达式为#，如 #student.cardId；
     * 1.1 获取多个字段值使用 #student.cardId+'-'+#student.name； #student.cardId+#student.name
     * 1.2 '' 标识字符，不会转换
     * 1.3 变量，字符之间用+号标识链接
     * 2、如果是对象类型，不指定value值，会使用对象.toString作为key，不建议使用
     * 3、如果是基本类型，int,string等。可不指定value值
     *
     * @return String
     */
    String value() default "";
}
