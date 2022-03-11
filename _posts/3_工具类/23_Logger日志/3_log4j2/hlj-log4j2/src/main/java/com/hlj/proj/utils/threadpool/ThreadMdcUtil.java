package com.hlj.proj.utils.threadpool;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author zhangyujin
 * @date 2022/3/11  3:48 下午.
 * @description
 */
public class ThreadMdcUtil {

    public static final String TRACE_ID = "REQ_UID";
    public static final String SON_ID = "SON_UID";

    public static void setTraceIdIfAbsent() {
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        }

        if (MDC.get(SON_ID) == null) {
            MDC.put(SON_ID, UUID.randomUUID().toString().replace("-", ""));
        }
    }

    /**
     * 封装线程任务，在执行的时候放入MDC，执行结束删除MDC
     * @param callable callable 线程任务
     * @param context MDC属性
     * @param <T>
     * @return
     */
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }


    /**
     * 封装线程任务，在执行的时候放入MDC，执行结束删除MDC
     * @param runnable Runnable 线程任务
     * @param context MDC属性
     * @return
     */
    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            // 断当前线程对应MDC的Map是否存在，存在则设置
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
