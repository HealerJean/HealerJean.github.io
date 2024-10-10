package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D03CGLIB代理;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.IDynamicProxy;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.ProxyInvoker;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * CglibDynamicProxy
 *
 * @author zhangyujin
 * @date 2024-09-29 03:09:51
 */
public class CglibDynamicProxy implements IDynamicProxy {


    @Override
    public <T> T acquireProxy(Class<T> interfaceClass, ProxyInvoker proxyInvoker) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceClass);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> proxyInvoker.invoke(o, method, objects));
        @SuppressWarnings("unchecked")
        T proxy = (T) enhancer.create();
        return proxy;
    }

}
