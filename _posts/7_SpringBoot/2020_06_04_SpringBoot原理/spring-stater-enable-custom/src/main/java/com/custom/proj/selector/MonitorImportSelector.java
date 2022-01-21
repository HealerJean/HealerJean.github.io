package com.custom.proj.selector;

import com.custom.proj.annotation.EnableCustomerService;
import com.custom.proj.enums.MonitorSelectorEnum;
import com.custom.proj.selector.service.MonitorEnableAService;
import com.custom.proj.selector.service.MonitorEnableBService;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:47 下午.
 * @description
 */
public class MonitorImportSelector implements ImportSelector {

    /**
     * 可以拿到所有注解的信息，可以根据不同注解的和注解的属性来返回不同的class,
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableCustomerService.class.getName());
        MonitorSelectorEnum monitorSelectorEnum = (MonitorSelectorEnum) annotationAttributes.get("monitor");
        switch (monitorSelectorEnum) {
            case MONITOR_ENABLE_A_SERVICE:
                return new String[]{MonitorEnableAService.class.getName()};
            case MONITOR_ENABLE_B_SERVICE:
                return new String[]{MonitorEnableBService.class.getName()};
            default:
                return new String[0];
        }
    }
}