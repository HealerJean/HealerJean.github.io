package com.healerjean.proj.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

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
        // String className = signature.getDeclaringTypeName(); //类名
        // String methodName = signature.getName(); //方法名
        //方法全名
        Method method = ((MethodSignature) signature).getMethod();
        //参数
        Object[] args = proceedingJoinPoint.getArgs();
        log.info("请求开始，路径:【{}】, 方法名:【{}】, 参数:【{}】", request.getRequestURI(), signature, args);
        long start = System.currentTimeMillis();
        try {
            Object result = proceedingJoinPoint.proceed();
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求结束 ，路径:【{}】, 方法名:【{}】, 参数:【{}】, 返回值:{}, 耗时:{}ms", request.getRequestURI(), method, args, result, timeCost);
            return result;
        } catch (Exception e) {
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求出错 ，路径:【{}】, 方法名:【{}】, 参数:【{}】, 耗时:{}ms", request.getRequestURI(), method, args, timeCost);
            throw e;
        }
    }
}
