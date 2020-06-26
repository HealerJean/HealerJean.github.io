package com.healerjean.proj.config.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author HealerJean
 * @ClassName ExceptionFilter
 * @date 2020/6/24  17:26.
 * @Description dubbo 统一异常处理
 */
@Activate(group = Constants.PROVIDER, order = 2)
@Slf4j
public class ProviderExceptionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Class returnType = method.getReturnType();
            if (result.hasException()) {
                Throwable throwable = result.getException();
                if (throwable instanceof  ArithmeticException){
                    log.error("系统错误--------", throwable);
                }
                if (returnType.equals(String.class)) {
                    return new RpcResult("系统错误");
                }
            }
        } catch (NoSuchMethodException e) {
            log.error("统一异常处理--------出现异常", e);
        }
        return result;
    }
}
