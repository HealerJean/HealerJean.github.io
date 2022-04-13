package com.healerjean.proj.config.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyujin
 * @date 2022/4/13  21:02.
 * @description
 */
@Slf4j
@Configuration
public class CollectionProperties {

    @Value("#{${custom.map}}")
    private Map<String, String> customMap;

    @Value("#{'${custom.list}'.split(',')}")
    private List<String> customList;


    @Value("#{'${custom.set}'.split(',')}")
    private Set<String> customSet;



    @PostConstruct
    public void init(){
        log.info("[CollectionProperties] customMap:{}", customMap);
        log.info("[CollectionProperties] customList:{}", customList);
        log.info("[CollectionProperties] customSet:{}", customSet);
    }

}
