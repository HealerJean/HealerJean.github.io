package com.healerjean.proj.annotation.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author HealerJean
 * @ClassName EncryptConditional
 * @date 2020/6/9  17:01.
 * @Description
 */
public class EncryptCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        Environment environment = conditionContext.getEnvironment();
        String encrypt = environment.getProperty("condition.var.encrypt");
        if (Boolean.valueOf(encrypt)) {
            return true;
        }
        return false;
    }
}
