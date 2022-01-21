package com.custom.proj.annotation;

import com.custom.proj.register.CounterDefinitionRegistrar;
import com.custom.proj.selector.MonitorImportSelector;
import com.custom.proj.service.CustomConfiguration;
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
}
