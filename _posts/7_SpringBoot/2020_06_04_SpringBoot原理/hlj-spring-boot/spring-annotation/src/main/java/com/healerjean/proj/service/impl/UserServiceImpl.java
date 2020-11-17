package com.healerjean.proj.service.impl;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.OtherService;
import com.healerjean.proj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService, BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, BeanPostProcessor {

    @Autowired
    private OtherService otherService;

    public UserServiceImpl(OtherService otherService) {
        log.info("=====UserServiceImpl===");
        this.otherService = otherService;
    }

    @Override
    public UserDTO login(Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName("HealerJean");
        userDTO.setPassword("123456");
        log.info("UserServiceImpl--------{}", userDTO);
        return userDTO;
    }


    @Override
    public void setBeanName(String name) {
        log.info("=====setBeanName===");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        log.info("=====setBeanClassLoader===");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("=====setBeanFactory===");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("=====afterPropertiesSet===");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("=====applicationContext===");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userServiceImpl")) {
            log.info("=====postProcessBeforeInitialization==={}", beanName);
        }
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userServiceImpl")) {
            log.info("=====postProcessBeforeInitialization==={}", beanName);
        }
        return bean;
    }


}

