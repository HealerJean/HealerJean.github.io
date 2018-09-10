---
title: Docker安装graylog和详解
date: 2018-03-16 14:33:00
tags: 
- Docker
- GrayLog
- Log
category: 
- Docker
- GrayLog
- Log
description: Docker安装graylog和详解
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
-->

## 前言

graylog我就不想介绍了，如果你看到graylog就知道它是一个日志存储服务器web

## 准备
 安装docker
 安装docker compose
 

## 1、安装graylog

### 1、创建目录<br/>

`/usr/local/graylog ` **注意自己服务器的id**
:i
该目录下新建文件   `graylog.yml` 或者 `compose.yml` 

写入内容：

```
version: '2'
services:
  # MongoDB: https://hub.docker.com/_/mongo/
  mongodb:
    image: mongo:3
  # Elasticsearch: https://www.elastic.co/guide/en/elasticsearch/reference/5.6/docker.html
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:5.6.3
    environment:
      - http.host=0.0.0.0
      - transport.host=localhost
      - network.host=0.0.0.0
      # Disable X-Pack security: https://www.elastic.co/guide/en/elasticsearch/reference/5.6/security-settings.html#general-security-settings
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    mem_limit: 1g
  # Graylog: https://hub.docker.com/r/graylog/graylog/
  graylog:
    image: graylog/graylog:2.4.0-1
    environment:
      # CHANGE ME!
      - GRAYLOG_PASSWORD_SECRET=somepasswordpepper
      # Password: admin
      - GRAYLOG_ROOT_PASSWORD_SHA2=8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
      - GRAYLOG_WEB_ENDPOINT_URI=http://127.0.0.1:9000/api
    links:
      - mongodb:mongo
      - elasticsearch
    depends_on:
      - mongodb
      - elasticsearch
    ports:
      # Graylog web interface and REST API
      - 9000:9000
      # Syslog TCP
      - 514:514
      # Syslog UDP
      - 514:514/udp
      # GELF TCP
      - 12201:12201
      # GELF UDP
      - 12201:12201/udp

```

### 2、开始安装

1、如果创建的文件名字是`graylog.yml` 使用命令 （-d表示后台运行）

```
sudo docker-compose -f graylog.yml up -d
```
2、如此创建的文件是 `compose.yml` 

```
docker-compose up -d 
```


## 2、浏览器中打开,密码admin/admin

[ http://localhost:9000/]( http://localhost:9000/)

![WX20180316-141255@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-141255@2x.png)



## 3、测试

### 3.1、查看运行的镜像 协议观察，可以看到12201 udp这样才算成功

`docker ps`

![WX20180316-141745@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-141745@2x.png)

### 3.2、使用http测试是否成功

#### 3.2.1、新建input http input

![WX20180316-141909@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-141909@2x.png)


#### 3.2.2、打开一个终端，输入下面的命令

```
curl -XPOST http://localhost:12201/gelf -p0 -d '{"message":"hello这是一条消息", "host":"127.0.0.1", "facility":"test", "topic": "meme"}'
```
打开浏览器观察信息,点击http-input之后


![WX20180316-142346@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-142346@2x.png)

## 2、springBoot集成graylog

1、创建 gref udp input

![WX20180316-155922@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-155922@2x.png)

### 1、使用logback进行日志的封装

```
<!--logback-->
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

### 2、springBoot配置

1、application.properties

```
spring.application.name=tools
server.port=8085
spring.profiles.active=prod


#log level
logging.level.root=info
logging.level.org.springframework=info
logging.level.org.mybatis=error

```
2、application-prod.properties

```
server.port=8080

#logging properties
logging.config=classpath:logback-prod.xml

```
### 3、logback-prod.xml 配置如下

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>


    <appender name="logging" class="com.github.pukkaone.gelf.logback.GelfAppender">
        <graylogHost>127.0.0.1</graylogHost>
        <originHost>tools</originHost>  <!--source,来源-->
        <levelIncluded>true</levelIncluded>
        <locationIncluded>false</locationIncluded>
        <loggerIncluded>true</loggerIncluded>
        <markerIncluded>false</markerIncluded>
        <mdcIncluded>false</mdcIncluded>
        <threadIncluded>false</threadIncluded>
        <facility>gelf-java</facility> <!--facility，随便写呗-->
        <additionalField>application=tools</additionalField> <!--application，应用-->
        <additionalField>environment=prod</additionalField> <!--environment，环境-->
        <additionalField>managername=HealerJean</additionalField> <!--我自己加的managername，环境-->

    </appender>


    <root level="info">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="logging" />
    </root>
</configuration>

```

### 4、测试controller，下面有个异常哦


```
@Slf4j
@RestController
public class TestController {

    @GetMapping("log")
    public String log(){

        log.error("error");
        log.warn("warn");
        log.info("info");
        log.debug("debug");
        int i = 1/0;
        return "success";
    }
}
```

### 5、开始启动springBoot的朋友们。观察浏览器graylog<br/>

可以看到日志和控制台是一样的

![WX20180316-144300@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-144300@2x.png)

## 3、详说graylog，里面的内容标签可以自己定义

### 3.1、分析内容

#### 3.1.1、异常

logback-prod.xml 中很多的信息都是固定的，除了message、timestamp、logger，

下面是异常的输出信息，可以看到里面有fullMessage自动出现的，很详细，开发人员可以根据里面的具体信息针对上线的异常进行分析和解决。

![WX20180316-144638@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-144638@2x.png)

#### 3.1.2、正常

有时候因为业务的需要我们会自己去生成log日志。比如ddkj的回调接口。和别人联调的时候，对方调用自己的接口，如果看有没有调用到自己，最好的方式就是打一个日志。

##### 浏览器打开 [http://localhost:8080/log](http://localhost:8080/log)

继续观察graylo日志，可以看到dubug的日志没有出现，因为我们配置文件中设置的级别是info

![WX20180316-145454@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-145454@2x.png)

### 3.2、条件搜索 

一般我们在控制面板中的标签都是可以搜索的，比如source ,连接查找关系一定要用大写AND。一般AND后面默认就是message中的内容


```
source:tools AND warn
```

![WX20180316-151056@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-151056@2x.png)



### 3.3、饼状图查看

![WX20180316-151904@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-151904@2x.png)


## 4、[代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_16_2_Docker%E5%AE%89%E8%A3%85graylog%E5%92%8C%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B/com-hlj-graylog.zip)


## 总结下

至此，如果公司要用的话，我觉得大家应该也能够用的非常顺利，祝大家工作顺利。如果那里不懂的可以即使留言哦












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
		id: 'enBuXMrTvxUHcidh',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

