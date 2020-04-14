package com.fintech.confin.web.utils.validate.anno;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * @author HealerJean
 * @ClassName DictionaryIncluded
 * @Date 2020/3/3  12:18.
 * @Description 字典校验
 */
@Constraint(validatedBy = {})
@Documented
@Target( {  ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryIncluded {

    String message() ;

    Class<?>[] groups() default { };

    String dictionaryType() ;

    Class<? extends Payload>[] payload() default { };
}
