package com.healerjean.proj.api.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author HealerJean
 * @ClassName RequireDomain
 * @date 2019/10/21  19:31.
 * @Description
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireDomain {

    boolean require();

}
