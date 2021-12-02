package com.hlj.proj.aspect;

import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2021/11/9  5:24 下午
 * @description
 */
@Slf4j
@Component
@Aspect
@Order(2)
public class LogAspect {

    @Around(value = "(execution(* *(..)) && @annotation(logIndex))", argNames = "pjp,logIndex")
    public Object printLog(final ProceedingJoinPoint pjp, LogIndex logIndex) throws Throwable {
        Object[] args = pjp.getArgs();
        String methodName = pjp.getSignature().getName();
        Signature sig = pjp.getSignature();
        String className = sig.getDeclaringTypeName();
        long start = System.currentTimeMillis();

        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
            long timeCost = System.currentTimeMillis() - start;
            Map<String, Object> map = new HashMap<>();
            map.put("class", className);
            map.put("method", className + "." + methodName);
            map.put("requestParams", args);
            map.put("responseParams", result);
            map.put("timeCost", timeCost + "ms");
            log.info("LogAspect：{}", JsonUtils.toJsonString(map));
        }
        return result;
    }


}
