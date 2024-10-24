package com.hlj.proj.aspect;

import com.hlj.proj.config.DynamicLogConfiguration;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author zhangyujin
 * @date 2021/11/9  5:24 下午
 * @desciption 切面日志
 */
@Slf4j
@Component
@Aspect
@Order(2)
public class LogAspect {


    /**
     * dynamicLogConfiguration
     */
    @Resource
    private DynamicLogConfiguration dynamicLogConfiguration;


    @Around(value = "(execution(* *(..)) && @annotation(logIndex))", argNames = "pjp,logIndex")
    public Object printLog(final ProceedingJoinPoint pjp, LogIndex logIndex) throws Throwable {
        Object[] args = pjp.getArgs();
        Signature sig = pjp.getSignature();
        long start = System.currentTimeMillis();
        Method method = null;
        Object result;
        Object reqParams;
        Map<String, Object> map = new HashMap<>(8);
        try {
            String    methodName = pjp.getSignature().getName();
            String  className = sig.getDeclaringTypeName();
            if ((sig instanceof MethodSignature)) {
                MethodSignature signature = (MethodSignature) sig;
                Object target = pjp.getTarget();
                method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
            } else {
                log.error("signature is not instanceof MethodSignature!");
            }

            if (method == null) {
                return pjp.proceed();
            }

            String key = className + "." + methodName;
            map.put("method", key);

            Boolean reqPrintFlag = dynamicLogConfiguration.getReqPrintFlag().get(key);
            if (Objects.nonNull(reqPrintFlag) && Boolean.TRUE.equals(reqPrintFlag)){
                reqParams = getRequestParams(args, method.getParameters());
                map.put("req", reqParams);
            }
            if (Objects.isNull(reqPrintFlag) && Boolean.TRUE.equals(logIndex.reqFlag())) {
                reqParams = getRequestParams(args, method.getParameters());
                map.put("req", reqParams);
            }

            result = pjp.proceed();

            Boolean resPrintFlag = dynamicLogConfiguration.getResPrintFlag().get(key);
            if (Objects.nonNull(resPrintFlag) && Boolean.TRUE.equals(resPrintFlag)){
                map.put("res", result);
            }
            if (Objects.isNull(resPrintFlag) && Boolean.TRUE.equals(logIndex.resFlag())) {
                map.put("res", result);
            }
        } finally {
            long timeCost = System.currentTimeMillis() - start;
            map.put("timeCost", timeCost + "ms");
            log.info("LogAspect:{}", JsonUtils.toJsonString(map));
        }
        return result;
    }

    /**
     * 重构请求参数
     *
     * @param args       参数
     * @param parameters 参数名
     * @return 重构后的请求参数
     */
    public Object getRequestParams(Object[] args, Parameter[] parameters) {
        if (Objects.isNull(args)) {
            return null;
        }
        if (args.length == 1 && !(args[0] instanceof HttpServletRequest) && !(args[0] instanceof HttpServletResponse)) {
            return args[0];
        }

        List<Object> result = new ArrayList<>();
        try {
            for (int i = 0; i < args.length; i++) {
                Object param = args[i];
                if (param instanceof HttpServletRequest) {
                    result.add("HttpServletRequest");
                    continue;
                }
                if (param instanceof HttpServletResponse) {
                    result.add("HttpServletResponse");
                    continue;
                }
                Map<Object, Object> map = new HashMap<>(2);
                map.put(parameters[i].getName(), param);
                result.add(map);
            }
        } catch (Exception e) {
            log.warn("LogAspect getRequestParams error:{}", ExceptionUtils.getStackTrace(e));
        }
        return result;
    }
}
