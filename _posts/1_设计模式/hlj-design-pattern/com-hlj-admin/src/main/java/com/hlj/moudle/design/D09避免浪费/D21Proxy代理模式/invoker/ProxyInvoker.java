package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * Invoker
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
public interface ProxyInvoker {

    /**
     * 通过给定的代理对象和方法调用来执行方法。
     */
    Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}
