package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D02Jdk动态代理;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.AbstractProxyInvoker;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.ProxyInvoker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JdkAspectProxyInvoker
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
@Slf4j
public class JdkAspectProxyInvoker extends AbstractProxyInvoker {

    /**
     * JdkAspectProxyInvoker
     *
     * @param object object
     */
    public JdkAspectProxyInvoker(Object object) {
        super(object);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        log.info("JdkAspectProxyInvoker#invoke before");
        Object result = method.invoke(this.getObject(), args);
        log.info("JdkAspectProxyInvoker#invoke end");
        return result;
    }
}
