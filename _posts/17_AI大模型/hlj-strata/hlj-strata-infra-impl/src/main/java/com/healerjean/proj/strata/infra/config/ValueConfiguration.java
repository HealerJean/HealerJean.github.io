package com.healerjean.proj.strata.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValueConfiguration {

    @Value("common.test")
    private String message;

    public String getMessage() {
        return message;
    }
}
