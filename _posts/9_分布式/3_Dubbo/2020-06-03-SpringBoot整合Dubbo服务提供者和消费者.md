---
title: SpringBoot整合Dubbo服务提供者和消费者
date: 2020-06-03 03:33:00
tags: 
- Dubbo
category: 
- Dubbo
description: SpringBoot整合Dubbo服务提供者和消费者
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

**Dubbo官方**：[http://dubbo.apache.org/](http://dubbo.apache.org/)



# 1、服务提供者和消费者

## 1.1、父工程：

> 项目名称：`hlj-dubbo`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>


    <groupId>com.healerjean.proj</groupId>
    <artifactId>hlj-parent</artifactId>
    <version>${project.healerjean.version}</version>
    <name>>hlj-parent</name>
    <description>父工程</description>
    <packaging>pom</packaging>

    <modules>
        <module>hlj-server-provider-2001</module>
        <module>hlj-server-consumer-3001</module>
        <module>hlj-api</module>
        <module>hlj-common</module>
    </modules>

    <properties>
        <project.healerjean.version>1.0.0-SNAPSHOT</project.healerjean.version>
        <java.version>1.8</java.version>
        <lombok.version>1.18.4</lombok.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <!--swagger 版本-->
        <swagger.version>2.7.0</swagger.version>
        <!--要激活的resoure目录-->
        <profiles.active>src/profiles/local</profiles.active>
        <!--commons-lang3-->
        <commons-lang3.version>3.7</commons-lang3.version>
        <!-- zookeeper 版本-->
        <zookeeper.version>3.4.10</zookeeper.version>
        <!-- dubbo 版本-->
        <dubbo.version>0.2.0</dubbo.version>

    </properties>

    <dependencyManagement>
        <dependencies>

            <!---dubbo-->
            <dependency>
                <groupId>com.alibaba.boot</groupId>
                <artifactId>dubbo-spring-boot-starter</artifactId>
                <version>${dubbo.version}</version>
            </dependency>

            <!--lombok版本太低了，不支持java10  升级版本：>= 1.18.0-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>

            <!--swagger-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>${swagger.version}</version>
            </dependency>

            <!--StringUtils-->
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-lang3</artifactId>
                <version>${commons-lang3.version}</version>
            </dependency>
        </dependencies>

    </dependencyManagement>


    <profiles>
        <profile>
            <!-- 本地开发环境 -->
            <id>local</id>
            <properties>
                <profiles.active>src/profiles/local</profiles.active>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <!-- 开发环境 -->
            <id>dev</id>
            <properties>
                <profiles.active>src/profiles/dev</profiles.active>
            </properties>
            <activation>
                <property>
                    <name>dev</name>
                    <value>true</value>
                </property>
            </activation>
        </profile>
        <profile>
            <!-- 生产环境 -->
            <id>product</id>
            <properties>
                <profiles.active>src/profiles/product</profiles.active>
            </properties>
            <activation>
                <property>
                    <name>product</name>
                    <value>true</value>
                </property>
            </activation>
        </profile>
    </profiles>


    <build>

        <!-- 定义资源目录 -->
        <resources>
            <resource>
                <directory>src/main/resources</directory>
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
        </resources>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.5.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```



## 1.2、API 工程

> 项目名称：`hlj-api`



### 1.2.1、pom依赖 

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

    <artifactId>hlj-api</artifactId>
    <version>${project.healerjean.version}</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!-- dubbo-->
        <dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
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



### 1.2.2、`dubbo`接口：

> 注意下面的包路径，将来 提供者和消费者都要扫描  
>
> `com.healerjean.proj.service`

```java
package com.healerjean.proj.service;


public interface ProviderDubboService {

    String connect(String name);

}

```





## 1.3、服务提供者_2001

> 项目名称：`hlj-server-provider-2001`  



### 1.3.1、pom依赖

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
</parent>

<!---dubbo-->
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
<dubbo.version>0.2.0</dubbo.version>
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

    <artifactId>hlj-server-provider-2001</artifactId>
    <version>${project.healerjean.version}</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <!-- starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--hlj-service-api-->
        <dependency>
            <groupId>com.healerjean.proj</groupId>
            <artifactId>hlj-service-api</artifactId>
            <version>${project.healerjean.version}</version>
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



### 1.3.2、`dubbo.properties`

```properties
####################################
## Dubbo 服务提供者配置
####################################
dubbo.application.name=hlj-server-provider
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880

```



### 1.3.3、`application.properties`

```properties
spring.application.name=hlj-server-provider
server.port=2001

```



### 1.3.5、dubbo接口实现类

> 注意这里的 `@Service` 是`dubbo`提供的

```java
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProviderDubboServiceImpl implements ProviderDubboService {

    @Override
    public String connect(String name) {
        log.info("消费者：【{} 】连接成功", name);
        return name + "：连接成功";
    }

}
```



### 1.3.4、启动类 

> `@EnableDubbo(scanBasePackages = "com.healerjean.proj.service")`：扫描接口的路径

```java
package com.healerjean.proj;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableDubbo(scanBasePackages = "com.healerjean.proj.service")
@PropertySource("classpath:dubbo.properties")
@SpringBootApplication
public class ServerProvider_2001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ServerProvider_2001_Application.class, args);
    }

}

