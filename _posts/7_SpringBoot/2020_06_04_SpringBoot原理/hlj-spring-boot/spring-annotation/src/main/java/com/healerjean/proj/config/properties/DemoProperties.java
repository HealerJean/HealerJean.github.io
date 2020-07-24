package com.healerjean.proj.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:demo.properties")
@Configuration
@Data
public class DemoProperties {

}
