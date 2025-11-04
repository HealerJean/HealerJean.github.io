package com.healerjean.proj.utils.validate.validator;


import com.healerjean.proj.utils.validate.validator.anno.CollectionIncluded;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 校验数据是否存在集合中
 *
 * @author zhangyujin
 * @date 2023/4/21  11:19
 */
public class CollectionIncludedValidator implements ConstraintValidator<CollectionIncluded, Object> {

    /**
     * collections
     */
    private Set<String> collections;

    /**
     * initialize
     *
     * @param constraintAnnotation constraintAnnotation
     */
    @Override
    public void initialize(CollectionIncluded constraintAnnotation) {
        collections = Arrays.stream(constraintAnnotation.collections().split(",")).collect(Collectors.toSet());
    }

    /**
     * isValid
     *
     * @param context context
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return collections.contains(value.toString());
    }
}
