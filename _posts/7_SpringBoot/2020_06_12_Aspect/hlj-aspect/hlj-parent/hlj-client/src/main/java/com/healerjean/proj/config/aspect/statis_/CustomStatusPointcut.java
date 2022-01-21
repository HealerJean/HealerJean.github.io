package com.healerjean.proj.config.aspect.statis_;

import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author zhangyujin
 * @date 2022/1/20  11:49 上午.
 * @description 静态切点
 */
public class CustomStatusPointcut extends StaticMethodMatcherPointcutAdvisor {

    /**
     * 切点方法 匹配
     * 匹配规则： 默认情况下，匹配所有的类
     */
    @Override
    public boolean matches(Method method, Class<?> clazz) {
        return Optional.ofNullable(clazz.getName()).map(item -> item.contains("Controller")).orElse(false);
    }

}
