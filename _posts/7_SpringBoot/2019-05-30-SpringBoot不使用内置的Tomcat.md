---
title: SpringBoot不使用内置的Tomcat
date: 2019-05-30 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot不使用内置的Tomcat
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             



刚创建好的SpringBoot项目，默认给我们提供了内置的Tomcat，只要运行`Application `就可以启动项目了，因为使用了注解`EnableConfiguration` ,这里我们使用外置的tomcat来启动

### 1、修改打包形式

```xml
<packaging>war</packaging>

```

### 2、移除嵌入式tomcat插件

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- 移除嵌入式tomcat插件 -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```



### 3、添加servlet-api的依赖

```xml
<!-- servlet -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>
```



### 4、启动类

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```



### 5、SpringBootStartApplication 

```java

   /**
     * 使用内嵌的tomcat时SpringbootdemoApplication的main方法启动的方式
     这里需要类似于web.xml的配置方式来启动spring上下文，因此重写SpringBootServletInitializer的configure方法，在Application类的同级添加一个SpringBootStartApplication类
     */

public class ScfManagerWebStartApplication extends SpringBootServletInitializer {

    @Override
 
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
              // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(Application.class);
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
		id: 's4iK96yRkt2O8aE5',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

