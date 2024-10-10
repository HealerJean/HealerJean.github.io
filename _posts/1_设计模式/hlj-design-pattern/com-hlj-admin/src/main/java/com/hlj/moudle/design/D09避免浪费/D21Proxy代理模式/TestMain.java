package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式;

import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D02Jdk动态代理.JdkAspectProxyInvoker;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D02Jdk动态代理.JdkDynamicProxy;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D03CGLIB代理.CglibDynamicProxy;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D04Javassist.JavassistDynamicProxy;
import com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.invoker.DefaultProxyInvoker;
import org.junit.jupiter.api.Test;

/**
 * TestMain
 *
 * @author zhangyujin
 * @date 2024-09-29 03:09:23
 */
public class TestMain {

    @Test
    public void java() throws Exception {

    }


    @Test
    public void jdk() throws Exception {
        JdkAspectProxyInvoker dynamicInvoker = new JdkAspectProxyInvoker(new BuyHouseImpl());
        JdkDynamicProxy jdkDynamicProxy = new JdkDynamicProxy();
        BuyHouse buyHouse = jdkDynamicProxy.acquireProxy(BuyHouse.class, dynamicInvoker);
        buyHouse.buyHosue();


        // byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", buyHouse.getClass().getInterfaces());
        // String code = IOUtils.toString(bytes, "utf-8");
        // System.out.println(code);
        // FileOutputStream fileOutputStream = new FileOutputStream("/Users/healerjean/Desktop/logs/porxy.class");
        // IOUtils.write(bytes, fileOutputStream);
    }


    @Test
    public void cglib() throws Exception {

        DefaultProxyInvoker dynamicInvoker = new DefaultProxyInvoker(new BuyHouseImpl());

        CglibDynamicProxy cglibDynamicProxy = new CglibDynamicProxy();
        BuyHouse buyHouse = cglibDynamicProxy.acquireProxy(BuyHouse.class, dynamicInvoker);
        buyHouse.buyHosue();
    }


    @Test
    public void javassist() throws Exception {
        DefaultProxyInvoker dynamicInvoker = new DefaultProxyInvoker(new BuyHouseImpl());

        JavassistDynamicProxy cglibDynamicProxy = new JavassistDynamicProxy();
        BuyHouse buyHouse = cglibDynamicProxy.acquireProxy(BuyHouse.class, dynamicInvoker);
        buyHouse.buyHosue();
    }


}
