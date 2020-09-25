package com.healerjean.proj.util.markdown;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author HealerJean
 * @date 2020/9/24  17:07.
 * @description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {

    String value();
}
