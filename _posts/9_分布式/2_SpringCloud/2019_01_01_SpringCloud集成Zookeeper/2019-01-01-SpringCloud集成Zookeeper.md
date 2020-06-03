---
title: SpringCloud集成Zookeeper
date: 2019-01-01 03:33:00
tags: 
- SpringCloud
category: 
- SpringCloud
description: SpringCloud集成Zookeeper
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、Zookeeper注册中心搭建

## 1.1、下载

[http://www.apache.org/dyn/closer.cgi/zookeeper/  ](http://www.apache.org/dyn/closer.cgi/zookeeper/)



## 1.2、启动



**1、进入conf目录将 zoo_sample.cfg 改名为 zoo.cfg。**    

```
修改dataDir目录
dataDir=D:\\programFiles\\zookeeper-3.4.14\\zookeeper-3.4.14\\data
```



**2、创建文件夹data目录**

![image-20200513151357705](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200513151357705.png)





**3、进入bin目录双击zkServer.cmd**   

![image-20200513151547903](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200513151547903.png)



# 2、服务提供者

## 2.1、`pom.xml`依赖

```xml
<!--zookeeper服务注册-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
</dependency>
```



## 2.2、`application.properties`：配置文件

```properties
spring.application.name=hlj-zookeeper-server-consumer
server.port=3001

spring.cloud.zookeeper.connectString=127.0.0.1:2181
#3spring.cloud.zookeeper.discovery.instanceHost=127.0.0.1
#spring.cloud.zookeeper.discovery.instancePort=${server.port}
```



## 2.3、启动类

```java
//支持服务发现
@EnableDiscoveryClient
@SpringBootApplication
public class ZookeeperServerProvider_7011_Application {

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperServerProvider_7011_Application.class, args);
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
		id: 'RLgFI38f0CuZmydW',
    });
    gitalk.render('gitalk-container');
</script> 

