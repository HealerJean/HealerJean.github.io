package com.healerjean.proj.config.properties;

import com.healerjean.proj.enums.CustomEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/4/13  21:22.
 * @description
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "custom.flow.config")
@Data
public class CollectionYml {

    private CustomEnum customEnum;
    private List<String> check;
    private Map<String,YmlInnerConfig> ymlInnerConfig;


    @PostConstruct
    public void init() {
        log.info("[CollectionYml] customEnum:{}", customEnum);
        log.info("[CollectionYml] check:{}", check);
        log.info("[CollectionYml] ymlInnerConfig:{}", ymlInnerConfig);
    }

}
