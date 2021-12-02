package com.hlj.proj.controller.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
        } finally {
            MDC.remove(TRACE_ID_NAME);
        }
    }
}
