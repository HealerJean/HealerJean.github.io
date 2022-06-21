package com.healerjean.proj.config.xmlbean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/21  09:44.
 */
@Slf4j
@ImportResource(value = "classpath:application-custom.xml")
@Configuration
public class XmlResourceConfig {


    @Resource(name="globalList")
    private List<String> globalList;

    @Resource(name="stringMap")
    private Map<String, String> stringMap;

    @Resource(name="stringOnMap")
    private Map<String, Map<String, String>> stringOnMap;

    @Resource(name="hashOnMap")
    private Map<String, HashSet<String>> hashOnMap;

    @Resource(name = "xmlBean")
    private XmlBeanConfig xmlBeanConfig;

    @PostConstruct
    public void init(){
        log.info("[XmlResourceConfig@#init] globalList:{}", globalList);
        log.info("[XmlResourceConfig@#init] map:{}", stringMap);
        log.info("[XmlResourceConfig@#init] stringOnMap:{}", stringOnMap);
        log.info("[XmlResourceConfig@#init] hashOnMap:{}", hashOnMap);
        log.info("[XmlResourceConfig@#init] xmlBean:{}", xmlBeanConfig);
    }
}
