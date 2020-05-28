---
title: 4_Feign声明式服务调用和Feign使用Hystrix断路器
date: 2018-11-29 03:33:00
tags: 
- SpringCloud
category: 
- SpringCloud
description: Feign声明式服务调用和Feign使用Hystrix断路器
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            



接着上一篇的代码继续 



# 1、Feign声明式服务调用

## 1.1、`pom`：依赖



```xml
<!--feign 声明式服务调用-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
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

        <!--feign 声明式服务调用-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
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

## 1.2、启动类  



> `@EnableFeignClients` ：注解开启 spring cloud feign申明式服务调用功能



```java
@EnableFeignClients //开启声明式服务调用 feign(假装)
@SpringCloudApplication //可以取代下面三个
// @EnableCircuitBreaker //开启断路器功能
// @EnableDiscoveryClient //支持服务发现
// @SpringBootApplication
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



## 1.3、服务提供者_2001  

### 1.3.1、`ProviderFeignController`

```java
package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020/4/8  17:03.
 * @Description
 */
@Api(description = "服务提供者_2001_声明式服务调用Controller")
@RestController
@RequestMapping("api/provider/feign")
@Slf4j
public class ProviderFeignController extends BaseController {


    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * reequestParam 参数接收
     */
    @GetMapping("reequestParam")
    public String reequestParam(@RequestParam("name") String name) {
        log.info("声明式服务调用Controller--------reequestParam 参数接收--------请求参数：{}", name);
        return "声明式服务调用Controller--------reequestParam 参数接收--------成功 ：" + name;
    }

    /**
     * requestHeader 参数接收
     */
    @GetMapping("requestHeader")
    public UserDTO requestHeader(@RequestHeader("name") String name, 
                                 @RequestHeader("id") Long id) {
        log.info("声明式服务调用Controller--------requestHeader 参数接收--------请求参数：{}", name);
        return new UserDTO(id, name);
    }


    /**
     *  requestBody 参数接收
     */
    @PostMapping("requestBody")
    public UserDTO requestBody(@RequestBody UserDTO userDTO) {
        log.info("声明式服务调用Controller--------requestBody 参数接收--------请求参数：{}", userDTO);
        return userDTO;
    }

    /**
     *  post 参数接收(接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("post")
    public UserDTO post(UserDTO userDTO) {
        log.info("声明式服务调用Controller--------post 参数接收--------请求参数：{}", userDTO);
        return userDTO;
    }

}



```



## 1.4、服务消费者_3001



### 1.4.1、`FeignServerService`：声明式服务调用接口    

> 1、通过`@FeignClient` 注解指定服务名称来绑定服务    
>
> 2、后通过`SpringMVc` 注解绑定服务提供者提供的REST接口，和服务提供者中的url是一模一样哦   



```java
package com.healerjean.proj.service;

import com.healerjean.proj.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName FeIgnProviderService
 * @date 2020/4/9  12:40.
 * @Description
 */

@FeignClient(name = "HLJ-SERVER-PROVIDER") //服务提供者应用名
public interface FeignServerService {


    @GetMapping("api/provider/feign/reequestParam")
    String reequestParam(@RequestParam("name") String name);


    @GetMapping("api/provider/feign/requestHeader")
    UserDTO requestHeader(@RequestHeader("id") Long id, @RequestHeader("name") String name);


    @PostMapping("api/provider/feign/requestBody")
    UserDTO requestBody(@RequestBody UserDTO userDTO);

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("api/provider/feign/post")
    UserDTO post(UserDTO userDTO);

}

```





### 1.4.2、`ConsumerFeignController`  

