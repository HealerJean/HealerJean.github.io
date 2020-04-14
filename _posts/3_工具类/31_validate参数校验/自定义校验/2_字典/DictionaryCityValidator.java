package com.fintech.confin.web.utils.validate.validator;

import com.fintech.confin.web.service.system.dictionary.DictionaryService;
import com.fintech.confin.web.utils.spring.SpringContextHolder;
import com.fintech.confin.web.utils.validate.anno.DictionaryCity;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName DictionaryCityValidator
 * @Author TD
 * @Date 2019/5/7 17:44
 * @Description 城市检验
 */
public class DictionaryCityValidator implements ConstraintValidator<DictionaryCity, String> {
    @Override
    public void initialize(DictionaryCity constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value)){
            return true;
        }
        DictionaryService dictionaryService = SpringContextHolder.getBean(DictionaryService.class);
        return dictionaryService != null && dictionaryService.isCityExist(null, value);
    }
}
