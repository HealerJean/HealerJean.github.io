package com.healerjean.proj.statemechine.config;

import com.healerjean.proj.statemechine.IStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * StateMachineBeanConfiguration
 *
 * @author zhangyujin
 * @date 2023-06-28 11:06:37
 */
@Slf4j
@Configuration
public class StateMachineBeanConfiguration implements ApplicationContextAware {

    /**
     * StateMachineBeanConfiguration
     */
    public StateMachineBeanConfiguration() {
    }

    /**
     * 状态机配置
     *
     * @param applicationContext applicationContext
     */
    @Override
    public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> stateMachineConfigBeans = applicationContext.getBeansWithAnnotation(EnableStateMachine.class);
        Map<String, IStateMachine> stateMachineBeans = applicationContext.getBeansOfType(IStateMachine.class);
        for (Map.Entry<String, Object> entry : stateMachineConfigBeans.entrySet()) {
            String configName = entry.getKey();
            Object configBean = entry.getValue();
            EnableStateMachine enableStateMachine = applicationContext.findAnnotationOnBean(configName, EnableStateMachine.class);
            assert enableStateMachine != null;
            String enableStateMachineName = enableStateMachine.value();
            for (Map.Entry<String, IStateMachine> e : stateMachineBeans.entrySet()) {
                String stateMachineName = e.getKey();
                IStateMachine stateMachineBean = e.getValue();
                if (enableStateMachineName.equalsIgnoreCase(stateMachineName)) {
                    try {
                        ((IStateMachineConfig) configBean).configure(stateMachineBean.getTransitions().setStateMachine(stateMachineBean));
                    } catch (Exception var5) {
                        log.error("StateMachineBeanConfiguration|Config {} failed.", stateMachineName);
                    }
                    log.info("StateMachineBeanConfiguration|Config {} success.", stateMachineName);
                }

            }
        }
    }
}