```java
package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.FeignProviderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName FeignConsumerController
 * @date 2020/4/14  16:09.
 * @Description
 */
@Api(description = "服务消费者_3001_控制器-声明式服务调用Controller")
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerFeignController {


    @Autowired
    private FeignServerService feignServerService;

    /**
     * 测试申明式服务调用
     */
    @GetMapping(value = "feign/invokeInter")
    public String invokeInter() {
        String str1 = feignServerService.reequestParam("healerjean");
        UserDTO userDTO1 = feignServerService.requestHeader(1L, "healerjean");
        UserDTO userDTO2 = feignServerService.requestBody(new UserDTO(1L, "healerjean"));
        UserDTO userDTO3 = feignServerService.post(new UserDTO(1L, "healerjean"));
        return "str1 = " + str1 + "\n "
                + "str2 = " + JsonUtils.toJsonString(userDTO1) + "\n "
                + "str3 = " + JsonUtils.toJsonString(userDTO2) + "\n "
                + "str4 = " + JsonUtils.toJsonString(userDTO3) + "\n ";
    }
}

```







## 1.5、访问测试  

```http
http://127.0.0.1:3001/api/consumer/feign/invokeInter
```

**接口返回：**

```json
str1 = 声明式服务调用Controller--------reequestParam 参数接收--------成功 ：healerjean
 str2 = {"id":1,"name":"healerjean"}
 str3 = {"id":1,"name":"healerjean"}
 str4 = {}
```





# 2、继承特性，重构解决User不同位置的问题

>1、这个时候我们会发现，在声明式服务中的User和服务提供者中的User的内容一抹一样，但是位置却不是一样的，所以这样很不利于维护，因为修改了一处，还要继续去修改另外的一处。   
>
>2、而且在声明式`@FeginClien`t中提供的接口其实服务提供者也可以使用。所以，请看下面



## 2.1、新建`Moudule`：`hlj-service-api`

### 2.1.1、`pom.xml`依赖

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

    <artifactId>hlj-service-api</artifactId>
    <version>${project.healerjean.version}</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <!--feign 声明式服务调用-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
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
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.10.3</version>
            <scope>compile</scope>
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




### 2.1.2、`UserDTO`

```java
package com.healerjean.proj.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String name;

    public UserDTO() {
    }

    public UserDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}


```



### 2.1.1、重构声明式服务调用接口：`FeignServerService`    

> `@RequestMapping("api/provider/feign")` 服务提供者的接口地址前缀  



```java
package com.healerjean.proj.service;

import com.healerjean.proj.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName FeIgnProviderService
 * @date 2020/4/9  12:40.
 * @Description
 */

@RequestMapping("api/provider/feign")
public interface FeignServerService {

    @GetMapping("reequestParam")
    String reequestParam(@RequestParam("name") String name);

    @GetMapping("requestHeader")
    UserDTO requestHeader(@RequestHeader("id") Long id, 
                          @RequestHeader("name") String name);

    @PostMapping("requestBody")
    UserDTO requestBody(@RequestBody UserDTO userDTO);

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("post")
    UserDTO post(UserDTO userDTO);

}

```



## 2.2、服务提供者_2001  

### 2.2.1、`ProviderFeignController`

> 创建`ProviderFeignController`类继承 `FeignServerService`没有必要在`@RequestMapping`,但是接口实现的方法上相关注解是要写的哦，   



```java
package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.FeignServerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020/4/8  17:03.
 * @Description
 */
@Api(description = "服务提供者_2001_声明式服务调用Controller")
@RestController
@Slf4j
public class ProviderFeignController extends BaseController implements FeignServerService {

    /**
     * reequestParam 参数接收
     */
    @GetMapping("reequestParam")
    @Override
    public String reequestParam(@RequestParam("name") String name) {
        log.info("声明式服务调用Controller--------reequestParam 参数接收--------请求参数：{}", 
                 name);
        return "声明式服务调用Controller--------reequestParam 参数接收--------成功 ：" + name;
    }

    /**
     * requestHeader 参数接收
     */
    @GetMapping("requestHeader")
    @Override
    public UserDTO requestHeader(@RequestHeader("id") Long id, 
                                 @RequestHeader("name") String name) {
        log.info("声明式服务调用Controller--------requestHeader 参数接收--------请求参数：{}", 
                 name);
        return new UserDTO(id, name);
    }


    /**
     *  requestBody 参数接收
     */
    @PostMapping("requestBody")
    @Override
    public UserDTO requestBody(@RequestBody UserDTO userDTO) {
        log.info("声明式服务调用Controller--------requestBody 参数接收--------请求参数：{}",
                 userDTO);
        return userDTO;
    }

    /**
     *  post 参数接收(接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("post")
    @Override
    public UserDTO post(UserDTO userDTO) {
        log.info("声明式服务调用Controller--------post 参数接收--------请求参数：{}", 
                 userDTO);
        return userDTO;
    }

}



```





