package com.hlj.moudle.design.D09避免浪费.D21Proxy代理模式.D03CGLIB代理;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @author HealerJean
 * @ClassName BuyHouseEnhancer
 * @date 2020/7/10  17:33.
 * @Description 增强类
 */
public class BuyHouseEnhancer {


    /**
     * 传入目标代理对象
     */
    public static  <T> T newEnhancer(Class<T> classT) {
        //字节码加强器：用来创建动态代理类
        Enhancer enhancer = new Enhancer();
        //代理的目标对象
        enhancer.setSuperclass(classT);
        //回调类，在代理类方法调用时会回调Callback类的intercept方法
        enhancer.setCallback(new BuyHouseMethodInterceptor());
        //创建代理类
        Object result = enhancer.create();
        return (T)result;
    }
}
