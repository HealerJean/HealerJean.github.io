---
title: SpringBoot_之_自定义Enable注解
date: 2022-01-21 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot_之_自定义Enable注解
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

# 1、`@Import`注解再次说明



> `spring boot` 的`@Import`注解可以配置三种不同的class，根据不同的场景来选择不同的注入方式
>
> 1、普通的`bean` 或者 带有 `@Configuration的bean` 直接注入     
>
> 2、实现`ImportSelector`接口注入     
>
> 3、实现` ImportBeanDefinitionRegistrar`接口注入



`pom.xml`

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.custom.proj</groupId>
    <artifactId>spring-stater-enable-custom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>spring-stater-enable-custom</name>
    <description>自定义@Enable</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.4</version>
        </dependency>
    </dependencies>

</project>
```



## 1.1、普通的`bean` 注入 

```java
@Slf4j
public class LoggerEnableService {
    public void saveLog(String logMsg){
        log.info("[CounterEnableService#saveLog] logMsg:{}", logMsg);
    }
}

```

## 1.2、导入`@Configuration`注解的配置类

### 1.2.1、`CustomConfiguration`

```java
//建议加上@Configuration
@Configuration
public class CustomConfiguration {

    @Bean
    public IronManService ironManService(){
        return new IronManService();
    }

    @Bean
    public SpiderMainService spiderMainService(){
        return new SpiderMainService();
    }
}

```

### 1.2.2、`IronManService`

```java
@Slf4j
public class IronManService {

    public void print(){
        log.info("[IronManService#print]");
    }
}

```

### 1.2.3、`SpiderMainService`

```java
@Slf4j
public class SpiderMainService {

    public void spiderPrint(){
        log.info("[SpiderMainService#spiderPrint]");
    }
}

```



## 1.3、通过`ImportSelector`的实现类间接导入

> 可以拿到所有注解的信息，可以根据不同注解的和注解的属性来返回不同的class,

### 1.3.1、`Service`

#### 1.3.1.1、`MonitorEnableAService`

```java
@Slf4j
public class MonitorEnableAService {
    public  void saveMonitor(){
        log.info("[MonitorEnableAService#saveMonitor] ok:{}");
    }
}

```

#### 1.3.1.2、`MonitorEnableBService`

```java
@Slf4j
public class MonitorEnableBService {

  public  void saveMonitor(){
    log.info("[MonitorEnableBService#saveMonitor] ok:{}");
  }
}

```



### 1.3.2、`MonitorImportSelector`

```java
public class MonitorImportSelector implements ImportSelector {

    /**
     * 可以拿到所有注解的信息，可以根据不同注解的和注解的属性来返回不同的class,
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> annotationAttributes = 
          annotationMetadata.getAnnotationAttributes(EnableCustomerService.class.getName());
        MonitorSelectorEnum monitorSelectorEnum = (MonitorSelectorEnum) annotationAttributes.get("monitor");
        switch (monitorSelectorEnum) {
            case MONITOR_ENABLE_A_SERVICE:
                return new String[]{MonitorEnableAService.class.getName()};
            case MONITOR_ENABLE_B_SERVICE:
                return new String[]{MonitorEnableBService.class.getName()};
            default:
                return new String[0];
        }
    }
}
```



## 1.4、通过`ImportBeanDefinitionRegistrar`的实现类间接导入

> 可以拿到所有注解的信息，可以根据不同注解来返回不同的class,从而达到开启不同功能的目的

### 1.4.1、`Service`

#### 1.4.1.1、`CounterEnableAService`

```java
@Slf4j
public class CounterEnableAService {

    public void add(int count ){
        log.info("CounterEnableAService#add]  count:{}", count);
    }
}

```

#### 1.4.1.2、`CounterEnableBService`

```java
@Slf4j
public class CounterEnableBService {

    public void add(int count ){
        log.info("CounterEnableBService#add]  count:{}", count);
    }
}

```



### 1.4.2、`CounterDefinitionRegistrar`

```java
public class CounterDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 这里可以拿到所有注解的信息，可以根据不同注解来返回不同的class,从而达到开启不同功能的目的
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Class beanClass = CounterEnableService.class;
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
        String beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}

```



# 2、`测试`

## 2.1、`pom.xml`

```xml

<!--  测试 自定义@Enalbe* @autoEnableConfiguration -->
<dependency>
  <groupId>com.custom.proj</groupId>
  <artifactId>spring-stater-enable-custom</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```



## 2.2、`Application`

```java
@EnableCustomerService(
  monitor = MonitorSelectorEnum.MONITOR_ENABLE_B_SERVICE)
@SpringBootApplication
public class SpringBoot_Annotation_Application {

  public static void main(String[] args) {
    SpringApplication.run(SpringBoot_Annotation_Application.class, args);
  }

}

```



## 2.3、`Controller`

```java

@RestController
@Slf4j
@RequestMapping("enable")
public class EnableController {

    @Resource
    private LoggerEnableService loggerEnableService;
    @Resource
    private CounterEnableAService counterEnableAService;
    // @Resource
    // private CounterEnableBService counterEnableBService;
    // @Resource
    // private MonitorEnableAService monitorEnableAService;
    @Resource
    private MonitorEnableBService monitorEnableBService;
    @Resource
    private SpiderMainService spiderMainService;
    @Resource
    private IronManService ironManService;

    @GetMapping("test")
    public String test() {
        loggerEnableService.saveLog("message");

        counterEnableAService.add(1);
        // counterEnableBService.add(1);

        // monitorEnableAService.saveMonitor();
        monitorEnableBService.saveMonitor();

        //configuration
        spiderMainService.spiderPrint();
        ironManService.print();
        return "ok";
    }
}

```



## 2.4、日志输出

```
2022-01-21 22:06:37.007  INFO 1353 --- [nio-8888-exec-1] c.c.proj.service.LoggerEnableService     : [LoggerEnableService#saveLog] logMsg:message
2022-01-21 22:06:37.007  INFO 1353 --- [nio-8888-exec-1] c.c.p.s.service.CounterEnableAService    : CounterEnableAService#add]  count:1
2022-01-21 22:06:37.007  INFO 1353 --- [nio-8888-exec-1] c.c.p.r.service.MonitorEnableBService    : [MonitorEnableBService#saveMonitor] ok:{}
2022-01-21 22:06:37.008  INFO 1353 --- [nio-8888-exec-1] c.c.p.c.service.SpiderMainService        : [SpiderMainService#spiderPrint]
2022-01-21 22:06:37.008  INFO 1353 --- [nio-8888-exec-1] c.c.p.c.service.IronManService           : [IronManService#print]

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
		id: 'xsh42Xq7BQ0bJCEl',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



