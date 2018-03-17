---
title: log4j和sfl4j和lobback到底有什么区别
date: 2018-03-15 18:33:00
tags: 
- Log
category: 
- Log
description: log4j和sfl4j和lobback到底有什么区别
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages

<font color="red"></font>
-->

## 前言

首先我要说的是，我公司用的是logback，并且将日志输出到额graylog中

日志当然是我们开发人员最常用的东西了，不管是调试日志，还是后期检查bug，对系统进行维护都是有及其重要的作用。但是很多人可能还是分不清 那么多日志类到底有什么区别，其实说白了，就一句话

比如：slf4j 是打日志的。可以使用各种日志系统存储。Log4j和logback就是那个日志存储系统(它自带打日志，因为自己本身就是一个日志系统。所以不能够切换日志系统)。但是slf4j 是可以随时切换到任何日志系统

一般我们打日志都用SLF4J进行打日志吧！！！


## 开始吧！！！

打印方式

sfl4j

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

Logger logger = LoggerFactory.getLogger(ControllerConfig.class);

```

Log4j


```
import org.apache.log4j.Logger;

Logger logger  = Logger.getLogger(CardBagController.class);

```


   SLF4J：即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。SLF4J是一个用于日志系统的简单Facade，允许最终用户在部署其应用时使用其所希望的日志系统(Log4j logback)。
   
    在使用SLF4J的时候，不需要在代码中或配置文件中指定你打算使用那个具体的日志系统，SLF4J提供了统一的记录日志的接口，<font color="red">只要按照其提供的方法记录即可，最终日志的格式、记录级别、输出方式等通过具体日志系统的配置来实现，因此可以在应用中灵活切换日志系统。</font>
    
 
    log4j：没什么好说的，就是一个打日志，并且有日志系统

    logback和log4j是非常相似的，Logback的内核重写了，在一些关键执行路径上性能提升10倍以上。而且logback不仅性能提升了，初始化内存加载也更小。说白了也是个日志系统，存储日志


### logback

在工程的src根目录下创建logback.xml文件，springboot工程使用logback-spring.xml(名字是固定的和Log4j是不是有点像呢)




```

<dependency>
    <groupId>com.github.pukkaone</groupId>
    <artifactId>logback-gelf</artifactId>
    <version>1.1.9</version>
</dependency>

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.1.6</version>
</dependency>

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.1.6</version>
</dependency>

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-access</artifactId>
    <version>1.1.6</version>
</dependency>

```
springboot 使用介绍


```

logging.level.root=info
logging.level.org.springframework=error
logging.level.org.mybatis=error

dev
		logging.config=classpath:logback-dev.xml
prod
		logging.config=classpath:logback-prod.xml

```

logback-dev.xml


```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />
    <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>

```

logback-prod.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="logging" class="com.github.pukkaone.gelf.logback.GelfAppender">
        <graylogHost>localhost</graylogHost>
        <originHost>admin</originHost>
        <levelIncluded>true</levelIncluded>
        <locationIncluded>false</locationIncluded>
        <loggerIncluded>true</loggerIncluded>
        <markerIncluded>false</markerIncluded>
        <mdcIncluded>false</mdcIncluded>
        <threadIncluded>false</threadIncluded>
        <facility>gelf-java</facility>
        <additionalField>application=admin</additionalField>
        <additionalField>environment=prod</additionalField>
    </appender>

    <root level="info">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="logging" />
    </root>
</configuration>


```


## [logback代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_16_2_Docker%E5%AE%89%E8%A3%85graylog%E5%92%8C%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B/com-hlj-graylog.zip)





<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|



<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'eDso3iazNfRQzpbC',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

