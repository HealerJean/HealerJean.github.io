package com.hlj.proj.testmain;

import com.hlj.proj.utils.threadpool.ThreadPoolExecutorMdcWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyujin
 * @date 2022/3/11  3:57 下午.
 * @description
 */
@Slf4j
public class TestMain2_TraceId {


    public static final String TRACE_ID = "REQ_UID";

    @Test
    public void threadpool() throws InterruptedException {
        MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        log.info("[TestMain2_TraceId#threadpool] 主线程开始");
        ThreadPoolExecutorMdcWrapper threadPoolExecutorMdcWrapper = new ThreadPoolExecutorMdcWrapper(10, 300, 30000, TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<>(10));
        threadPoolExecutorMdcWrapper.submit(()->{
            log.info("[TestMain2_TraceId#threadpool] 线程池任务1 start ");


            log.info("[TestMain2_TraceId#test] 线程池任务1 end  ");

        });

        threadPoolExecutorMdcWrapper.submit(()->{
            log.info("[TestMain2_TraceId#threadpool] 线程池任务2 start ");


            log.info("[TestMain2_TraceId#test] 线程池任务2 end  ");

        });

        log.info("[TestMain2_TraceId#threadpool] 主线程结束");

        Thread.sleep(3000);

        MDC.remove(TRACE_ID);

    }


}
