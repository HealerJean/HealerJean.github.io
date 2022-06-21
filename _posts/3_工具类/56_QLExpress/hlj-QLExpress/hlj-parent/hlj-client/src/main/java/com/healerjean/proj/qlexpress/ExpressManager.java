package com.healerjean.proj.qlexpress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/21  17:16.
 */
@Slf4j
@Component
public class ExpressManager implements ApplicationContextAware{

    private ApplicationContext applicationContext;


    /**
     * Spring重载方法
     * @param applicationContext Spring上下文对象
     * @throws BeansException 抛出异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 执行规则引擎
     * @param express 表达式
     * @param aProperties 上下文对象
     * @return 返回对象
     * @throws Exception 抛出异常
     */
    public Object execute(String express, Map<String, Object> aProperties) throws Exception {
        QlExpressRunner runner = new QlExpressRunner();
        QlExpressContext context = new QlExpressContext(aProperties, applicationContext);
        context.put("SYSTEM_VARIABLE_RUNNER", runner);
        return runner.execute(express, context, null, true, false);
    }


}
