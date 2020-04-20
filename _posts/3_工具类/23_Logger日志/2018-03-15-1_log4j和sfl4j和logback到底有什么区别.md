---
title: log4j和sfl4j和lobback到底有什么区别
date: 2018-03-15 18:33:00
tags: 
- Log
category: 
- Log
description: log4j和sfl4j和lobback到底有什么区别
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)       

首先我要说的是，我公司用的是logback，并且将日志输出到额graylog中

日志当然是我们开发人员最常用的东西了，不管是调试日志，还是后期检查bug，对系统进行维护都是有及其重要的作用。但是很多人可能还是分不清 那么多日志类到底有什么区别，其实说白了，就一句话

比如：slf4j 是打日志的。可以使用各种日志系统存储。Log4j和logback就是那个日志存储系统(Log4j它自带打日志，因为自己本身就是一个日志系统。所以不能够切换日志系统)。但是slf4j 是可以随时切换到任何日志系统

一般我们打日志都用SLF4J进行打日志吧！！！


## 开始吧！！！

打印方式

sfl4j

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

Logger logger = LoggerFactory.getLogger(ControllerConfig.class);

```

Log4j


```java
import org.apache.log4j.Logger;

Logger logger  = Logger.getLogger(CardBagController.class);

```

   SLF4J：即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。SLF4J是一个用于日志系统的简单Facade，允许最终用户在部署其应用时使用其所希望的日志系统**(Log4j logback)。**        

在使用SLF4J的时候，不需要在代码中或配置文件中指定你打算使用那个具体的日志系统，SLF4J提供了统一的记录日志的接口，<font color="red">只要按照其提供的方法记录即可，最终日志的格式、记录级别、输出方式等通过具体日志系统的配置来实现，因此可以在应用中灵活切换日志系统。</font>            

log4j：没什么好说的，就是一个打日志，并且有日志系统       

logback和log4j是非常相似的，Logback的内核重写了，在一些关键执行路径上性能提升10倍以上。而且logback不仅性能提升了，初始化内存加载也更小。**说白了也是个日志系统，存储日志**


### logback

在工程的src根目录下创建logback.xml文件，springboot工程使用logback-spring.xml(名字是固定的和Log4j是不是有点像呢)



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


```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
<!--学习 https://blog.csdn.net/ZYC88888/article/details/85060315-->

    <!--
     格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度 %msg：日志消息，%n是换行符 -->
    -->
    <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg %n "/>

    <property name="LOG_PATH" value="/Users/healerjean/Desktop/logs"/>
    <property name="FILE_PATH_INFO"  value="${LOG_PATH}/hlj-logback.log"/>
    <property name="FILE_PATH_ERROR" value="${LOG_PATH}/hlj-logback-error.log"/>

    <!--控制台-->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>


    <appender name="FILE-INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--日志文件输出的文件名 -->
        <File>${FILE_PATH_INFO}</File>
        <!--滚动日志 基于时间和文件大小-->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- 滚动日志文件保存格式 i是超出文件大小MaxFileSize 从0开始起步，
            如果超过了最大的totalSizeCap，就会全部删除，重新开始-->
            <FileNamePattern>${FILE_PATH_INFO}.%d{yyyy-MM-dd}.%i.log</FileNamePattern>
            <MaxFileSize>1MB</MaxFileSize>
            <totalSizeCap>5GB</totalSizeCap>
            <!--日志最大的历史 10天 -->
            <MaxHistory>10</MaxHistory>
        </rollingPolicy>
        <!-- 按临界值过滤日志：低于INFO以下级别被抛弃 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <encoder>
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符 -->
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>


    <appender name="FILE-ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${FILE_PATH_ERROR}</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <FileNamePattern>${FILE_PATH_ERROR}.%d{yyyy-MM-dd}.%i.log</FileNamePattern>
            <MaxFileSize>60MB</MaxFileSize>
            <totalSizeCap>5GB</totalSizeCap>
            <MaxHistory>10</MaxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>



    <!--以配置文件application.properties 中为主，如果配置文件中不存在以它为主-->
    <root level="info">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE-ERROR"/>
        <appender-ref ref="FILE-INFO"/>
    </root>
</configuration>





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
		id: 'eDso3iazNfRQzpbC',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

