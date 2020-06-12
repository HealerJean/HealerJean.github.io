package com.fintech.pub.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Aspect
@Component
@Slf4j
@Order(1)
public class Log4jAspect {

    private static final String REQ_UID = "REQ_UID";


    @Around("execution(* com.fintech.dubbo.service.*Service.*(..))")
    public Object handleControllerLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MDC.put(REQ_UID, UUID.randomUUID().toString().replace("-", ""));

        Signature signature = proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = proceedingJoinPoint.getArgs();
        log.info("请求开始, 类名：【{}】，方法名:【{}】, 参数:【{}】", className, methodName, args);
        long start = System.currentTimeMillis();
        try {
            Object result = proceedingJoinPoint.proceed();
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求结束，类名：【{}】， 方法名:【{}】, 参数:【{}】, 返回值:{}, 耗时:{}ms", className, methodName, args, result, timeCost);
            return result;
        } catch (Exception e) {
            long timeCost = System.currentTimeMillis() - start;
            log.info("请求出错，类名：【{}】，方法名:【{}】, 参数:【{}】, 耗时:{}ms", className, methodName, args, timeCost);
            throw e;
        } finally {
            MDC.remove(REQ_UID);
        }
    }
}
