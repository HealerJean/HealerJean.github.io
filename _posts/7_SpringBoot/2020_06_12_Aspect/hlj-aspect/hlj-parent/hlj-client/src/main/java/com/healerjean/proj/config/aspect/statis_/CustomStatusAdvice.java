package com.healerjean.proj.config.aspect.statis_;

import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


/**
 * @author zhangyujin
 * @date 2022/1/20  12:00 下午.
 * @description
 */
@Slf4j
public class  CustomStatusAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation joinPoint) throws Throwable {

        Method method = joinPoint.getMethod();
        String className = null;
        String methodName = null;
        Object[] args = joinPoint.getArguments();
        long start = System.currentTimeMillis();
        Object result = null;
        Object reqParams = null;
        try {
            methodName = method.getName();
            className = method.getDeclaringClass().getName();
            Parameter[] parameters = method.getParameters();
            reqParams = getRequestParams(args, parameters);
            result = joinPoint.proceed();
        } finally {
            long timeCost = System.currentTimeMillis() - start;
            Map<String, Object> map = new HashMap<>(8);
            map.put("method", className + "." + methodName);
            map.put("requestParams", reqParams);
            map.put("responseParams", result);
            map.put("timeCost", timeCost + "ms");
            log.info("LogAspect：{}", JsonUtils.toJsonString(map));
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
