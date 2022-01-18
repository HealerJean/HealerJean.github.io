package com.hlj.proj.business.function.impl;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangyujin
 * @date 2022/1/18  4:15 下午.
 * @description
 * LogRecordExpressionEvaluator 继承自 CachedExpressionEvaluator 类
 * 这个类里面有两个 Map，一个是 expressionCache 一个是 targetMethodCache。
 * 在上面的例子中可以看到，SpEL 会解析成一个 Expression 表达式，然后根据传入的 Object 获取到对应的值，
 *
 * 下面的
 */
public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

    /**
     * expressionCache 是为了缓存方法、表达式和 SpEL 的 Expression 的对应关系，让方法注解上添加的 SpEL 表达式只解析一次。
     */
    private Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    /**
     * targetMethodCache 是为了缓存传入到 Expression 表达式的 Object。核心的解析逻辑是上面最后一行代码。
     */
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, String.class);
    }
}