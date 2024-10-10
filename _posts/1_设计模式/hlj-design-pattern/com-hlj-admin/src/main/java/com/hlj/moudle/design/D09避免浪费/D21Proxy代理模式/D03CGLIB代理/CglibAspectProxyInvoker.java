package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D03CGLIB代理;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.AbstractProxyInvoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * CglibAspectProxyInvoker
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
@Slf4j
public class CglibAspectProxyInvoker extends AbstractProxyInvoker {


    /**
     * JdkAspectProxyInvoker
     *
     * @param object object
     */
    public CglibAspectProxyInvoker(Object object) {
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
