package com.fintech.confin.web.utils.validate.validator;

import com.fintech.confin.web.service.system.dictionary.DictionaryService;
import com.fintech.confin.web.utils.spring.SpringContextHolder;
import com.fintech.confin.web.utils.validate.anno.DictionaryIncluded;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName DictionaryIncludValidator
 * @Author TD
 * @Date 2019/5/7 16:48CreditBillController
 * @Description 校验数据是否存在与字典表中
 */
@Slf4j
public class DictionaryIncludedValidator implements ConstraintValidator<DictionaryIncluded, String> {

    private String dictionaryType;

    @Override
    public void initialize(DictionaryIncluded constraintAnnotation) {
        dictionaryType = constraintAnnotation.dictionaryType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value)) {
            return true;
        }
        DictionaryService dictionaryService = SpringContextHolder.getBean(DictionaryService.class);
        log.info("校验字典项：type: {}，data:{}",dictionaryType,value);
        return dictionaryService != null && dictionaryService.judgeDictDataExist(dictionaryType, value);
    }
}
