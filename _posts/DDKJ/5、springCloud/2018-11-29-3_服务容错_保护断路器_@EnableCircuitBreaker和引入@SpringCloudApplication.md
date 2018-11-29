---
title: 3_服务容错_保护断路器_@EnableCircuitBreaker和引入@SpringCloudApplication
date: 2018-11-29 03:33:00
tags: 
- SpringCloud
category: 
- SpringCloud
description: 服务容错_保护断路器_@EnableCircuitBreaker和引入@SpringCloudApplication
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->
**
## 前言

前面做了一个简单的服务 注册。服务发现，服务提供者和消费者的项目，现在我们还是准备之前的项目代码

## 1、 服务容错保护，准备的项目工程


>1、服务注册中心 ，端口为1111
>2、服务提供者，端口为8080，8081
>3、服务消费者 端口为9000

## 2、在服务消费者中引入依赖包

>hystrix 对应的中文名字是“豪猪”，豪猪周身长满了刺，能保护自己不受天敌的伤害，代表了一种防御机制，这与hystrix本身的功能不谋而合，因此Netflix团队将该框架命名为Hystrix，并使用了对应的卡通形象做作为logo。<br/>



---- 

>在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是Hystrix需要做的事情。Hystrix提供了熔断、隔离、Fallback、cache、监控等功能，能够在一个、或多个依赖同时出现问题时保证系统依然可用。



```java
   <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-hystrix</artifactId>
   </dependency> 

```


```java

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>

   <groupId>com.didispace</groupId>
   <artifactId>ribbon-consumer</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <packaging>jar</packaging>

   <name>ribbon-consumer</name>
   <description>Demo project for Spring Boot</description>

   <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>1.3.7.RELEASE</version>
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
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-test</artifactId>
         <scope>test</scope>
      </dependency>

      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-eureka</artifactId>
      </dependency>

      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-ribbon</artifactId>
      </dependency>

      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-actuator</artifactId>
      </dependency>

      <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-hystrix</artifactId>
      </dependency>

   </dependencies>

   <dependencyManagement>
      <dependencies>
         <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Brixton.SR5</version>
            <type>pom</type>
            <scope>import</scope>
         </dependency>
      </dependencies>
   </dependencyManagement>

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


## 3、注解开启断路器功能@EnableCircuitBreaker

#### 解释:这里还可以使用 @ SpringCloudApplication 注解代替上面三个注解，由此也可以得到spring Cloud标准应用包含服务发下和断路器



```java

@EnableCircuitBreaker //开启断路器功能
@EnableDiscoveryClient //开启服务发现客户端，
@SpringBootApplication
////@SpringCloudApplication //可以取代上面三个
public class ConsumerApplication {

   @Bean
   @LoadBalanced
   RestTemplate restTemplate() {
      return new RestTemplate();
   }
   public static void main(String[] args) {
      SpringApplication.run(ConsumerApplication.class, args);
   }

}

//或者下面这个
@ SpringCloudApplication 
public class ConsumerApplication {

   @Bean
   @LoadBalanced
   RestTemplate restTemplate() {
      return new RestTemplate();
   }
   public static void main(String[] args) {
      SpringApplication.run(ConsumerApplication.class, args);
   }

}

```

## 4、@HystrixCommand(方法内部报错之后，自动去找回滚的方法)

### 4.1、新建service类，用来添加指定回调方法


```java

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallBack")
    public String hello(){
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello",String.class).getBody();
    }


    public String helloFallBack(){
        return "error";
    }
}
```

## 5、修改之前服务消费者的Controller方法


```java
@RestController
public class ConsumerController {


    @Autowired
    HelloService helloService;

    @ResponseBody
    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    public String helloConsumer() {
        return helloService.hello();
    }

}


```

## 6、开始验证断路器的实现的回调逻辑，

### 6.1、启动所有的服务

#### 访问[http://localhost:9000/ribbon-consumer ](http://localhost:9000/ribbon-consumer )
 

### 6.2、在8080 8081 这两个服务提供者工作的时候，返回的结果都是一样的

![WX20181129-164811@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181129-164811@2x.png)

### 6.3、直接断开8081服务提供者

<font  color="red" size="4">  

这个时候我们将8081的服务提供者挂掉，继续访问，发现一会正常，一会显示error，但是时间长了，就还是只会显示hello ，出现这种情况，应该是注册中心没有及时检测到挂掉了8081，还继续提供给消费者服务。但是时间长了，就肯定原型毕露

 </font>

![WX20181129-164940@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181129-164940@2x.png)

## 7、模拟服务阻塞，添加延迟

#### 解释：(hystrix默认超时2000毫秒) （启动所有的服务）

### 7.1、服务提供者中添加延迟时间设置为3000毫秒

```java

/**
 * 2、断路器，模拟服务阻塞
 */
@RequestMapping(value = "/hello", method = RequestMethod.GET)
public String hello() throws InterruptedException {

   ServiceInstance instance = client.getLocalServiceInstance();


   int sleepTime = new Random().nextInt(3000);
   logger.info("处理线程等待 "+sleepTime+" 秒");
   Thread.sleep(sleepTime);

   logger.info("/hello, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
   return "Hello World";
}

```


### 7.2、结果 ：和上面的测试是一样的，也是有错误，有正常



[HealerJean-代码下载](https://github.com/HealerJean/com-hlj-springcloud/tree/master/3)



<br/><br/><br/>
<font color="red"> 感兴趣的，欢迎添加博主微信， </font><br/>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
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