```



## 1.4、服务消费者_3001

> 项目名称：`hlj-server-consumer-3001`



### 1.4.1、pom依赖

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

    <artifactId>hlj-server-provider-2001</artifactId>
    <version>${project.healerjean.version}</version>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <!-- starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--hlj-service-api-->
        <dependency>
            <groupId>com.healerjean.proj</groupId>
            <artifactId>hlj-service-api</artifactId>
            <version>${project.healerjean.version}</version>
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



### 1.4.2、`dubbo.properties`

```properties
####################################
### dubbo  zookeeper
####################################
dubbo.application.name=hlj-server-consumer
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.consumer.timeout=3000
```



### 1.4.3、`application.properties`

```properties
spring.application.name=hlj-server-consumer
server.port=3001

```

### 1.4.4、启动类

> `@EnableDubbo(scanBasePackages = "com.healerjean.proj.service")  `

```java
package com.healerjean.proj;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableDubbo(scanBasePackages = "com.healerjean.proj.service")
@PropertySource("classpath:dubbo.properties")
@SpringBootApplication
public class ServerConsumer_3001_Application {


    public static void main(String[] args) {
        SpringApplication.run(ServerConsumer_3001_Application.class, args);
    }

}

```



### 1.4.5、`dubbo调用`

```java
package com.healerjean.proj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.healerjean.proj.service.ProviderDubboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "服务消费者_3001_控制器")
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerController extends BaseController {

    @Reference
    private ProviderDubboService providerDubboService;

    @ApiOperation(value = "connect",
            notes = "connect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = String.class)
    @GetMapping(value = "connect")
    public String connectProvider(String name) {
        log.info("服务消费者控制器--------connect--------");
        return providerDubboService.connect(name);
    }

}

```





# 2、dubbo注解的使用

## 2.1、`@Service`

> 服务提供者，接口实现需要配置该注解

```java
@Service(version = "0.1", group = "inter_one")
```

| 属性    | 值     | 说明                                                         |
| ------- | ------ | ------------------------------------------------------------ |
| version | String | 服务版本(默认 0.0.0)，建议使用两位数字版本，如：1.0，通常在接口不兼容时版本号才需要升级，**也可以当做group使用，但是不建议** |
| group   | String | **服务分组，当一个接口有多个实现，可以用分组区分**           |



```java
public interface ProviderDubboService {

    String connect(String name);

}

```



```java
@Slf4j
@Service(version = "0.1", group = "inter_one")
public class ProviderDubboServiceImpl implements ProviderDubboService {

    @Override
    public String connect(String name) {
        log.info("消费者：ProviderDubboServiceImpl 【{} 】连接成功", name);
        int i = 1 / 0;
        return name + "：连接成功";
    }

}

```



```java
@Slf4j
@Service(version = "0.1", group = "inter_other")
public class ProviderDubboOtherServiceImpl implements ProviderDubboService {

    @Override
    public String connect(String name) {
        log.info("消费者 ProviderDubboOtherServiceImpl：【{} 】连接成功", name);
        return name + "：连接成功";
    }

}

```







## 2.2、`@Reference`



| 属性    | 值     | 说明                                                         |
| ------- | ------ | ------------------------------------------------------------ |
| version | String | 提供者接口的版本                                             |
| group   | String | 提供者的分组。当一个接口有多个实现的时候，可以用分组区别     |
| mock    | String | （非业务异常） 服务接口调用失败Mock实现类名，该Mock类必须有一个无参构造函数，与Local的区别在于，Local总是被执行，而Mock只在出现非业务异常(比如超时，网络异常等)时执行，Local在远程调用之前执行，Mock在远程调用后执行。 |

```java
@RestController
@RequestMapping("api/consumer")
@Slf4j
public class ConsumerController extends BaseController {

    // moke （非业务异常） 服务接口调用失败Mock实现类名，该Mock类必须有一个无参构造函数，与Local的区别在于，Local总是被执行，而Mock只在出现非业务异常(比如超时，网络异常等)时执行，Local在远程调用之前执行，Mock在远程调用后执行。
    @Reference(version = "0.1", group = "inter_one", mock = "com.healerjean.proj.service.impl.Apple")
    private ProviderDubboService providerDubboService;

    @GetMapping(value = "connect")
    public String connectProvider(String name) {
        String connect = providerDubboService.connect(name);
        return connect;
    }

}
```



```java
@Slf4j
public class Apple implements Fruits {

    @Override
    public void name() {
        log.info("Fruits--------苹果");
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
		id: 'So1P4nzLm8TOwGrW',
    });
    gitalk.render('gitalk-container');
</script> 

