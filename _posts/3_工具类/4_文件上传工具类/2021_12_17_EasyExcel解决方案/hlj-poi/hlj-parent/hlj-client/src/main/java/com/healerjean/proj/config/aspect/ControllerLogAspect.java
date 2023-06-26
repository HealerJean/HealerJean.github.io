package com.healerjean.proj.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

    @Around("execution(* com.healerjean.proj.controller.*Controller.*(..))")
    public Object handleControllerLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) ra;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        Signature signature = proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = proceedingJoinPoint.getArgs();
        log.info("请求开始，路径:【{}】, 类名：【{}】，方法名:【{}】, 参数:【{}】", request.getRequestURI(), className, methodName, args);
        long start = System.currentTimeMillis();
        try {
            Object result = proceedingJoinPoint.proceed();
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求结束 ，路径:【{}】,类名：【{}】， 方法名:【{}】, 参数:【{}】, 返回值:{}, 耗时:{}ms", request.getRequestURI(), className, methodName, args, result, timeCost);
            return result;
        } catch (Exception e) {
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求出错 ，路径:【{}】, 类名：【{}】，方法名:【{}】, 参数:【{}】, 耗时:{}ms", request.getRequestURI(), className, methodName, args, timeCost);
            throw e;
        }
    }
}
