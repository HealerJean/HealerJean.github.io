package com.custom.proj.selector;

import com.custom.proj.service.MonitorEnableService;
import com.custom.proj.annotation.EnableCustomerService;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:47 下午.
 * @description
 */
public class MonitorImportSelector implements ImportSelector {

    /**
     * 可以拿到所有注解的信息，可以根据不同注解的和注解的属性来返回不同的class,
     */
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes( EnableCustomerService.class.getName()));
        return new String[]{MonitorEnableService.class.getName()};
    }
}