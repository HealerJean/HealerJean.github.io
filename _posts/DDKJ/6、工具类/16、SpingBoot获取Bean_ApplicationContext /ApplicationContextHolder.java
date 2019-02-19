package com.duodian.youhui.admin.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/2/19  上午10:37.
 * 类描述：
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware, BeanPostProcessor {

    /**
     * Spring上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * @return
     * @Description: 获取 ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param <T>
     * @param name
     * @return
     * @Description: 获取Bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * @param <T>
     * @param name
     * @return
     * @Description: 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    /**
     * @param <T>
     * @param name
     * @return
     * @Description: 获取Bean
     */
    public static <T> T getBean(Class<T> clazz, String name) {
        return (T) applicationContext.getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Override
    public Object postProcessAfterInitialization(Object arg0, String arg1)
            throws BeansException {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public Object postProcessBeforeInitialization(Object arg0, String arg1)
            throws BeansException {
        // TODO Auto-generated method stub
        return arg0;
    }

}

