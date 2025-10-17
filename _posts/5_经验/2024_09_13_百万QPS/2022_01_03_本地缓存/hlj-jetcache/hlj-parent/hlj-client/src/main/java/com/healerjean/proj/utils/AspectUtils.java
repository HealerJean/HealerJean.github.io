package com.healerjean.proj.utils;

import com.healerjean.proj.common.contants.RedisConstants;
import com.healerjean.proj.common.anno.ElParam;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
/**
 * AspectUtils
 * @author zhangyujin
 * @date 2023/5/26  11:46
 */
public class AspectUtils {
    /**
     * el解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 获取缓存key,规则：项目:Cacheable.prefix:R2mCacheParam.value表达式获取的值
     * 获取 RedisLockParam 对应参数的值
     *
     * @param jp     ProceedingJoinPoint
     * @param method Method
     * @return String
     */
    public static String parseParamKey(ProceedingJoinPoint jp, Method method) {
        StringBuilder key = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = jp.getArgs()[i];
            ElParam elParam = parameter.getAnnotation(ElParam.class);
            if (elParam == null) {
                continue;
            }
            //如果参数未配置value,则自动获取参数名称作为表达式
            StringBuilder elParserBuilder = new StringBuilder();
            if (StringUtils.isBlank(elParam.value())) {
                elParserBuilder.append("#").append(parameter.getName());
            } else {
                elParserBuilder.append(elParam.value());
            }
            // 获取el值
            String parseVal = AspectUtils.parseExpression(parameter.getName(), arg, elParserBuilder.toString());
            if (StringUtils.isNotBlank(parseVal)) {
                key.append(parseVal).append(RedisConstants.SPLIT);
            }
        }
        //如果方法无参数,获取类名+方法名作为key
        if (parameters.length == 0) {
            String name = method.getDeclaringClass().getName() + "." + method.getName();
            key.append(name).append(RedisConstants.SPLIT);
        }
        // 如果获取的最终key值为空，会抛出异常
        if (parameters.length > 1 && StringUtils.isEmpty(key.toString())) {
            return null;
        }
        if (StringUtils.endsWith(key, RedisConstants.SPLIT)) {
            key = new StringBuilder(StringUtils.substringBeforeLast(key.toString(), RedisConstants.SPLIT));
        }
        return key.toString();
    }

    /**
     * 转换对象
     *
     * @param param 对象
     * @param arg   Object
     * @param exp   表达式
     * @return String
     */
    public static String parseExpression(String param, Object arg, String exp) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable(param, arg);
            Expression expression = PARSER.parseExpression(exp);
            Object cal = expression.getValue(context);
            return cal != null ? cal.toString() : "";
        } catch (Exception e) {
            throw new RuntimeException("Unsupported redis cache param value: " + exp + ".");
        }
    }
}
