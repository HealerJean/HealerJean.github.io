package com.healerjean.proj.utils.validate.validator;

import com.healerjean.proj.data.repository.system.SysDictionaryDataRepository;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.utils.SpringHelper;
import com.healerjean.proj.validate.anno.DictionaryInclude;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author HealerJean
 * @ClassName DictionaryIncludeValidator
 * @date 2019-10-17  22:52.
 * @Description
 */
public class DictionaryIncludeValidator implements ConstraintValidator<DictionaryInclude, String> {

    private String type;

    @Override
    public void initialize(DictionaryInclude constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        SysDictionaryDataRepository repository = SpringHelper.getBean(SysDictionaryDataRepository.class);
        return repository.existsByRefTypeKeyAndDataKeyAndStatus(type, value, StatusEnum.生效.code);
    }


}
