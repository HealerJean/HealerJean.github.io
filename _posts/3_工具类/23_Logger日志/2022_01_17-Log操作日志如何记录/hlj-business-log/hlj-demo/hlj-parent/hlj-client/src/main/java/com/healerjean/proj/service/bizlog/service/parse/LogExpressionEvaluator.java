package com.healerjean.proj.service.bizlog.service.parse;

import com.healerjean.proj.service.bizlog.data.BizLogContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志表达式解析器
 *
 * @author zhangyujin
 * @date 2023/5/30  19:35.
 */

public class LogExpressionEvaluator extends CachedExpressionEvaluator {

    /**
     * 表达式缓存
     */
    private Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    /**
     * 方法缓存
     */
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);


    /**
     * 创建解析上下文 EvaluationContext
     *
     * @param bizLogContext bizLogContext
     * @return createEvaluationContext
     */
    public EvaluationContext createEvaluationContext(BizLogContext bizLogContext) {
        LogEvaluationContext evaluationContext = new LogEvaluationContext(getParameterNameDiscoverer(), bizLogContext);
        BeanFactory beanFactory = bizLogContext.getBeanFactory();
        evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        return evaluationContext;
    }


    /***
     * 表达式解析
     * @param conditionExpression conditionExpression
     * @param methodKey methodKey
     * @param evalContext evalContext
     * @return String
     */
    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        Object value = getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, Object.class);
        return value == null ? "" : value.toString();
    }


    /**
     * getTargetMethod
     *
     * @param targetClass targetClass
     * @param method      method
     * @return
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }
}
