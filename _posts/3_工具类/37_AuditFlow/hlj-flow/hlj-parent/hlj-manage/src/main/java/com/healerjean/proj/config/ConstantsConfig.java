package com.healerjean.proj.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author HealerJean
 * @ClassName ConstantConfig
 * @date 2019/10/18  14:27.
 * @Description
 */
@Configuration
@Data
public class ConstantsConfig {

    public static String application_name;

    @PostConstruct
    public void init() {
        application_name = this.applicationName;
    }

    @Value("${spring.application.name}")
    private String applicationName;


}
