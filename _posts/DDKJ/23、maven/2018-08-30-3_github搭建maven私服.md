---
title: github搭建maven私服
date: 2018-08-30 03:33:00
tags: 
- Maven
- GitHub
category: 
- Maven
- GitHub
description: github搭建maven私服
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

GitHub的强大，详细很多小伙伴们都知道了，下面我主要讲解的是两种jar包的上传，
1、第一种是我们自己写的代码想提供给他人或者自己使用
2、第二种是针对一些平台，比如阿里，百度等知名互联网他们提供的jar包制作成我们的maven包，

### 1、生成自己的Maven私服


#### 1.1、配置发布管理器，将这个发布管理器发布的maven版本放到我们本地（发布管理器如果不懂的话，建议查找我的博客进行了解）


<font color="red"> 下面这个名字，建议根据我们所制作的maven的jar包的作用进行命名，因为为了我们已经自己在github上查看方便以及维护方便 </font>



```xml
<distributionManagement.directory.name>hlj-test-github-maven</distributionManagement.directory.name>



<distributionManagement>
	<repository>
		<id>hlj-managemaent-Id</id>
		<name>hlj managemaent name</name>
		<url>file://${project.build.directory}/${distributionManagement.directory.name}</url>
	</repository>
</distributionManagement>


```

#### 1.2、添加maven发布插件

```xml
<plugin>
	<artifactId>maven-deploy-plugin</artifactId>
	<version>2.8.1</version>
	<configuration>
		<altDeploymentRepository>internal.repo::default::file://${project.build.directory}/${distributionManagement.directory.name}</altDeploymentRepository>
	</configuration>
</plugin>

```

#### 1.3、完整的pom如下


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.hlj.repo</groupId>
	<artifactId>test-github-maven</artifactId>
	<version>0.0.1</version>
	<packaging>jar</packaging>

	<name>com-hlj-github-maven-repo</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<distributionManagement.directory.name>hlj-test-github-maven</distributionManagement.directory.name>

	</properties>

	<distributionManagement>
		<repository>
			<id>hlj-managemaent-Id</id>
			<name>hlj managemaent name</name>
			<url>file://${project.build.directory}/${distributionManagement.directory.name}</url>
		</repository>
	</distributionManagement>


	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
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

			<!--maven发布插件-->
			<plugin>
				<artifactId>maven-deploy-plugin</artifactId>
				<version>2.8.1</version>
				<configuration>
					<altDeploymentRepository>internal.repo::default::file://${project.build.directory}/${distributionManagement.directory.name}</altDeploymentRepository>
				</configuration>
			</plugin>
		</plugins>
	</build>


</project>

		

```


#### 1.4、运行命令，发布到我们上面配置的路径target/hlj-test-github-maven中


```
mvn clean deploy
```

![WX20180830-212430](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180830-212430.png)

#### 1.5、创建一个maven项目`maven_github`,将`hlj-test-github-maven`
文件夹复制到我们创建好的maven项目中去

![WX20180830-213322@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180830-213322@2x.png)

#### 1.6、pom中开始使用这个pom依赖，为了测试，我们应该先删除除掉本地仓库中的依赖包，

```

JeandeMBP:hlj healerjean$ cd ~/.m2/repository/com/hlj
JeandeMBP:hlj healerjean$ ls -l
total 0
drwxr-xr-x  6 healerjean  staff  204 Aug 30 20:30 repo
JeandeMBP:hlj healerjean$ rm -rf repo
JeandeMBP:hlj healerjean$ ls -l
total 0
JeandeMBP:hlj healerjean$ 


```

#### 1.7、修改下我们目前测试工程中pom中的`groupId`，防止产生干扰


```
<groupId>com.hlj.repo</groupId>
改为
<groupId>com.hlj.repo-add</groupId>


```

#### 1.8、开始导入我们自己的依赖,这个是及时我们更新pom，发现还是显示红色报错，不过没关系，我们直接使用命令 mvn package,强制下载

```xml
<repositories>
	<repository>
		<!--id任意-->
		<id>hlj-repo</id>
		<url>https://raw.github.com/HealerJean/maven_github/master/hlj-test-github-maven</url>
	</repository>
</repositories>


<!--github仓库导入的-->
<dependency>
	<groupId>com.hlj.repo</groupId>
	<artifactId>test-github-maven</artifactId>
	<version>0.0.1</version>
