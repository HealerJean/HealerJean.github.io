---
title: 3_服务容错_保护断路器@EnableCircuitBreaker和引入@SpringCloudApplication
date: 2018-11-29 03:33:00
tags: 
- SpringCloud
category: 
- SpringCloud
description: 服务容错_保护断路器_@EnableCircuitBreaker和引入@SpringCloudApplication
---





**前言**    

前面做了一个简单的服务 注册。服务发现，服务提供者和消费者的项目，现在我们还是准备之前的项目代码





# 1、`Hystrix`：容错保护  （消费者：3001）

> > `hystrix` 对应的中文名字是“豪猪”，豪猪周身长满了刺，能保护自己不受天敌的伤害，代表了一种防御机制，这与`hystrix`本身的功能不谋而合，因此`Netflix`团队将该框架命名为`Hystrix`，并使用了对应的卡通形象做作为logo。
>
> ​     
>
> 在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是`Hystrix`需要做的事情。      
>
> **`Hystrix`提供了熔断、隔离、`Fallback`、`cache`、监控等功能，能够在一个、或多个依赖同时出现问题时保证系统依然可用。** 



## 1.1、`pom.xml`添加依赖  



```xml
<!--hystrix 容错保护-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.healerjean.proj</groupId>
        <artifactId>hlj-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>hlj-server-consumer-3001</artifactId>
    <version>${project.healerjean.version}</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <!--eureka 客户端，处理服务的注册和发现-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

        <!--ribbon 消费服务，它同时也作为辅助均衡器-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>


        <!--hystrix 容错保护-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
        </dependency>

        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
        </dependency>

        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <!--StringUtils-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```



## 1.2、启动类：

> `@EnableCircuitBreaker`：**开启断路器功能**    
>
> `@ SpringCloudApplication`：可以代替如下三个 
>
> > `@EnableCircuitBreaker`  //开启断路器功能
> > `@EnableDiscoveryClient  `//开启服务发现客户端，
> > `@SpringBootApplication`



```java
package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker //开启断路器功能
@EnableDiscoveryClient //支持服务发现
@SpringBootApplication
////@SpringCloudApplication //可以取代上面三个
public class ServerConsumer_3001_Application {

    //开启客户端负载均衡
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerConsumer_3001_Application.class, args);
    }

}

```



## 1.3、`@HystrixCommand`：服务降级

> `@HystrixCommand`：**方法内部报错之后，不会抛出异常，自动去找回滚的方法**



### 1.3.1、`ConsumeService`  

```java
public interface ConsumeService {

    String testFallBack();
}

```



### 1.3.2、`ConsumeServiceImpl`

```java
@Service
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${hlj.server.providerName}")
    private String serverProviderName;

    @Override
    @HystrixCommand(fallbackMethod = "fallBack")
    public String testFallBack() {
        return restTemplate.getForEntity(
            "http://" + serverProviderName + "/api/provider/connect/", 
            String.class).getBody();
        
        // int i = 1/0;
        // return "success";
    }

    public String fallBack() {
        return "testFallBack 方法不可用，服务降级";
    }

}
```





## 1.4、访问接口 ：启动所有的服务测试  

> 服务注册中心：` hlj-eureka-server-1111`
> 服务注册中心：` hlj-eureka-server-1112`   
>
> 服务提供者：`hlj-server-provider-2001`
> 服务提供者：`hlj-server-provider-2002`
>
> 服务消费者：`hlj-server-consumer-3001`



### 1.4.1、访问接口 

```java
http://127.0.0.1:3001/api/consumer/hystrix/fallBack
```

**接口返回**   

> 服务提供者：2002

```json
{
    "host": "localhost",
    "port": 2002,
    "metadata": {},
    "serviceId": "hlj-server-provider",
    "secure": false,
    "uri": "http://localhost:2002"
}
```

> 服务提供者：2001  

```json
{
    "host": "localhost",
    "port": 2001,
    "metadata": {},
    "secure": false,
    "serviceId": "hlj-server-provider",
    "uri": "http://localhost:2001"
}
```



## 1.5、断路器测试    

### 1.5.1、这个时候断开服务提供者：2002  

> 这个时候我们将2002 的服务提供者挂掉，继续访问，发现一会正常，一会显示错误信息。     
>
> 但是时间长了，就还是只会显示正常 ，出现这种情况，应该是注册中心没有及时检测到挂掉了2002，还继续提供给消费者服务。但是时间长了，就肯定原型毕露   



**接口返回**   

> 服务提供者：2002

```json
testFallBack 方法不可用，服务降级
```

> 服务提供者：2001  

```json
{
    "host": "localhost",
    "port": 2001,
    "metadata": {},
    "secure": false,
    "serviceId": "hlj-server-provider",
    "uri": "http://localhost:2001"
}
```



### 15.2、`Hystrix`默认超时2000毫秒   

> 如果某个服务提供者超时了，就会进行容错保护，按照上面的测试













<font color="red"> 感兴趣的，欢迎添加博主微信， </font>     

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
		id: 'KSLv1QXsfRrj08Bl',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

