package com.hlj.proj.service;


import com.hlj.proj.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * InvokeUtils
 *
 * @author healerjean
 * @date 2021/6/23  9:21 下午.
 */
public class InvokeUtils {


    /**
     * call
     *
     * @param method   method
     * @param req      req
     * @param function function
     * @param logger   logger
     * @return {@link Res}
     */
    public static <Req, Res> Res call(String method, Req req, Function<Req, Res> function, Logger logger) {
        return call(method, req, function, logger, null);
    }


    /**
     * call
     *
     * @param methodName methodName
     * @param req        req
     * @param function   function
     * @param logger     logger
     * @param level      level
     * @return {@link Res}
     */
    public static <Req, Res> Res call(String methodName, Req req, Function<Req, Res> function, Logger logger, LogLevel level) {
        Res res = null;
        String stackTrace = null;
        long startTime = System.currentTimeMillis();
        try {
            res = function.apply(req);
            level = level != null ? level : LogLevel.INFO;
        } catch (Exception throwable) {
            stackTrace = ExceptionUtils.getStackTrace(throwable);
            level = level != null ? level : LogLevel.ERROR;
        } finally {
            log(methodName, req, res, stackTrace, startTime, logger, level);
        }
        return res;
    }


    /**
     * log
     *
     * @param method        method
     * @param requestParam  requestParam
     * @param responseParam responseParam
     * @param stackTrace    stackTrace
     * @param startTime     startTime
     * @param log           log
     * @param level         level
     */
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
                    log.debug("InvokeUtils:{}", JsonUtils.toJsonString(map));
                    break;
                case INFO:
                    log.info("InvokeUtils:{}", JsonUtils.toJsonString(map));
                    break;
                case WARN:
                    log.warn("InvokeUtils:{}", JsonUtils.toJsonString(map));
                    break;
                case ERROR:
                    log.error("InvokeUtils:{}", JsonUtils.toJsonString(map));
                    break;
                default:
                    log.info("InvokeUtils:{}", JsonUtils.toJsonString(map));
                    break;
            }
        } catch (Exception e) {
            log.error("InvokeUtils {}", ExceptionUtils.getStackTrace(e));
        }
    }
}

