package com.healerjean.proj.service.bizlog.service.parse;

import com.healerjean.proj.service.bizlog.data.BizLogContext;
import com.healerjean.proj.service.bizlog.utils.LogTheadLocal;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhangyujin
 * @date 2023/5/30  19:42.
 */
public class LogEvaluationContext extends MethodBasedEvaluationContext {


    /**
     * 1、初始化 EvaluationContext
     * 2、获取本地内存变量数据放到上下文
     * 3、获取被切面的方法执行结果，放入上下文
     * LogEvaluationContext
     *
     * @param parameterNameDiscoverer parameterNameDiscoverer
     * @param logContext              logContext
     */
    public LogEvaluationContext(ParameterNameDiscoverer parameterNameDiscoverer, BizLogContext logContext) {
        // 1、初始化 EvaluationContext
        super(null, logContext.getMethod(), logContext.getArgs(), parameterNameDiscoverer);

        // 2、获取本地内存变量数据放到上下文
        Map<String, Object> variables = LogTheadLocal.getVariables();
        if (variables != null && variables.size() > 0) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                setVariable(entry.getKey(), entry.getValue());
            }
        }

        // 3、获取被切面的方法执行结果，放入上下文
        BizLogContext.MethodExecuteResult methodExecuteResult = logContext.getMethodExecuteResult();
        Object result = null;
        String errorMsg = null;
        if (Objects.nonNull(methodExecuteResult)) {
            result = methodExecuteResult.getResult();
            errorMsg = methodExecuteResult.getErrorMsg();
        }
        setVariable("_ret", result);
        setVariable("_errorMsg", errorMsg);
    }
}