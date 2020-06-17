package com.healerjean.proj.aspect;

import com.healerjean.annotation.InterfaceName;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@Order(1)
public class ControllerLogAspect {

    private static final String REQ_UID = "REQ_UID";


    @Around("execution(* com.healerjean.proj.controller.*Controller.*(..))")
    public Object handleControllerLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = proceedingJoinPoint.getArgs();

        String value = "";
        Method method = ((MethodSignature) signature).getMethod();
        if (method.isAnnotationPresent(InterfaceName.class)) {
            value = "请求接口：【" + method.getAnnotation(InterfaceName.class).value() + "】，";
        }

        long start = System.currentTimeMillis();
        try {
            log.info("请求开始：{}类名：【{}】，方法名:【{}】, 参数:【{}】", value, className, methodName, args);
            Object result = proceedingJoinPoint.proceed();
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求结束：{}类名：【{}】， 方法名:【{}】, 参数:【{}】, 返回值:{}, 耗时:{}ms。", value, className, methodName, args, result, timeCost);
            return result;
        } catch (Exception e) {
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求出错：{}类名：【{}】，方法名:【{}】, 参数:【{}】, 耗时:【{}】ms。", value, className, methodName, args, timeCost);
            throw e;
        }
    }
}
