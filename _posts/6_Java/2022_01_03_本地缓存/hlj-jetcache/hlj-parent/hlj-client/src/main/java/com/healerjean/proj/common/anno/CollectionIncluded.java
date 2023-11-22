package com.healerjean.proj.common.anno;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 集合包含校验
 *
 * @author zhangyujin
 * @date 2023/6/14  20:56.
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CollectionIncluded {

    String message();

    Class<?>[] groups() default {};

    String collections();

    Class<? extends Payload>[] payload() default {};
}