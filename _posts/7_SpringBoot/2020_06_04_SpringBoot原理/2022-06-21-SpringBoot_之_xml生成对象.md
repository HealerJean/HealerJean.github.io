---
title: SpringBoot_之_xml生成对象
date: 2022-06-21 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot_之_xml生成对象
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、配置类

## 1.1、`XmlBeanInnerConfig`

```java
@Data
public class XmlBeanInnerConfig {

    private String innerName;
}

```

## 1.2、`XmlBeanConfig`

```java
@Data
public class XmlBeanConfig {

    private List<String> list;

    private Set<String> set;

    private Map<String, String> xmlBeanMap;

    private Map<String, List<String>> xmlBeanListMap;

    private Map<String, HashSet<String>> xmlBeanSetMap;

    private XmlBeanInnerConfig xmlBeanInnerConfig;

    private List<XmlBeanInnerConfig> xmlBeanInnerConfigList;

    private Map<String, List<XmlBeanInnerConfig>> beanListMap;

}
```

## 1.3、`XmlResourceConfig`

```java
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

```



## 1.4、`xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd   http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd"
       default-autowire="byName" default-lazy-init="true">


    <!--private List<String> globalList;-->
    <bean id="globalList" class="java.util.ArrayList">
        <constructor-arg>
            <list>
                <value>张三</value>
                <value>小三</value>
            </list>
        </constructor-arg>
    </bean>



    <!--Map<String, String> stringMap;-->
    <bean id="stringMap" class="java.util.HashMap">
        <constructor-arg>
            <map>
                <entry key="1" value="10"></entry>
                <entry key="2" value="20"></entry>
                <entry key="3" value="30"></entry>
            </map>
        </constructor-arg>
    </bean>


    <!--Map<String, Map<String, String>> stringOnMap;-->
    <bean id="stringOnMap" class="java.util.HashMap">
        <constructor-arg>
            <map>
                <entry key="key1">
                    <map>
                        <entry key="1" value="10"></entry>
                        <entry key="2" value="20"></entry>
                        <entry key="3" value="30"></entry>
                    </map>
                </entry>
            </map>
        </constructor-arg>
    </bean>

    <!--private Map<String, HashSet<String>> hashOnMap;-->
    <bean id="hashOnMap" class="java.util.HashMap">
        <constructor-arg>
            <map>
                <entry key="set1">
                    <set>
                        <value>set1_value1</value>
                        <value>set1_value2</value>
                    </set>
                </entry>
                <entry key="set2">
                    <set>
                        <value>set2_value1</value>
                        <value>set2_value2</value>
                    </set>
                </entry>
            </map>
        </constructor-arg>
    </bean>



    <bean id="xmlBeanInnerConfigId1" class="com.healerjean.proj.config.xmlbean.XmlBeanInnerConfig">
        <property name="innerName" value="宣传部门1"></property>
    </bean>
    <bean id="xmlBeanInnerConfigId2" class="com.healerjean.proj.config.xmlbean.XmlBeanInnerConfig">
        <property name="innerName" value="宣传部门2"></property>
    </bean>


    <bean id="xmlBean" class="com.healerjean.proj.config.xmlbean.XmlBeanConfig">
        <!--private List<String> list;-->
        <property name="list">
            <list>
                <value>张三</value>
                <value>小三</value>
            </list>
        </property>

        <!--private Set<String> set;-->
        <property name="set">
            <set>
                <value>张三</value>
                <value>小三</value>
            </set>
        </property>


        <!--Map<String, String> xmlBeanMap;-->
        <property name="xmlBeanMap">
            <map>
                <entry key="1" value="10"></entry>
                <entry key="2" value="20"></entry>
                <entry key="3" value="30"></entry>
            </map>
        </property>

        <!--private Map<String, List<String>> listMap;-->
        <property name="listMap">
            <map>
                <entry key="list1">
                    <set>
                        <value>list1_value1</value>
                        <value>list1_value2</value>
                    </set>
                </entry>
                <entry key="list2">
                    <set>
                        <value>list2_value1</value>
                        <value>list2_value2</value>
                    </set>
                </entry>
            </map>
        </property>


        <!-- Map<String, HashSet<String>> setMap;-->
        <property name="setMap">
            <map>
                <entry key="set1">
                    <set>
                        <value>set1_value1</value>
                        <value>set1_value2</value>
                    </set>
                </entry>
                <entry key="set2">
                    <set>
                        <value>set2_value1</value>
                        <value>set2_value2</value>
                    </set>
                </entry>
            </map>
        </property>


        <!--private XmlBeanInnerConfig xmlBeanInnerConfig-->
        <property name="xmlBeanInnerConfig">
            <ref bean="xmlBeanInnerConfigId1"></ref>
        </property>


        <!--private List<XmlBeanInnerConfig> xmlBeanInnerConfigList;-->
        <property name="xmlBeanInnerConfigList">
            <list>
                <ref bean="xmlBeanInnerConfigId1"></ref>
                <ref bean="xmlBeanInnerConfigId2"></ref>
            </list>
        </property>

        <!-- Map<String, HashSet<String>> xmlBeanSetMap;-->
        <property name="beanListMap">
            <map>
                <entry key="bean1">
                    <set>
                        <ref bean="xmlBeanInnerConfigId1"></ref>
                        <ref bean="xmlBeanInnerConfigId2"></ref>
                    </set>
                </entry>
                <entry key="bean2">
                    <set>
                        <ref bean="xmlBeanInnerConfigId1"></ref>
                        <ref bean="xmlBeanInnerConfigId2"></ref>
                    </set>
                </entry>
            </map>
        </property>

    </bean>


