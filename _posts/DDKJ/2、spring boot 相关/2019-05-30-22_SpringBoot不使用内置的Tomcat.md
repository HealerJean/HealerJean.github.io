---
title: SpringBoot不使用内置的Tomcat
date: 2019-05-30 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot不使用内置的Tomcat
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    

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
        return builder.sources(ScfManagerWebApplication.class);
    }
}

```













<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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

