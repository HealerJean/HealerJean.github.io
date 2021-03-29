package com.sankuai.windmill.interact.support.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/30  下午12:13.
 */
@Service
public class SpringHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public SpringHelper() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class clazz) {
        return applicationContext.getBean((Class<T>) clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return  applicationContext.getBean(name, clazz);
    }

    public static Object getBeanByName(String beanName) throws BeansException {
        return applicationContext.getBean(beanName);
    }
}
