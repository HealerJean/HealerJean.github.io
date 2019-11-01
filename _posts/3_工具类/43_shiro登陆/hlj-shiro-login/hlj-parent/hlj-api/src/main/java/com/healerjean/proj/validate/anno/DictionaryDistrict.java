package com.healerjean.proj.validate.anno;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @ClassName CheckFgwMoney
 * @Author DYB
 * @Date 2018/12/3 12:00
 * @Description 校验区/县
 * @Version V1.0
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryDistrict {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
