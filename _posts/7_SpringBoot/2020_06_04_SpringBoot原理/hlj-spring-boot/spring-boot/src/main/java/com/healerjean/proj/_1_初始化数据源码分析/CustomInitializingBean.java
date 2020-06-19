package com.healerjean.proj._1_初始化数据源码分析;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomInitializingBean  implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("CustomInitializingBean  implements InitializingBean --------afterPropertiesSet()方法");
    }
}
