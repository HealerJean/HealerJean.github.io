package com.healerjean.annotation;


import java.lang.annotation.*;

/**
 * @author HealerJean
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceName {

    String value() default "";
}
