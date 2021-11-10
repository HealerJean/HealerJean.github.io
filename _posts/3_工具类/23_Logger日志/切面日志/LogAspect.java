package com.jd.merchant.sjx.aop;

import com.alibaba.fastjson.JSONObject;
import com.jd.jr.sgm.probe.api.SgmSpan;
import com.jd.jr.sgm.probe.api.SgmTracer;
import com.jd.jr.sgm.probe.api.SgmTracerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author zhangyujin
 * @date 2021/11/9  5:24 下午
 * @description
 */
@Slf4j
@Component
@Aspect
public class LogAspect {

    private static ThreadLocal<TraceLogSpan> traceLogThreadLocal = new ThreadLocal<>();
    private static SgmTracer SGM_TRACER = SgmTracerFactory.create();

    @Around(value = "(execution(* *(..)) && @annotation(logIndex))", argNames = "pjp,logIndex")
    public Object printLog(final ProceedingJoinPoint pjp, LogIndex logIndex) throws Throwable {
        Object[] args = pjp.getArgs();
        String methodName = pjp.getSignature().getName();
        Signature sig = pjp.getSignature();
        String className = sig.getDeclaringTypeName();
        long start = System.currentTimeMillis();

        //设置traceId
        setTraceLogSpan();

        Object result = null;
        try {
            result = pjp.proceed();
            return result;
        } finally {
            long timeCost = System.currentTimeMillis() - start;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("className", className);
            jsonObject.put("methodName", methodName);
            jsonObject.put("requestParams", args);
            jsonObject.put("responseParams", result);
            jsonObject.put("timeCost", timeCost+"ms");
            log.info("LogAspect：{}", jsonObject.toJSONString());

            traceLogThreadLocal.remove();
            MDC.clear();
        }
    }

    private void setTraceLogSpan(){
        TraceLogSpan traceLogSpan = new TraceLogSpan();
        try {
            SgmSpan sgmSpan = SGM_TRACER.activeSpan();
            if (sgmSpan.getTraceId() == 0) {
                this.genTraceId(traceLogSpan);
            } else {
                traceLogSpan.setSgmSpan(sgmSpan);
            }
        } catch (Exception ex) {
            log.error("获取SGM traceId异常 ", ex);
            this.genTraceId(traceLogSpan);
        }
        traceLogThreadLocal.set(traceLogSpan);
    }


    private void genTraceId(TraceLogSpan traceLogSpan){
        String traceId = getTraceId();
        traceLogSpan.setTraceId(traceId);
        MDC.put("trace-id",traceId);
    }

    private String getTraceId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    @Data
    class TraceLogSpan {
        private SgmSpan sgmSpan;
        private String traceId;
    }

}
