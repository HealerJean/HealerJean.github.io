package com.healerjean.proj.annotation.condition;

import com.healerjean.proj.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author HealerJean
 * @ClassName EncryptConditional
 * @date 2020/6/9  17:01.
 * @Description
 */
@Slf4j
public class EncryptCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        String property = conditionContext.getEnvironment().getProperty("var.encrypt");
        String de = conditionContext.getEnvironment().getProperty("var.demo");

        String encrypt = PropertiesUtil.getProperty("var.encrypt");
        log.debug("服务启动--------数据加解密：{}", encrypt);
        if (Boolean.valueOf(encrypt)) {
            return true;
        }
        return false;
    }
}
