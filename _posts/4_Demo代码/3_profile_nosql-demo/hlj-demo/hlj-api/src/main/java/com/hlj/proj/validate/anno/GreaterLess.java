package com.hlj.proj.validate.anno;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * 自定义注解 校验大于 小于 的校验
 * min <  number <  max
 */
@Constraint(validatedBy = {})  //这里可以写GreaterLessValidator.class 但是因为是我接口与实现类分离的，所以这部分业务统一写到了CustomValidatorFactoryImpl方法里面，如果不是分离的项目，这里直接写上class就可以了
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GreaterLess {

    String message(); //报错返回的信息

    Class<?>[] groups() default {}; //被哪个组校验

    String min(); //给定的数肯定大于它

    String max(); //给定的数肯定小于它

    Class<? extends Payload>[] payload() default {};
}
