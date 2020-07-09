package com.healerjean.proj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HealerJean
 * @ClassName AppProperties
 * @date 2020/7/8  17:29.
 * @Description
 */
@ConfigurationProperties(value = "a")
@Data
public class AppProperties {

}
