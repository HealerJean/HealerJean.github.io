package com.healerjean.proj.service.bizlog.data;

import com.healerjean.proj.service.bizlog.anno.LogRecordAnnotation;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 业务日志上下文
 *
 * @author zhangyujin
 * @date 2023/5/30  18:29.
 */
@Accessors(chain = true)
@Data
public class BizLogContext {

    /**
     * logRecordAnnotation
     */
    private LogRecordAnnotation logRecordAnnotation;

    /**
     * 切面类
     */
    private Class<?> targetClass;

    /**
     * 切面方法
     */
    private Method method;

    /**
     * 参数数组
     */
    private Object[] args;

    /**
     * spel 模版
     */
    private List<String> spElTemplates;

    /**
     * spel 函数解析结果（key 函数 value 结果）
     */
    private Map<String, String> functionReturnMap;

    /**
     * 方法执行结果
     */
    private MethodExecuteResult methodExecuteResult;

    /**
     * bean工厂
     */
    private BeanFactory beanFactory;


    @Accessors(chain = true)
    @Data
    public static class MethodExecuteResult {

        /**
         * 切面方法执行结果
         */
        private Object result;

        /**
         * 切面方法是否成功
         */
        private boolean success;

        /**
         * 切面方法 异常
         */
        private Throwable throwable;

        /**
         * 切面方法 异常信息
         */
        private String errorMsg;
    }



}
