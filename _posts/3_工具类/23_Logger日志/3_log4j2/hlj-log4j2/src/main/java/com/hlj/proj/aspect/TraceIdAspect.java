package com.hlj.proj.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author zhangyujin
 * @date 2021/11/10  3:37 下午.
 * @description 必须在入口率先执行
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class TraceIdAspect {

    private static final String TRACE_ID_NAME = "REQ_UID";

    @Around("execution(* com.hlj.proj.controller.*Controller.*(..))")
    public Object traceIdSet(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String traceId = MDC.get(TRACE_ID_NAME);
        if (StringUtils.isBlank(traceId)) {
            MDC.put(TRACE_ID_NAME, UUID.randomUUID().toString().replace("-", ""));
        }
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            Signature sig = proceedingJoinPoint.getSignature();
            String className = sig.getDeclaringTypeName();
            String methodName = proceedingJoinPoint.getSignature().getName();
            log.error("[{}.{}] error:{} ", className, methodName, ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            MDC.remove(TRACE_ID_NAME);
        }
    }
}
