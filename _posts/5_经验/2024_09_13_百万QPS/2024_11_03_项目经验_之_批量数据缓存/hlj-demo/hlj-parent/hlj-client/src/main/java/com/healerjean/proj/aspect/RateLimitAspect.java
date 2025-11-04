package com.healerjean.proj.aspect;

import com.healerjean.proj.common.anno.RateLimit;
import com.healerjean.proj.utils.ratelimit.QpsRateLimitUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * RateLimitAspect
 *
 * @author zhangyujin
 * @date 2024/10/30
 */

@Aspect
@Component
@Slf4j
public class RateLimitAspect {


    /**
     * 限流
     */
    @Around(value = "(execution(* *(..)) && @annotation(rateLimit))", argNames = "pjp,rateLimit")
    public Object rateLimit(final ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        Signature sig = pjp.getSignature();
        Method method = null;
        if ((sig instanceof MethodSignature)) {
            MethodSignature signature = (MethodSignature) sig;
            Object target = pjp.getTarget();
            method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
        }
        if (method == null) {
            return pjp.proceed();
        }
         QpsRateLimitUtils.acquire(rateLimit.key());
        return pjp.proceed();
    }
}
