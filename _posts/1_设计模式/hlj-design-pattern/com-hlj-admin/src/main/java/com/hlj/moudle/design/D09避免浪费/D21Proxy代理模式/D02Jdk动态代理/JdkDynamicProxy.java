package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D02Jdk动态代理;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.IDynamicProxy;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.ProxyInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * JdkDynamicProxy
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
public class JdkDynamicProxy implements IDynamicProxy {

    /**
     * acquireProxy
     *
     * @param interfaceClass interfaceClass
     * @param proxyInvoker invoker
     * @return {@link T}
     */
    @Override
    public <T> T acquireProxy(Class<T> interfaceClass, ProxyInvoker proxyInvoker) {
        ClassLoader classLoader = interfaceClass.getClassLoader();
        InvocationHandler invocationHandler = proxyInvoker::invoke;
        @SuppressWarnings("unchecked")
        T result = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, invocationHandler);
        return result;
    }

}
