package com.hlj.proj.utils.validate.validator;

import com.hlj.proj.validate.anno.ValidInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * ValidIntegervalidator
 *
 * @author zhangyujin
 * @date 2026/1/8
 */
@Slf4j
public class ValidIntegerValidator implements ConstraintValidator<ValidInteger, String> {

    /**
     * isValid
     *
     * @param value value
     * @param context context
     * @return {@link boolean}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }
}