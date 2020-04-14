package com.fintech.confin.web.utils.validate.validator;

import com.fintech.confin.web.service.system.dictionary.DictionaryService;
import com.fintech.confin.web.utils.spring.SpringContextHolder;
import com.fintech.confin.web.utils.validate.anno.DictionaryProvince;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName DictionaryProvinceValidator
 * @Author TD
 * @Date 2019/5/7 17:45
 * @Description 省份校验
 */
public class DictionaryProvinceValidator implements ConstraintValidator<DictionaryProvince, String> {


    @Override
    public void initialize(DictionaryProvince constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value)) {
            return true;
        }
        DictionaryService dictionaryService = SpringContextHolder.getBean(DictionaryService.class);
        return dictionaryService != null && dictionaryService.isProvinceExist(value);
    }
}