## 2.3、服务消费者_3001：

### 2.3.1、`ConsumerFeignServerService`   接口

> 这里主要还是用到上面的声明式服务注解：`@FeignClient`

```java
@FeignClient(name = "HLJ-SERVER-PROVIDER") //服务提供者应用名
public interface ConsumerFeignServerServiceImpl extends FeignServerService {

}
```



### 2.3.1、`ConsumerFeignController`

```java
package com.healerjean.proj.controller;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.FeignServerService;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName FeignConsumerController
 * @date 2020/4/14  16:09.
 * @Description
 */
@Api(description = "服务消费者_3001_控制器-声明式服务调用Controller")
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerFeignController {


    @Autowired
    private FeignServerService feignServerService;

    /**
     * 测试申明式服务调用
     */
    @GetMapping(value = "feign/invokeInter")
    public String invokeInter() {
        String str1 = feignServerService.reequestParam("healerjean");
        UserDTO userDTO1 = feignServerService.requestHeader(1L, "healerjean");
        UserDTO userDTO2 = feignServerService.requestBody(new UserDTO(1L, "healerjean"));
        UserDTO userDTO3 = feignServerService.post(new UserDTO(1L, "healerjean"));
        return "str1 = " + str1 + "\n "
                + "str2 = " + JsonUtils.toJsonString(userDTO1) + "\n "
                + "str3 = " + JsonUtils.toJsonString(userDTO2) + "\n "
                + "str4 = " + JsonUtils.toJsonString(userDTO3) + "\n ";
    }
}

```





## 2.4、启动测试  

```http
http://127.0.0.1:3001/api/consumer/feign/invokeInter
```



**接口返回：**   

```json
str1 = 声明式服务调用Controller--------reequestParam 参数接收--------成功 ：healerjean
str2 = {"id":1,"name":"healerjean"}
str3 = {"id":1,"name":"healerjean"}
str4 = {}
```



# 3、声明式服务调用降级  

## 3.1、降级声明

```java
@FeignClient(name = "HLJ-SERVER-PROVIDER", //服务提供者应用名
             fallback = ConsumerFeignServerServiceFallBack.class) 
public interface ConsumerFeignServerService extends FeignServerService {

}
```



## 3.2、降级配置       

### 3.2.1、开启 `feign`使用`hystrix`熔断的配置   

```properties
# feign使用hystrix熔断的配置
feign.hystrix.enabled=true
```



```properties
spring.application.name=hlj-server-consumer
server.port=3001


#eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
#为这个服务消费者指定两个注册中心 ，这样即使一个注册中心挂掉了，另外的一个注册中心还能够继续提供服务
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/,http://localhost:1112/eureka/
hlj.server.providerName=HLJ-SERVER-PROVIDER
# feign使用hystrix熔断的配置
feign.hystrix.enabled=true
```



### 3.2.2、降级接口  `ConsumerFeignServerServiceFallBack`



```java
package com.healerjean.proj.service.impl;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.ConsumerFeignServerService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName ConsumerFeignServerServiceFallBack
 * @date 2020/4/14  17:51.
 * @Description
 */
@Component
public class ConsumerFeignServerServiceFallBack implements ConsumerFeignServerService {


    @Override
    public String reequestParam(String name) {
        return "error";
    }

    @Override
    public UserDTO requestHeader(Long id, String name) {
        return new UserDTO();
    }


    @Override
    public UserDTO requestBody(UserDTO userDTO) {
        return new UserDTO();
    }

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @Override
    public UserDTO post(UserDTO userDTO) {
        return  new UserDTO();
    }
}

```



### 3.2.3、启动报错 

