package com.healerjean.proj.starter.support.aop;

import com.healerjean.proj.starter.annotation.LogRecordAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @date 2023/5/30  15:09.
 */
@Slf4j
@Component
@Aspect
public class LogRecordAnnotationAspect {

    @Around(value = "(execution(* *(..)) && @annotation(logRecordAnnotation))", argNames = "pjp,logRecordAnnotation")
    public Object printLog(final ProceedingJoinPoint pjp, LogRecordAnnotation logRecordAnnotation) throws Throwable {
        return null;
    }

}

