package com.healerjean.proj.config.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.healerjean.proj.util.limit.RateLimiterPropertyUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * RateLimiterAspect
 *
 * @author zhangyujin
 * @date 2022/9/8  21:44.
 */
@Component
@Aspect
public class RateLimiterAspect {
    /**
     * log
     */
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    /**
     *
     */
    private RateLimiterAspect() {
    }


    /**
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.healerjean.proj.controller.*Controller.*(..))")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Class<?> clazz = pjp.getTarget().getClass();
        log.info("开始拦截:" + clazz.getName());

        try {
            this.limit(clazz.getName());
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5);
        }

        return pjp.proceed(args);
    }

    /**
     *
     * @param className
     */
    public void limit(String className) {
        RateLimiter rateLimiter = RateLimiterPropertyUtil.getMap().get(className);
        if (null != rateLimiter) {
            log.info("开始获取令牌:" + className + " rate:" + rateLimiter.getRate());
            double waitTime = rateLimiter.acquire();
            log.info(className + ":获取成功 waitTime :" + waitTime + "ms");
        }
    }
}
