package com.healerjean.proj.utils.lock;

import com.healerjean.proj.common.anno.InsLock;
import com.healerjean.proj.service.RedisService;
import com.healerjean.proj.utils.AspectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 根据Lock配置对方法进行加锁处理
 *
 * @author zhangyujin
 * @date 2023/5/26  11:47
 */
@Aspect
@Component
@Slf4j
public class LockAspect {
    /**
     * RedisService
     */
    @Resource
    private RedisService redisService;

    /**
     * 方法锁
     *
     * @param joinPoint 切点
     * @param insLock   锁配置
     * @return Object
     * @throws Throwable Throwable
     */
    @Around(value = "(execution(* *(..)) && @annotation(insLock))", argNames = "joinPoint,insLock")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, InsLock insLock) throws Throwable {
        if (null == insLock.value()) {
            return joinPoint.proceed();
        }
        //获取方法
        Method method = null;
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        }
        if (method == null) {
            //非方法类型
            return joinPoint.proceed();
        }
        String threadId = UUID.randomUUID().toString();
        String lockKey = AspectUtils.parseParamKey(joinPoint, method);
        String redisKey = insLock.value().join(lockKey);
        boolean lockResult = redisService.lock(redisKey, threadId, insLock.value().getExpireSec());
        if (!lockResult) {
            log.error("获取锁失败,redisKey:{}", redisKey);
        }
        try {
            return joinPoint.proceed();
        } finally {
            boolean unLockResult = redisService.unLock(redisKey, threadId);
            if (!unLockResult) {
                log.error("解锁失败,redisKey:{}", redisKey);
            }
        }
    }
}