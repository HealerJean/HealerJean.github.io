---
title: Junit测试SpringBoot中
date: 2018-01-01 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Junit测试SpringBoot中
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            



## 1、sprinboot中直接写测试用例



### 1.1、如果直接输入`@Test`就会自动让我将这个版本放到pom中去

![WX20190131-132859](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190131-132859.png)

#### 1.1.1、然后`pom.xml`中就出现了下面的


```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>RELEASE</version>
    <scope>compile</scope>
</dependency>

```

#### 1.1.2、如果运行报下面错误

主要是上面的依赖导致的错误


```
Exception in thread "main" java.lang.NoSuchMethodError: org.junit.platform.commons.util.ReflectionUtils.getDefaultClassLoader()Ljava/lang/ClassLoader;
    at org.junit.platform.launcher.core.ServiceLoaderTestEngineRegistry.loadTestEngines(ServiceLoaderTestEngineRegistry.java:30)
    at org.junit.platform.launcher.core.LauncherFactory.create(LauncherFactory.java:53)
    at com.intellij.junit5.JUnit5IdeaTestRunner.createListeners(JUnit5IdeaTestRunner.java:39)
    at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:49)
    at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
    at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
    
```

### 1.2、使用新的依赖


```xml

<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>

```



## 2、SpringBoot真正测试

### 2.1、maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```



### 2.2、开始测试

```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class)
public class MainTest {

    @Autowired
    private CacheService cacheService ;

    @Test
    public void test(){
        while (true){
            boolean lock = cacheService.lock("TEST", 1, TimeUnit.MINUTES);
            if (lock){
                log.info("锁状态【{}】", lock);
            }
        }
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
		id: 'r1Xa5mPfjHdMlWC7',
    });
    gitalk.render('gitalk-container');
</script> 
<!-- Gitalk end -->

