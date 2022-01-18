package com.hlj.proj.aspect.business;

import com.hlj.proj.utils.SpelUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @date 2022/1/17  7:03 下午.
 * @description
 */
@Slf4j
@Component
@Aspect
public class LogRecordAspect {
    private static final Logger BUSINESS_LOG = LoggerFactory.getLogger("businessLog");

    @Around(value = "(execution(* *(..)) && @annotation(logRecordAnnotation))", argNames = "pjp,logRecordAnnotation")
    public Object printLog(final ProceedingJoinPoint pjp, LogRecordAnnotation logRecordAnnotation) throws Throwable {
        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
        }
        String operator = SpelUtil.generateKeyBySpEL(logRecordAnnotation.operator(), pjp);
        String bizNo = SpelUtil.generateKeyBySpEL(logRecordAnnotation.bizNo(), pjp);
        String content = logRecordAnnotation.content();
        BUSINESS_LOG.info("operator:{}", operator);
        BUSINESS_LOG.info("bizNo:{}", bizNo);
        BUSINESS_LOG.info("content:{}", content);
        return result;
    }


}