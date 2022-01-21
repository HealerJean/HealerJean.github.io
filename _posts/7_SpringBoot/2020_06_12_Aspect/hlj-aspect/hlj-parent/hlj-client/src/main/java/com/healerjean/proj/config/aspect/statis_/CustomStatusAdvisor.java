package com.healerjean.proj.config.aspect.statis_;

import lombok.Setter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:06 下午.
 * @description
 */
@Setter
public class CustomStatusAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private Pointcut customStatusPointcut;

    @Override
    public Pointcut getPointcut() {
        return customStatusPointcut;
    }
}
