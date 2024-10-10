package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D04Javassist;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.IDynamicProxy;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.ProxyInvoker;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * JavassistDynamicProxy
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
public class JavassistDynamicProxy implements IDynamicProxy {

    /**
     * acquireProxy
     *
     * @param interfaceClass interfaceClass
     * @param proxyInvoker invoker
     * @return {@link T}
     */
    @Override
    public <T> T acquireProxy(Class<T> interfaceClass, ProxyInvoker proxyInvoker) {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(new Class[]{interfaceClass});
        MethodHandler methodHandler = (self, thisMethod, proceed, args) -> proxyInvoker.invoke(self, thisMethod, args);
        try {
            @SuppressWarnings("unchecked")
            T proxy = (T) factory.create(new Class[0], new Object[0], methodHandler);
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
