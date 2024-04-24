package com.healerjean.proj.utils;

import org.slf4j.Logger;

import java.util.function.Function;

/**
 * ExceptionUtils
 *
 * @author zhangyujin
 * @date 2024/4/2
 */
public class ExceptionUtils {

    /**
     * catchException
     *
     * @param function function
     * @param req req
     * @param log log
     * @param msg msg
     * @return {@link RES}
     */
    public static <REQ, RES> RES catchFuntionException(Function<REQ, RES> function, REQ req, Logger log, String msg) {
        try {
            return function.apply(req);
        } catch (Exception e) {
            log.info("[RedisCacheAspect#tryExceptionIgnore] msg:{}, req:{}", msg, req, e);
        }
        return null;
    }


}
