package com.healerjean.proj.config;

import com.healerjean.proj.annotation.condition.EncryptCondition;
import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.bean.DataBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

/**
 * @author HealerJean
 * @ClassName DataConfit
 * @date 2020/6/4  18:58.
 * @Description
 */
@ConfigurationProperties("var")
@Configuration
@Slf4j
@Data
public class DataConfig {

    private String name ;
    private String email;

    @Bean
    public DataBean dataBean() {
        log.info(name);
        return new DataBean();
    }

}
