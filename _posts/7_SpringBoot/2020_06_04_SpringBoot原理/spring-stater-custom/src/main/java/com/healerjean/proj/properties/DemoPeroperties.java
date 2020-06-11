package com.healerjean.proj.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author HealerJean
 * @ClassName DemoPeroperties
 * @date 2020/6/11  10:39.
 * @Description
 */
@ConfigurationProperties("demo")
@Data
public class DemoPeroperties {

    /** 提供一个默认值 */
    private static final String NAME = "";


    /** 下面的配置将来在主项目中配置 */
    private String name = NAME;
    private Integer age;
}
