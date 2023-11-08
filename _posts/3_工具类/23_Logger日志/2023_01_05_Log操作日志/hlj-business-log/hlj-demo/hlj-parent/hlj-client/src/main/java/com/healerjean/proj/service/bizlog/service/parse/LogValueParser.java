package com.healerjean.proj.service.bizlog.service.parse;

import com.google.common.base.Strings;
import com.healerjean.proj.service.bizlog.data.BizLogContext;
import com.healerjean.proj.service.bizlog.service.IFunctionService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangyujin
 * @date 2023/5/30  19:47.
 */
@Service
public class LogValueParser {

    /**
     * expressionEvaluator
     */
    private final LogExpressionEvaluator expressionEvaluator = new LogExpressionEvaluator();

    /**
     * pattern
     */
    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");


    /**
     * functionService
     */
    @Resource
    private IFunctionService functionService;

    /**
     * beanFactory
     */
    @Resource
    private BeanFactory beanFactory;


    /**
     * 函数解析结果Map
     * 1、获取解析上下文 EvaluationContext
     * 2、根据模版解析SPEL内置函数
     *
     * @param bizLogContext bizLogContext
     * @return Map<String, String>
     */
    public Map<String, String> buildFunctionResult(BizLogContext bizLogContext) {
        bizLogContext.setBeanFactory(beanFactory);
        Map<String, String> functionReturnMap = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(bizLogContext);

        List<String> templates = bizLogContext.getSpElTemplates();
        Method method = bizLogContext.getMethod();
        Class<?> targetClass = bizLogContext.getTargetClass();
        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                while (matcher.find()) {
                    // 解析spel表达式
                    String expression = matcher.group(2);
                    if (expression.contains("#_ret") || expression.contains("#_errorMsg")) {
                        continue;
                    }
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    //解析函数名
                    String functionName = matcher.group(1);
                    if (functionService.check(functionName)) {
                        String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                        String functionReturnValue = getFunctionReturnValue(null, value, functionName);
                        functionReturnMap.put(functionName, functionReturnValue);
                    }
                }
            }
        }
        return functionReturnMap;

    }


    /**
     * 获取函数执行结果
     *
     * @param functionReturnMap 函数返回结果
     * @param value             value
     * @param functionName      functionName
     * @return
     */
    private String getFunctionReturnValue(Map<String, String> functionReturnMap, String value, String functionName) {
        String functionReturnValue = "";
        if (functionReturnMap != null) {
            functionReturnValue = functionReturnMap.get(functionName);
        }
        if (StringUtils.isEmpty(functionReturnValue)) {
            functionReturnValue = functionService.apply(functionName, value);
        }
        return functionReturnValue;
    }


    /**
     * 解析SPEL模版
     * 1、获取解析上下文 EvaluationContext
     * 2、解析模版
     * @param bizLogContext bizLogContext
     * @return Map<String, String>
     */
    public Map<String, String> processTemplate(BizLogContext bizLogContext) {
        Map<String, String> expressionValues = new HashMap<>();

        // 1、获取解析上下文 EvaluationContext
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(bizLogContext);


        // 2、解析SPEL模版
        List<String> templates = bizLogContext.getSpElTemplates();
        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    // 1、依次获取Spel表达式
                    String expression = matcher.group(2);
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(bizLogContext.getMethod(), bizLogContext.getTargetClass());

                    // 2、解析spel表达式的数据
                    String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);

                    // 3、获取表达式的函数名进行函数编辑
                    String functionName = matcher.group(1);
                    String functionReturnValue = getFunctionReturnValue(bizLogContext.getFunctionReturnMap(), value, functionName);

                    // 4、将输入字符序列首次与正在表达式匹配的部分进行更改为replaceMent并且把结果添加到一个sb结果集中
                    matcher.appendReplacement(parsedStr, Strings.nullToEmpty(functionReturnValue));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, parsedStr.toString());
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }

        }
        return expressionValues;
    }

}
