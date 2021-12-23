---
title: SpringBoot中properties读取pom中的值
date: 2019-05-14 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot中properties读取pom中的值
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、`pom.xml`

```xml
<properties>
    <pom.var.name>pomValue</pom.var.name>
</properties>




<build>
    <!-- 定义资源目录 -->
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <!--filtering 这个配置的意思是过滤上面指定属性文件中的占位符，占位符是${变量名称}这样的形式，
						  maven会自动读取配置文件，然后解析其中的占位符，使用上面pom文件中定义的属性进行替换 -->
            <filtering>true</filtering>
        </resource>
        <resource>
            <directory>${profiles.active}</directory>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>

        
        
        
        <plugins>
            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <encoding>utf-8</encoding>
                    <useDefaultDelimiters>true</useDefaultDelimiters>
                </configuration>
            </plugin>
        </plugins>
        </build>
```



# 2、`demo.properties`  

![image-20200724151323802](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200724151323802.png)



```properties
demo.name=healerjean
demo.age=12
demo.pomVal=pomValue
demo.version=1.0.0-SNAPSHOT
```





# 3、使用

```java
@Configuration
@Slf4j
public class Customonfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @PostConstruct
    public void init() {
        log.info("demo.name ==> {}" , environment.getProperty("demo.name"));
        log.info("demo.version ==> {}" , environment.getProperty("demo.version"));
    }

}

```







![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





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
		id: 'LHbCf9vyGAomhe1a',
    });
    gitalk.render('gitalk-container');
</script> 
