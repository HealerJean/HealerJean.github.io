package com.healerjean.proj.mt.statemachine.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * spring相关上下文
 *
 */
@Component
public class SpringContextUtil implements ApplicationContextAware, BeanPostProcessor {

    /**
     * ApplicationContext
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;

    }

    /**
     * 根据type获取bean
     *
     * @param requiredType 类型
     * @return bean
     **/
    public static <T> T getBeanByType(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * @param: name beanName
     * @param: requiredType 类型
     * @return: bean
     **/
    public static <T> T getBeanByNameAndType(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }
}
