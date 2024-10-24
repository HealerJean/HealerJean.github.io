package com.healerjean.proj.validate.anno;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author HealerJean
 * @ClassName PojoIdExist
 * @date 2019-10-17  22:50.
 * @Description
 */

@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PojoIdExist {

    String message(); //报错返回的信息

    Class<?>[] groups() default {}; //被哪个组校验

    String repository(); //自己定义的

    Class<? extends Payload>[] payload() default {};
}