```
public abstract com.healerjean.proj.dto.UserDTO com.healerjean.proj.service.FeignServerService.post(com.healerjean.proj.dto.UserDTO)
to {[/api/provider/feign/post],methods=[POST]}: There is already 'consumerFeignServerServiceFallBack' bean method
```

**方案一：如果下面没有修改`@GetMapping`或者`@PostMapping`的的地址，则启动会报错，报错信息如下，提示已经有方法了，所以我们一定要加上`@GetMapping`或者`@PostMapping`，并且修改为不重复的**         

```java
package com.healerjean.proj.service.impl;

import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.service.ConsumerFeignServerService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName ConsumerFeignServerServiceFallBack
 * @date 2020/4/14  17:51.
 * @Description
 */
@Component
public class ConsumerFeignServerServiceFallBack implements ConsumerFeignServerService {


    @GetMapping("fallBackReequestParam")
    @Override
    public String reequestParam(String name) {
        return "error";
    }
    @GetMapping("fallBackRequestHeader")
    @Override
    public UserDTO requestHeader(Long id, String name) {
        return new UserDTO();
    }

    @PostMapping("fallBackRequestBody")
    @Override
    public UserDTO requestBody(UserDTO userDTO) {
        return new UserDTO();
    }

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("fallBackPost")
    @Override
    public UserDTO post(UserDTO userDTO) {
        return  new UserDTO();
    }
}

```



**方案二、如果觉得上面麻烦，`@Requestmappering`不要加在声明式类上也是可以的 ，这种情况在1的用例中是可以的。在2重构之后的情况，将它放到方法层面，服务提供者Controller自己写`@Requestmappering`，所以这种方案要视情况选用。**



```java
public interface FeignServerService {

    @GetMapping("api/provider/feign/reequestParam")
    String reequestParam(@RequestParam("name") String name);

    @GetMapping("api/provider/feign/requestHeader")
    UserDTO requestHeader(@RequestHeader("id") Long id, @RequestHeader("name") String name);

    @PostMapping("api/provider/feign/requestBody")
    UserDTO requestBody(@RequestBody UserDTO userDTO);

    /**
     * (接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("api/provider/feign/post")
    UserDTO post(UserDTO userDTO);

}
```



```java
@Api(description = "服务提供者_2001_声明式服务调用Controller")
@RestController
@Slf4j
@RequestMapping("api/provider/feign")
public class ProviderFeignController extends BaseController implements FeignServerService {

    /**
     * reequestParam 参数接收
     */
    @GetMapping("reequestParam")
    @Override
    public String reequestParam(@RequestParam("name") String name) {
        log.info("声明式服务调用Controller--------reequestParam 参数接收--------请求参数：{}", name);
        return "声明式服务调用Controller--------reequestParam 参数接收--------成功 ：" + name;
    }

    /**
     * requestHeader 参数接收
     */
    @GetMapping("requestHeader")
    @Override
    public UserDTO requestHeader(@RequestHeader("id") Long id, @RequestHeader("name") String name) {
        log.info("声明式服务调用Controller--------requestHeader 参数接收--------请求参数：{}", name);
        return new UserDTO(id, name);
    }


    /**
     *  requestBody 参数接收
     */
    @PostMapping("requestBody")
    @Override
    public UserDTO requestBody(@RequestBody UserDTO userDTO) {
        log.info("声明式服务调用Controller--------requestBody 参数接收--------请求参数：{}", userDTO);
        return userDTO;
    }

    /**
     *  post 参数接收(接收会使空，这种方式我目前开发基本不会用到了，今后用到再说)
     */
    @PostMapping("post")
    @Override
    public UserDTO post(UserDTO userDTO) {
        log.info("声明式服务调用Controller--------post 参数接收--------请求参数：{}", userDTO);
        return userDTO;
    }

}

```







## 3.3、启动测试  

> 不启动服务提供者_2001：

```http
http://127.0.0.1:3001/api/consumer/feign/invokeInter
```

**接口返回：**     

```json
str1 = error
str2 = {}
str3 = {}
str4 = {}
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
		id: 'GPcyXWxjSDUO6E5F',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

