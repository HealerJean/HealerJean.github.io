package com.hlj.config;

import com.hlj.vialidate.ValidateUtils;
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
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }

    @Bean
    public static Validator validator() {
        return ValidateUtils.validator;
    }
}
