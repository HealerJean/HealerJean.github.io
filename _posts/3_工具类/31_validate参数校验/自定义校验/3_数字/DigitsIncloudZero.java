package com.fintech.confin.web.utils.validate.anno;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @ClassName DigitsIncloudZero
 * @Author TD
 * @Date 2019/7/9 10:39
 * @Description 校验数字整数位和小数位，小数位为0时可接受
 */
@Constraint(validatedBy = {})
@Documented
@Target( {  ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DigitsIncloudZero {

    String message() default "{javax.validation.constraints.Digits.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};

    int integer();

    int fraction();
}
