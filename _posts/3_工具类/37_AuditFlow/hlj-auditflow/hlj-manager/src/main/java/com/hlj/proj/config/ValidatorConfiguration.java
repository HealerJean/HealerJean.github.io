package com.hlj.proj.config;

import com.hlj.proj.utils.validate.ValidateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

/**
 * 校验配置
 */
@Configuration
public class ValidatorConfiguration {


    @Bean
    public static Validator validator() {
        return ValidateUtils.validator;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }


}
