package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DynamicInvoker
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
public class DefaultProxyInvoker extends AbstractProxyInvoker implements ProxyInvoker {


    /**
     * JdkDynamicProxyHandler
     *
     * @param object object
     */
    public DefaultProxyInvoker(Object object) {
        super(object);
    }

    /**
     * 可以在这里做切面
     *
     * @param proxy  proxy
     * @param method method
     * @param args   args
     * @return {@link Object}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(getObject(), args);
    }


}