</dependency>


```


```
mvn packege
```

![WX20180830-213427](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180830-213427.png)



### 1.9、成功，测试的pom如下


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.hlj.repo</groupId>
	<artifactId>test-github-maven</artifactId>
	<version>0.0.1</version>
	<packaging>jar</packaging>

	<name>com-hlj-github-maven-repo</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<distributionManagement.directory.name>hlj-test-github-maven</distributionManagement.directory.name>

	</properties>

	<distributionManagement>
		<repository>
			<id>hlj-managemaent-Id</id>
			<name>hlj managemaent name</name>
			<url>file://${project.build.directory}/${distributionManagement.directory.name}</url>
		</repository>
	</distributionManagement>

	<repositories>
		<repository>
			<!--id任意-->
		  <id>hlj-repo</id>
			<url>https://raw.github.com/HealerJean/maven_github/master/hlj-test-github-maven</url>
		</repository>
	</repositories>

	<dependencies>


		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<!--github仓库导入的-->
		<dependency>
			<groupId>com.hlj.repo</groupId>
			<artifactId>test-github-maven</artifactId>
			<version>0.0.1</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>

			<!--maven发布插件-->
			<plugin>
				<artifactId>maven-deploy-plugin</artifactId>
				<version>2.8.1</version>
				<configuration>
					<altDeploymentRepository>internal.repo::default::file://${project.build.directory}/${distributionManagement.directory.name}</altDeploymentRepository>
				</configuration>
			</plugin>
		</plugins>
	</build>


</project>

		

```

## 2、jar上传到github制作依赖包

参考下面的，将它先导入本地maven仓库，然后我们直接将生成的文件夹`taobao-sdk-java`上传到上面那个仓库中

```xml

mvn install:install-file -Dfile=taobao-sdk-java-5.2.1.jar -DgroupId=taobao-sdk-java -DartifactId=taobao-sdk-java -Dversion=5.2.1 -Dpackaging=jar  
 

<dependency>
    <groupId>taobao-sdk-java</groupId>
    <artifactId>taobao-sdk-java</artifactId>
    <version>5.2.1</version>
</dependency>

```

### 3、解释下如何观察这个依赖包的pom Id如果确认

#### 3.1、打开这个`maven-metadata.xml`文件

![WX20180830-213453@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180830-213453@2x.png)


#### 3.2、可以观察到版本信息，已经更新时间


```xml
<?xml version="1.0" encoding="UTF-8"?>
<metadata>
  <groupId>com.hlj</groupId>
  <artifactId>com-hlj-github-maven-repo</artifactId>
  
  <versioning>
    <versions>
      <version>0.0.1-SNAPSHOT</version>
    </versions>
    <lastUpdated>20180830123020</lastUpdated>
  </versioning>
</metadata>

```

## 4、我自己今后的规范

#### 1、groupId `com.hlj.repo`
#### 2、artifactId 工具类名字
#### 3、版本 0.0.1（初始）

```xml
<dependency>
	<groupId>com.hlj.repo</groupId>
	<artifactId>logback-access-</artifactId>
	<version>0.0.1</version>
</dependency>

<dependency>
	<groupId>com.hlj.repo</groupId>
	<artifactId>logback-classic</artifactId>
	<version>0.0.1</version>
</dependency>

```

#### 4、目录发布管理器或者叫githut入口名字 `hlj-artifactId`


```xml
<distributionManagement.directory.name>hlj-test-github-maven</distributionManagement.directory.name>

<distributionManagement>
	<repository>
		<id>hlj-managemaent-Id</id>
		<name>hlj managemaent name</name>
		<url>file://${project.build.directory}/${distributionManagement.directory.name}</url>
	</repository>
</distributionManagement>

<repositories>
	<repository>
		<!--id任意-->
		<id>hlj-repo</id>
		<url>https://raw.github.com/HealerJean/maven_github/master/hlj-test-github-maven</url>
	</repository>
</repositories>

	<dependency>
			<groupId>com.hlj.repo</groupId>
			<artifactId>test-github-maven</artifactId>
			<version>0.0.1</version>
	</dependency>

```

### [代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_08_30_3_github%E6%90%AD%E5%BB%BAmaven%E7%A7%81%E6%9C%8D/com-hlj-github-maven-repo.zip)


<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'jz57b4En9mWNxHvX',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

