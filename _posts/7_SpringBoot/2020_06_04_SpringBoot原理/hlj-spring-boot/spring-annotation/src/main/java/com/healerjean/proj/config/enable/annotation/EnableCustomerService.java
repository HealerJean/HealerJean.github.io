package com.healerjean.proj.config.enable.annotation;

import com.healerjean.proj.config.enable.LoggerEnableService;
import com.healerjean.proj.config.enable.register.CounterDefinitionRegistrar;
import com.healerjean.proj.config.enable.selector.MonitorImportSelector;
import org.springframework.context.annotation.Import;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:47 下午.
 * @description
 */
@Import({LoggerEnableService.class,
        MonitorImportSelector.class,
        CounterDefinitionRegistrar.class})
public @interface EnableCustomerService {
}
