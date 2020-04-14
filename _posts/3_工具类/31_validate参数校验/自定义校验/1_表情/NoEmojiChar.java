package com.fintech.confin.web.utils.validate.anno;


import com.fintech.confin.web.utils.validate.validator.NoEmojiCharValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @ClassName NoEmojiChar
 * @Author DYB
 * @Date 2020/4/14 9:56
 * @Description 禁止输入emoji表情
 * @Version V1.0
 */
@Constraint(validatedBy = {NoEmojiCharValidator.class})
@Documented
@Target( {  ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoEmojiChar {

    String message() ;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
