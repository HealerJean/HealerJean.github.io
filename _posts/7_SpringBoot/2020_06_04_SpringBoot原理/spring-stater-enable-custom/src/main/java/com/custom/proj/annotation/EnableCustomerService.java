package com.custom.proj.annotation;

import com.custom.proj.enums.CounterSelectorEnum;
import com.custom.proj.enums.MonitorSelectorEnum;
import com.custom.proj.register.CounterDefinitionRegistrar;
import com.custom.proj.selector.MonitorImportSelector;
import com.custom.proj.configuration.CustomConfiguration;
import com.custom.proj.service.LoggerEnableService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:47 下午.
 * @description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LoggerEnableService.class,
        CustomConfiguration.class,
        MonitorImportSelector.class,
        CounterDefinitionRegistrar.class})
public @interface EnableCustomerService {

    MonitorSelectorEnum monitor() default MonitorSelectorEnum.MONITOR_ENABLE_A_SERVICE;

    /**
     * 暂未使用
     */
    CounterSelectorEnum countor() default CounterSelectorEnum.COUNTER_ENABLE_A_SERVICE;

}
