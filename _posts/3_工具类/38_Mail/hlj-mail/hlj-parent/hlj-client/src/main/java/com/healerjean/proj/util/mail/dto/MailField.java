package com.healerjean.proj.util.mail.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mail
 * 邮件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface MailField {
    /**
     * 映射字段名称
     *
     * @return 映射字段名称
     */
    String value() default "";
}
