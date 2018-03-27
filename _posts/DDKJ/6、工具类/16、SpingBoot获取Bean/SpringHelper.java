package com.duodian.admore.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by j.sh on 2015/4/22.
 */
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class clazz) throws BeansException {
        return (T)context.getBean(clazz);
    }

    public static <T> T getBean(String clazz) throws BeansException {
        return (T)context.getBean(clazz);
    }

    public static Object getBeanByName(String beanName) throws BeansException {
        return context.getBean(beanName);
    }

}
