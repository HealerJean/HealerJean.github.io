package com.hlj.proj.validate.anno;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * ValidInteger
 *
 * @author zhangyujin
 * @date 2026/1/8
 */
@Constraint(
        validatedBy = {}
)
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInteger {


    String message() default "必须是有效的整数（-2147483648 ～ 2147483647）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}