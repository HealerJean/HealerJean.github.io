package com.healerjean.proj.service.bizlog.aspect;

import com.healerjean.proj.service.bizlog.anno.LogRecordAnnotation;
import com.healerjean.proj.service.bizlog.data.BizLogContext;
import com.healerjean.proj.service.bizlog.utils.LogTheadLocal;
import com.healerjean.proj.service.bizlog.service.BizLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 业务日志切面
 *
 * @author zhangyujin
 * @date 2023/5/30  18:54.
 */
//todo
@Slf4j
@Component
@Aspect
@Order(2)
public class BizLogAspect {


    @Resource
    private BizLogService bizLogService;

    /**
     * 1、切面方法执行
     * 2、切面日志记录
     * 3、结果返回
     *
     * @param pjp                 pjp
     * @param logRecordAnnotation logRecordAnnotation
     * @return Object
     * @throws Throwable
     */
    @Around(value = "(execution(* *(..)) && @annotation(logRecordAnnotation))", argNames = "pjp,logRecordAnnotation")
    public Object around(final ProceedingJoinPoint pjp, LogRecordAnnotation logRecordAnnotation) throws Throwable {
        Signature sig = pjp.getSignature();
        Method method = null;
        Class<?> clazz = null;
        Object[] args = pjp.getArgs();
        if ((sig instanceof MethodSignature)) {
            MethodSignature signature = (MethodSignature) sig;
            Object target = pjp.getTarget();
            clazz = pjp.getTarget().getClass();
            method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
        } else {
            log.error("signature is not instanceof MethodSignature!");
        }
        if (method == null) {
            return pjp.proceed();
        }


        // 1、切面方法执行
        Object result = null;
        BizLogContext.MethodExecuteResult methodExecuteResult = new BizLogContext
                .MethodExecuteResult()
                .setSuccess(true);

        LogTheadLocal.putEmptySpan();
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            methodExecuteResult.setSuccess(false).setErrorMsg(e.getMessage()).setThrowable(e);
        }

        // 2、切面日志记录
        BizLogContext bizLogContext = new BizLogContext();
        bizLogContext.setMethod(method);
        bizLogContext.setTargetClass(clazz);
        bizLogContext.setArgs(args);
        bizLogContext.setLogRecordAnnotation(logRecordAnnotation);
        bizLogContext.setMethodExecuteResult(methodExecuteResult);
        try {
            bizLogService.bizLogExecute(bizLogContext);
        }catch (Exception e){
            log.error("[BizLogAspect#around] 操作日志记录异常", e);
        }finally {
            LogTheadLocal.clear();
        }

        // 3、结果返回
        if (methodExecuteResult.getThrowable() != null) {
            throw methodExecuteResult.getThrowable();
        }
        return result;
    }

}
