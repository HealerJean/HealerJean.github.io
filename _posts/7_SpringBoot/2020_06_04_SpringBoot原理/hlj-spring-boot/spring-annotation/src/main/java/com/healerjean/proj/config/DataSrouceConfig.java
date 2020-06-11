package com.healerjean.proj.config;

import com.healerjean.proj.config.bean.DataSrouceBean;
import com.healerjean.proj.config.condition.EncryptCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author HealerJean
 * @ClassName DataSrouceConfig
 * @date 2020/6/11  10:10.
 * @Description
 */
@Conditional({EncryptCondition.class})
@Slf4j
public class DataSrouceConfig {

    @Bean
    public DataSrouceBean dataSrouceBean() {
        log.info("服务启动--------数据源加密配置成功");
        return new DataSrouceBean();
    }

}
