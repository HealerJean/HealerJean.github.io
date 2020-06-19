// package com.healerjean.proj.initdata;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.BeansException;
// import org.springframework.beans.factory.config.BeanPostProcessor;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author HealerJean
//  * @ClassName CustomBeanPostProcessor
//  * @date 2020/6/17  14:51.
//  * @Description
//  */
// @Component
// @Slf4j
// public class CustomBeanPostProcessor implements BeanPostProcessor {
//
//     @Override
//     public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//         log.info("CustomBeanPostProcessor--------postProcessBeforeInitialization");
//         return bean;
//     }
//
//     @Override
//     public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//         log.info("CustomBeanPostProcessor--------postProcessAfterInitialization");
//         return bean;
//     }
// }
