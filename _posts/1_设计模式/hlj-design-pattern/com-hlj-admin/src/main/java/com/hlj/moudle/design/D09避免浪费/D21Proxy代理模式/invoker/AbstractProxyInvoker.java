package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker;

import lombok.Getter;

/**
 * DynamicInvoker
 *
 * @author zhangyujin
 * @date 2024/9/29
 */
@Getter
public abstract class AbstractProxyInvoker implements ProxyInvoker {

    /**
     * object
     */
    private final Object object;

    /**
     * JdkDynamicProxyHandler
     *
     * @param object object
     */
    public AbstractProxyInvoker(final Object object) {
        this.object = object;
    }

}

