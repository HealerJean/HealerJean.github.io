package com.healerjean.proj.utils.validate;

import com.healerjean.proj.utils.validate.validator.anno.CollectionIncluded;
import com.healerjean.proj.utils.validate.validator.CollectionIncludedValidator;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * ValidateUtils- 校验工具类
 *
 * @author zhangyujin
 * @date 2023/4/21  11:27
 */
public class ValidateUtils {


    /**
     * 校验成功标识常量（系统内部使用）
     */
    public static final String COMMON_SUCCESS = "success";

    /**
     * 校验工具类
     */
    public static Validator validator;

    static {
        HibernateValidatorConfiguration configure = Validation
                .byProvider(HibernateValidator.class)
                .configure();

        DefaultConstraintMapping collectionIncludedConstraintMapping = new DefaultConstraintMapping();
        collectionIncludedConstraintMapping.constraintDefinition(CollectionIncluded.class)
                .validatedBy(CollectionIncludedValidator.class);
        configure.addMapping(collectionIncludedConstraintMapping);


        //快速返回模式，有一个验证失败立即返回错误信息
        validator = configure
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }


    /**
     * 静态方法校验使用的
     *
     * @param object object
     * @return String
     */
    public static String validate(Object object) {
        Set<ConstraintViolation<Object>> validate = validator.validate(object);
        return resultValidate(validate);
    }

    /**
     * 静态方法校验使用，并且带分组的
     *
     * @param object object
     * @param group  group
     * @return String
     */
    public static String validate(Object object, Class<?> group) {
        if (object == null) {
            return "校验对象为空";
        }
        if (group == null) {
            return validate(object);
        } else {
            Set<ConstraintViolation<Object>> validate = validator.validate(object);
            if (validate != null && !validate.isEmpty()) {
                return resultValidate(validate);
            }

            Set<ConstraintViolation<Object>> validateGroup = validator.validate(object, group);
            return resultValidate(validateGroup);
        }
    }


    /**
     * resultValidate
     *
     * @param validate validate
     * @return String
     */
    private static String resultValidate(Set<ConstraintViolation<Object>> validate) {
        if (!validate.isEmpty()) {
            final StringBuffer stringBuffer = new StringBuffer();
            validate.forEach(
                    item -> stringBuffer.append(item.getMessage()).append(","));
            stringBuffer.setLength(stringBuffer.length() - 1);
            return stringBuffer.toString();
        }
        return COMMON_SUCCESS;
    }
}
