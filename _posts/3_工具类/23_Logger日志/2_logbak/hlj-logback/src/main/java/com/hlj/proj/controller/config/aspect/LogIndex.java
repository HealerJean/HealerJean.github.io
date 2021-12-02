package com.hlj.proj.controller.config.aspect;

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

}
