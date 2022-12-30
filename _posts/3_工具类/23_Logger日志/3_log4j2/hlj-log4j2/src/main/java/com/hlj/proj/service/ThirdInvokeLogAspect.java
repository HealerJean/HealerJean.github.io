package com.hlj.proj.service;

/**
 * @author zhangyujin
 * @date 2021/11/10  10:40 上午.
 * @description
 */

import com.hlj.proj.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author zhangyujin
 * @date 2021/6/23  9:21 下午.
 * @description
 */
public class ThirdInvokeLogAspect {


    public static <Req, Res> Res call(String method, Req req, Callable<Res> callable, Logger logger) {
        return call(method, req, callable, logger, null);
    }


    public static <Req, Res> Res call(String methodName, Req req, Callable<Res> callable, Logger logger, LogLevel level) {
        Res res = null;
        String stackTrace = null;
        long startTime = System.currentTimeMillis();
        try {
            res = callable.call();
            level = level != null ? level : LogLevel.INFO;
        } catch (Exception throwable) {
            stackTrace = ExceptionUtils.getStackTrace(throwable);
            level = level != null ? level : LogLevel.ERROR;
        } finally {
            log(methodName, req, res, stackTrace, startTime, logger, level);
        }
        return res;
    }


    private static void log(String method, Object requestParam, Object responseParam, String stackTrace, long startTime, Logger log, LogLevel level) {
        try {
            long timeCost = System.currentTimeMillis() - startTime;
            Map<String, Object> map = new HashMap<>();
            map.put("method", method);
            map.put("requestParams", requestParam);
            map.put("responseParams", responseParam);
            map.put("timeCost", timeCost + "ms");
            if (StringUtils.isNotBlank(stackTrace)) {
                map.put("stackTrace", stackTrace);
            }
            switch (level) {
                case DEBUG:
                    log.debug("ThirdInvokeLogAspect:{}", JsonUtils.toJsonString(map));
                    break;
                case INFO:
                    log.info("ThirdInvokeLogAspect:{}", JsonUtils.toJsonString(map));
                    break;
                case WARN:
                    log.warn("ThirdInvokeLogAspect:{}", JsonUtils.toJsonString(map));
                    break;
                case ERROR:
                    log.error("ThirdInvokeLogAspect:{}", JsonUtils.toJsonString(map));
                    break;
            }
        } catch (Exception e) {
            log.error("log2Hive error {}", ExceptionUtils.getStackTrace(e));
        }
    }
}

