---
title: 阿里云搭建springboot项目容器
date: 2018-08-14 03:33:00
tags: 
- Docker
category: 
- Docker
description: 阿里云搭建springboot项目容器
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


### 1、开通阿里云容器镜像服务

![WX20180814-151352@2x](markdownImage/WX20180814-151352@2x.png)



### 2、创建镜像仓库

![WX20180814-151312@2x](markdownImage/WX20180814-151312@2x.png)


### 4、选择阿里云或者其他地方的托管代码

![WX20180814-151340@2x](markdownImage/WX20180814-151340@2x.png)



![WX20180814-151501@2x](markdownImage/WX20180814-151501@2x.png)




## 2、搭建sprinboot-docker项目（之前已经做过一个了）


```
	•	镜像的名称registry.cn-qingdao.aliyuncs.com/ihaidou/sprinboot_docker是阿里云镜像仓库的域名，
	•	ihaidou是用户的命名空间，
	•	sprinboot_docker是用户某个仓库的名称，
	•	此处没有镜像tag，默认tag为latest。

	<docker.image.prefix>registry.cn-qingdao.aliyuncs.com/ihaidou/sprinboot_docker</docker.image.prefix>
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
		<docker.image.prefix>registry.cn-qingdao.aliyuncs.com/ihaidou/sprinboot_docker</docker.image.prefix>
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
		</plugins>
	</build>


</project>


```


## 3、本地docker登录（网站注册的用户名和密码）

[https://cloud.docker.com/swarm/healerjean/dashboard/onboarding/cloud-registry](https://cloud.docker.com/swarm/healerjean/dashboard/onboarding/cloud-registry)

```
JeandeMBP:com-hlj-springboot-docker healerjean$ docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username (mxzdhealer@gmail.com): healerjean
Password: 
Login Succeeded
JeandeMBP:com-hlj-springboot-docker healerjean$ 


```




<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

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
		id: 'AAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

