package com.healerjean.proj.utils.validate.validator;

import com.healerjean.proj.api.system.DictionaryService;
import com.healerjean.proj.utils.SpringHelper;
import com.healerjean.proj.validate.anno.DictionaryDistrict;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName DictionaryTownValidator
 * @Author TD
 * @Date 2019/5/7 17:44
 * @Description 区县校验
 */
public class DictionaryDistrictValidator implements ConstraintValidator<DictionaryDistrict, String> {

    @Override
    public void initialize(DictionaryDistrict constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        DictionaryService dictionaryService = SpringHelper.getBean(DictionaryService.class);
        return dictionaryService != null && dictionaryService.isDistrictExist(null, null, value);
    }
}
