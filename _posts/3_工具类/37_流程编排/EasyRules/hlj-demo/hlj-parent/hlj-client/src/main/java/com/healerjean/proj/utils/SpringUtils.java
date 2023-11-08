package com.healerjean.proj.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * SpringUtils
 *
 * @author zhangyujin
 * @date 2023/6/16  11:48
 */
@Service
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public SpringUtils() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class clazz) {
        return applicationContext.getBean((Class<T>) clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static Object getBeanByName(String beanName) throws BeansException {
        return applicationContext.getBean(beanName);
    }
}
