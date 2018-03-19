---
title: Docker部署sprinBoot项目
date: 2018-03-19 16:33:00
tags: 
- Docker
- SpringBoot
category: 
- Docker
- SpringBoot
description: Docker部署sprinBoot项目
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

相信能够查到这篇《Docker部署sprinBoot项目》文章的，大家都知道docker和springBoot了，那我就不废话了，直接开始干吧！！！


## 1、模拟构建一个springBoot项目

相信构建一个sprinBoot项目对于大家来说太简单了，是吧，那么这里直接贴出pom文件吧，本来这个也不想贴出来，但是怕一些绝对的小白后面看不懂，所以贴出来的，意义呢，就是希望大家看到这里的版本和项目的名字


```
版本和项目的名字，那么将来打包的jar名字就是 com-hlj-springboot-docker-0.0.1-SNAPSHOT.jar

	<version>0.0.1-SNAPSHOT</version>
	<artifactId>com-hlj-springboot-docker</artifactId>
```


```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.hlj.springBoot.docker</groupId>
	<artifactId>com-hlj-springboot-docker</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>com-hlj-springboot-docker</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.0.RELEASE</version>
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

## 2、项目中配置docker

1、配置名称

```
<properties>	
						……  ……
	<docker.image.prefix>springboot</docker.image.prefix>
</properties>

```
2、插件

```
<plugin>
	<groupId>com.spotify</groupId>
	<artifactId>docker-maven-plugin</artifactId>
	<version>1.0.0</version>
	<configuration>
		<imageName>${docker.image.prefix}/${project.artifactId}</imageName>
		<dockerDirectory>src/main/docker</dockerDirectory>
		<resources>
			<resource>
				<targetPath>/</targetPath>
				<directory>${project.build.directory}</directory>
				<include>${project.build.finalName}.jar</include>
			</resource>
		</resources>
	</configuration>
</plugin>

```

3、在当前项目目录下创建文件名为的文件`Dockerfile`

![WX20180319-155748](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180319-155748.png)

写入内容，解释说明

1、FROM ，表示使用 Jdk8 环境 为基础镜像

2、MAINTAINER ,作者为 HealerJean

3、ADD 表示将maven打包好的jar重命名为app.jar

4、CMD 表示运行这个项目 自己好好瞅瞅是不是 java -jar app.jar

```
FROM java:8
MAINTAINER HealerJean
ADD target/com-hlj-springboot-docker-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","/app.jar"]

```

## 3、测试下这个sprinBoot工程是否可用



```

打包
java package

发布
java -jar target/com-hlj-springboot-docker-0.0.1-SNAPSHOT.jar
```

浏览器访问 [http://localhost:8080/](http://localhost:8080/) 这个里面是自己随意自己弄的url请求，只是看看有没有数据而已，成功


```
package com.hlj.spring.boot.docker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerController {

    @RequestMapping("/")
    public String index() {
        return "Hello Docker!";
    }
}
```

![WX20180319-162339@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180319-162339@2x.png)

## 4、docker开始部署吧，朋友

解释; hello为前缀，可以自己取名称， com-hlj-springboot-docker为项目的名字（或者也叫jar的名字），后面的.表示Dockerfile 路径的，.表示在当前路径下面

1、编译镜像,这一步相当慢

```
docker build -t hello/com-hlj-springboot-docker .

```

![WX20180319-161111](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180319-161111.png)


2、查看是否存在我们帮个编译好的镜像


```
docker images
```

![WX20180319-161243@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180319-161243@2x.png)
 
3 docker 部署运行 成功 浏览器访问即可

```
端口映射(将容器的8080端口后面的映射到本机的8080端口前面的)

docker run -p 8080:8080 -t hello/com-hlj-springboot-docker

```

![WX20180319-162339@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180319-162339@2x.png)




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
		id: 'oRdKZCbzt05ZNGhm',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