</beans>
```



# 2、拆解实例

## 2.1、`List<String>`

```xml
<!--private List<String> list;-->
<property name="list">
  <list>
    <value>张三</value>
    <value>小三</value>
  </list>
</property>

```

## 2.2、`Set<String>`

```xml
<!--private Set<String> set;-->
<property name="set">
  <set>
    <value>张三</value>
    <value>小三</value>
  </set>
</property>

```

## 2.3、`Map<String, String>`

```xml
<!--Map<String, String> xmlBeanMap;-->
<property name="xmlBeanMap">
  <map>
    <entry key="1" value="10"></entry>
    <entry key="2" value="20"></entry>
    <entry key="3" value="30"></entry>
  </map>
</property>
```

## 2.4、`Map<String, List<String>>`

```xml
<!--private Map<String, List<String>> listMap;-->
<property name="listMap">
  <map>
    <entry key="list1">
      <set>
        <value>list1_value1</value>
        <value>list1_value2</value>
      </set>
    </entry>
    <entry key="list2">
      <set>
        <value>list2_value1</value>
        <value>list2_value2</value>
      </set>
    </entry>
  </map>
</property>
```



## 2.5、`Map<String, HashSet<String>>`

```xml
<!-- Map<String, HashSet<String>> setMap;-->
<property name="setMap">
  <map>
    <entry key="set1">
      <set>
        <value>set1_value1</value>
        <value>set1_value2</value>
      </set>
    </entry>
    <entry key="set2">
      <set>
        <value>set2_value1</value>
        <value>set2_value2</value>
      </set>
    </entry>
  </map>
</property>
```



## 2.6、`Bean`

### 2.6.1、普通 `Bean`

```xml
<bean id="xmlBeanInnerConfigId1" 
      class="com.healerjean.proj.config.xmlbean.XmlBeanInnerConfig">
  <property name="innerName" value="宣传部门1"></property>
</bean>
<bean id="xmlBeanInnerConfigId2" 
      class="com.healerjean.proj.config.xmlbean.XmlBeanInnerConfig">
  <property name="innerName" value="宣传部门2"></property>
</bean>

```



### 2.6.2、内部 `Bean`

```xml
<property name="xmlBeanInnerConfig">
  <ref bean="xmlBeanInnerConfigId1"></ref>
</property>
```



### 2.6.3、内部 `List<Bean>`

```xml
<!--private List<XmlBeanInnerConfig> xmlBeanInnerConfigList;-->
<property name="xmlBeanInnerConfigList">
  <list>
    <ref bean="xmlBeanInnerConfigId1"></ref>
    <ref bean="xmlBeanInnerConfigId2"></ref>
  </list>
</property>

```



### 2.6.4、内部 `Map` `Bean`

```xml
<!-- Map<String, HashSet<String>> xmlBeanSetMap;-->
<property name="beanListMap">
  <map>
    <entry key="bean1">
      <set>
        <ref bean="xmlBeanInnerConfigId1"></ref>
        <ref bean="xmlBeanInnerConfigId2"></ref>
      </set>
    </entry>
    <entry key="bean2">
      <set>
        <ref bean="xmlBeanInnerConfigId1"></ref>
        <ref bean="xmlBeanInnerConfigId2"></ref>
      </set>
    </entry>
  </map>
</property>

```



## 2.7、全局集合类

### 2.7.1、全局 `List`

```xml
<!--private List<String> globalList;-->
<bean id="globalList" class="java.util.ArrayList">
  <constructor-arg>
    <list>
      <value>张三</value>
      <value>小三</value>
    </list>
  </constructor-arg>
</bean>

```

### 2.7.2、`Map<String, String>`

```xml
<!--Map<String, String> stringMap;-->
<bean id="stringMap" class="java.util.HashMap">
  <constructor-arg>
    <map>
      <entry key="1" value="10"></entry>
      <entry key="2" value="20"></entry>
      <entry key="3" value="30"></entry>
    </map>
  </constructor-arg>
</bean>

```



### 2.7.3、`Map<String, Map<String, String>>`

```xml
<!--Map<String, Map<String, String>> stringOnMap;-->
<bean id="stringOnMap" class="java.util.HashMap">
  <constructor-arg>
    <map>
      <entry key="key1">
        <map>
          <entry key="1" value="10"></entry>
          <entry key="2" value="20"></entry>
          <entry key="3" value="30"></entry>
        </map>
      </entry>
    </map>
  </constructor-arg>
</bean>

```

### 2.7.4、`Map<String, HashSet<String>>`

```xml
<!--private Map<String, HashSet<String>> hashOnMap;-->
<bean id="hashOnMap" class="java.util.HashMap">
  <constructor-arg>
    <map>
      <entry key="set1">
        <set>
          <value>set1_value1</value>
          <value>set1_value2</value>
        </set>
      </entry>
      <entry key="set2">
        <set>
          <value>set2_value1</value>
          <value>set2_value2</value>
        </set>
      </entry>
    </map>
  </constructor-arg>
</bean>
```















![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)



<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'x5SvIKAMbafCOUs1',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



