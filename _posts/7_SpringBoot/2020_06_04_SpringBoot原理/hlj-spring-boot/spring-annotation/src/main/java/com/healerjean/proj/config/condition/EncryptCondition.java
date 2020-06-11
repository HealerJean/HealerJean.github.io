package com.healerjean.proj.config.condition;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class EncryptCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        Environment environment = conditionContext.getEnvironment();
        String encryptStr = environment.getProperty("database.encrypt");
        log.debug("服务启动--------数据加解密：{}", encryptStr);
        if (StringUtils.isNotBlank(encryptStr) && encryptStr.equals("true")) {
            return true;
        }
        return false;
    }
}
