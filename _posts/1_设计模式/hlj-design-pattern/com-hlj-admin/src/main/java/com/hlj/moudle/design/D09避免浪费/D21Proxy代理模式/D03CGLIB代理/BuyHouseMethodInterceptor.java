package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D03CGLIB代理;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author HealerJean
 * @ClassName CglibProxy
 * @date 2019/8/21  14:28.
 * @Description
 */
public class BuyHouseMethodInterceptor implements MethodInterceptor {


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //调用目标类（父类）的方法
        Object result = proxy.invokeSuper(obj, args);
        return result;
    }

}
