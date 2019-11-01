package com.healerjean.proj.utils.validate.validator;

import com.healerjean.proj.api.system.DictionaryService;
import com.healerjean.proj.utils.SpringHelper;
import com.healerjean.proj.validate.anno.DictionaryProvince;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName DictionaryProvinceValidator
 * @Author HealerJean
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
        DictionaryService dictionaryService = SpringHelper.getBean(DictionaryService.class);
        return dictionaryService != null && dictionaryService.isProvinceExist(value);
    }
}
