package com.healerjean.proj.statemechine.config;

import java.lang.annotation.*;

/**
 * EnableStateMachine
 *
 * @author zhangyujin
 * @date 2023-06-28 11:06:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface EnableStateMachine {

    String value() default "statemachine";
}
