package com.hlj.proj.service.log;

import com.hlj.proj.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;

import java.util.concurrent.Callable;

/**
 * @author zhangyujin
 * @date 2021/6/23  9:21 下午.
 * @description
 */
public class ThirdInvokeLogAspect {


    public static <Req, Res> Res call(String methodName, Req req, Callable<Res> callable, Logger logger) {
        return call(methodName, req, callable, logger, null);
    }


    public static <Req, Res> Res call(String methodName, Req req, Callable<Res> callable, Logger logger, LogLevel level) {
        Res res = null;
        String errorMessage = null;
        long startTime = System.currentTimeMillis();
        try {
            res = callable.call();
            level = level != null ? level : LogLevel.INFO;
        } catch (Exception throwable) {
            errorMessage = ExceptionUtils.getStackTrace(throwable);
            level = level != null ? level : LogLevel.ERROR;
        } finally {
            long endTime = System.currentTimeMillis();
            log(methodName, JsonUtils.toJsonString(req), JsonUtils.toJsonString(res), errorMessage, startTime, endTime, logger, level);
        }
        return res;
    }


    private static void log(String methodName, String requestParam, String responseParam, String errorMessage, long startTime, long endTime, Logger log, LogLevel level) {
        try {
            switch (level) {
                case DEBUG:
                    if (StringUtils.isBlank(errorMessage)) {
                        log.debug("methodName:[{}],requestParam:{},responseParam:{},startTime:{},endTime:{}", methodName, requestParam, responseParam, startTime, endTime);
                    } else {
                        log.debug("methodName:[{}] error,requestParam:{},responseParam:{},startTime:{},endTime:{},stackTrace:{}", methodName, requestParam, responseParam, startTime, endTime, errorMessage);
                    }
                    break;
                case INFO:
                    if (StringUtils.isBlank(errorMessage)) {
                        log.info("methodName:[{}],requestParam:{},responseParam:{},startTime:{},endTime:{}", methodName, requestParam, responseParam, startTime, endTime);
                    } else {
                        log.info("methodName:[{}] error,requestParam:{},responseParam:{},startTime:{},endTime:{},stackTrace:{}", methodName, requestParam, responseParam, startTime, endTime, errorMessage);
                    }
                    break;
                case WARN:
                    if (StringUtils.isBlank(errorMessage)) {
                        log.warn("methodName:[{}],requestParam:{},responseParam:{},startTime:{},endTime:{}", methodName, requestParam, responseParam, startTime, endTime);
                    } else {
                        log.warn("methodName:[{}] error,requestParam:{},responseParam:{},startTime:{},endTime:{},stackTrace:{}", methodName, requestParam, responseParam, startTime, endTime, errorMessage);
                    }
                    break;
                case ERROR:
                    if (StringUtils.isBlank(errorMessage)) {
                        log.error("methodName:[{}],requestParam:{},responseParam:{},startTime:{},endTime:{}", methodName, requestParam, responseParam, startTime, endTime);
                    } else {
                        log.error("methodName:[{}] error,requestParam:{},responseParam:{},startTime:{},endTime:{},stackTrace:{}", methodName, requestParam, responseParam, startTime, endTime, errorMessage);
                    }
                    break;
                default:
                    if (StringUtils.isBlank(errorMessage)) {
                        log.info("methodName:[{}],requestParam:{},responseParam:{},startTime:{}, endTime:{}", methodName, requestParam, responseParam, startTime, endTime);
                    } else {
                        log.info("methodName:[{}] error,requestParam:{},responseParam:{},startTime:{},endTime:{},stackTrace:{}", methodName, requestParam, responseParam, startTime, endTime, errorMessage);
                    }
            }
        } catch (Exception e) {
            log.error("log2Hive error {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
