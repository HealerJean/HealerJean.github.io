package com.healerjean.proj.aspect;

import com.healerjean.proj.common.anno.RedisCache;
import com.healerjean.proj.data.bo.BatchFunctionBO;
import com.healerjean.proj.service.RedisService;
import com.healerjean.proj.utils.AspectUtils;
import com.healerjean.proj.utils.FunctionUtils;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * r2m缓存处理
 *
 * @author zhouyunchao
 * @date 2020/3/31
 */
@Aspect
@Component
@Slf4j
public class RedisCacheAspect {
    /**
     * 缓存工具
     */
    @Resource
    private RedisService redisService;

    /**
     * 环绕缓存
     *
     * @param joinPoint  切点
     * @param redisCache 缓存配置项
     * @return Object
     * @throws Throwable Throwable
     */
    @Around(value = "(execution(* *(..)) && @annotation(redisCache))", argNames = "joinPoint,redisCache")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        if (!redisCache.enabled() || null == redisCache.value()) {
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
        //获取redis缓存
        String paramKey = AspectUtils.parseParamKey(joinPoint, method);
        if (StringUtils.isBlank(paramKey)) {
            return joinPoint.proceed();
        }
        String r2mKey = redisCache.value().join(paramKey);
        Object proceed = null;

        BatchFunctionBO<String, String> function = BatchFunctionBO.instance();
        function.getReqBusiness().setReq(r2mKey).setName("redisService.get");
        function.setFunction(redisService::get);
        FunctionUtils.invokeFunction(function);
        if (Boolean.TRUE.equals(function.getResBusiness().getInvokeFlag())) {
            return JsonUtils.toObject(function.getResBusiness().getRes(), method.getGenericReturnType());
        }

        try {
            proceed = joinPoint.proceed();
        } finally {
            if (Objects.nonNull(proceed)) {
                redisService.set(r2mKey, JsonUtils.toString(proceed), redisCache.value().getExpireSec());
            }
        }
        return proceed;
    }





}
