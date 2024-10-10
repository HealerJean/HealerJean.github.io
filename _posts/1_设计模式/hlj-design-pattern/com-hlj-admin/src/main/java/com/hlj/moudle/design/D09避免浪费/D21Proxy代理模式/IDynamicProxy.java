package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.ProxyInvoker;

/**
 * IDynamicProxy
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
public interface IDynamicProxy {


    /**
     * 获取指定接口的代理对象
     * @param interfaceClass 要代理的接口类
     * @param proxyInvoker 用于处理方法调用的 Invoker 对象
     * @return 代理对象
     */
    <T> T acquireProxy(Class<T> interfaceClass, ProxyInvoker proxyInvoker);

}
