---
title: properties和yml工具类读取
date: 2021-12-12 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: properties和yml工具类读取
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、`properties`

## 1.1、工具1：`PropertiesUtil`

```java

package com.healerjean.proj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

  private static Properties properties = null;
  private static String[] props = new String[]{"application-db.properties"};

  private PropertiesUtil() {
  }

  public static String getProperty(String key) {
      if (properties == null) {
          initProperty();
      }
      return properties.getProperty(key) == null ? "" : properties.get(key).toString();
  }

  private static synchronized void initProperty() {
      if (properties == null) {
          properties = new Properties();
          for (String prop : props) {
              InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(prop);
              if (inputStream != null) {
                  Properties propertiest = new Properties();
                  try {
                      propertiest.load(inputStream);
                      properties.putAll(propertiest);
                  } catch (IOException e) {
                      throw new RuntimeException(e.getMessage(), e);
                  }
              }
          }
      }
  }

}

```



## 1.2、工具2：`PropertiesUtil`

```java

@SLF4J
public class PropertiesUtil {

  private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
  public final static ConcurrentMap<String, String> KEY_1 = new ConcurrentHashMap<String, String>();
  public final static ConcurrentMap<String, String> KEY_2 = new ConcurrentHashMap<String, S


  static {
      init();  
  }

  public static void init() throws Exception {
      log.info("Properties init start");
      initMap("props/KEY_1", KEY_1);
      initMap("props/KEY_2", KEY_2);
      log.info("Properties init end");
  }

  public static void initMap(String configFile, ConcurrentMap<String, String> configMap) {
      ResourceBundle configRb = ResourceBundle.getBundle(configFile);
      Enumeration<String> elements = configRb.getKeys();
      while (elements.hasMoreElements()) {
          String key = elements.nextElement();
          configMap.put(key, configRb.getString(key));
      }
  }

}

```



# 2、`yml`

## 2.1、`YamlHelper`

### 2.1.1、`pom`

```xml
<!-- 装载Yml配置文件 -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>2.13.3</version>
</dependency>
```

### 2.1.2、`YamlHelper`

```java
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 辅助装载项目中Yaml文件
*
* @author houyadong3
* @date 2023-01-05 20:11 2023-01-06 09:16
*/
@Slf4j·
public class YamlHelper {

  /**
   * 将Yaml文件装载为Map对象
   *
   * @param path
   * @return Map
   */
  public static Map loadYaml(String path) {
      try {
          String yamlString = preLoad(path);
          if (yamlString == null || yamlString.length() <= 0) {
              return null;
          }
          //初始化Yaml解析器
          Yaml yaml = new Yaml();
          //读入文件
          Object result = yaml.load(yamlString);
          if (result instanceof Map) {
              return (Map) result;
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return null;
  }

  /**
   * 将Yaml文件装载为实体对象
   *
   * @param path
   * @return T
   */
  public static <T> T loadYaml(String path, Class<T> type) {
      try {
          String yamlString = preLoad(path);
          if (yamlString == null || yamlString.length() <= 0) {
              return null;
          }
          //初始化Yaml解析器
          Yaml yaml = new Yaml();
          //读入文件
          return yaml.loadAs(yamlString, type);

      } catch (Exception e) {
          e.printStackTrace();
      }
      return null;
  }

  /**
   * 预载Yaml文件,并移除其中无法识别的注释内容
   * 这个方法主要作用是因为文件中有!!的一行，yaml无法进行解析，所以将其去掉
   *
   * @param path
   * @return String
   */
  private static String preLoad(String path) {
      FileReader fileReader = new FileReader(path);
      List<String> readLines = fileReader.readLines();
      if (CollUtil.isNotEmpty(readLines)) {
          StringBuilder stringBuilder = new StringBuilder();
          List<String> collect = readLines.stream().filter(vo -> !vo.trim().startsWith("!")).collect(Collectors.toList());
          for (String line : collect) {
              stringBuilder.append(line).append("\n");
          }
          String result = stringBuilder.toString();
          log.info("YAML 内容-->{}", result);
          return result;
      }
      return null;
  }

}
```



### 2.1.3、`yml`

```yml
# 商城订单更新因子配置类
handleFactorList:
  - factorHandler: one
    factorList:
      - factorKey: insuranceSkuCode
        factorType: Contain
        factorValues:
          - 2000
  - factorHandler: two
    factorList:
      - factorKey: insuranceid
        factorType: NotContain
        factorValues:
          - 1000
```

### 2.1.4、`ModifyOrderConfigData`

```java
@Getter
@Setter
@ToString
public class ModifyOrderConfigData {
    /**
     * 配置集合对象
     */
    private List<ModifyOrderHandleFactor> handleFactorList;


    /**
     * 因子
     */
    @Getter
    @Setter
    @ToString
    public static class ModifyOrderHandleFactor {
        /**
         * 因子处理器
         * 注意此处需要标注出Handler的Bean名称
         */
        private String factorHandler;

        /**
         * 因子集合
         */
        private List<ModifyOrderFactor> factorList;

    }


    /**
     * 因子
     */
    @Getter
    @Setter
    @ToString
    public static class ModifyOrderFactor {
        /**
         * 因子类型
         */
        private String factorType;
      
        /**
         * 因子Key
         */
        private String factorKey;
        /**
         * 因子值集合
         */
        private List<String> factorValues;
    }
}

```



### 2.1.5、`Test`

```java
@Test
void testLoadYaml2() {
  ModifyOrderConfigData result = YamlHelper.loadYaml("classpath:props/factor-config.yml", 
                                                     ModifyOrderConfigData.class);
  log.info("Data:{}", result);
}
```



### 2.1.6、`ModifyOrderConfigProperties`

```java
@Slf4j
@Data
@Component
public class ModifyOrderConfigProperties {

  /**
   * 配置文件运行环境
   */
  @Value("${prop.env:}")
  private String propEnv;

  /**
   * 因子执行器集合
   */
  private List<ModifyOrderConfigData.ModifyOrderHandleFactor> list;


  @PostConstruct
  private void initialize() {
      String formatPath = String.format("classpath:props/%s/factor-config.yml", propEnv);
      ModifyOrderConfigData configData = YamlHelper.loadYaml(formatPath, ModifyOrderConfigData.class);
    
      if (Objects.isNull(configData)) {
          throw new CustomException("无有效的配置信息");
      }
      List<ModifyOrderConfigData.ModifyOrderHandleFactor> configDataList = configData.getHandleFactorList();
      this.setList(configDataList);
  }
}

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
		id: 'i4orAPHNdx9YtvmX',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



