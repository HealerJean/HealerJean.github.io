package com.healerjean.proj.config.xmlbean;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyujin
 * @date 2022/6/21  09:51.
 */
@Data
public class XmlBeanConfig {

    private List<String> list;

    private Set<String> set;

    private Map<String, String> xmlBeanMap;

    private Map<String, List<String>> listMap;

    private Map<String, HashSet<String>> setMap;

    private XmlBeanInnerConfig xmlBeanInnerConfig;

    private List<XmlBeanInnerConfig> xmlBeanInnerConfigList;

    private Map<String, List<XmlBeanInnerConfig>> beanListMap;

}
