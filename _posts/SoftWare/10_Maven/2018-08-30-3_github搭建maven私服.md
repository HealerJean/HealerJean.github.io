---
title: Github搭建maven私服
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
<distributie.directory>maven_directory</distributie.directory>



<distributionManagement>
    <repository>
        <id>healerjean-managemaent_id</id>
        <name>healerjean_managemaent_name</name>
        <url>file://${project.build.directory}/${distributie.directory}</url>
    </repository>
</distributionManagement>


```

#### 1.2、添加maven发布插件

```xml
<!--maven发布插件-->
<plugin>
    <artifactId>maven-deploy-plugin</artifactId>
    <version>2.8.1</version>
    <configuration>
        <altDeploymentRepository>internal.repo::default::file://${project.build.directory}/${distributie.directory}</altDeploymentRepository>
    </configuration>
</plugin>

```

#### 1.3、完整的pom如下


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.healerjean.proj</groupId>
	<artifactId>hlj-github-maven</artifactId>
	<version>1.0.1</version>
	<packaging>jar</packaging>

	<name>hlj-github-maven</name>
	<description>hlj-github-maven</description>

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
		<distributie.directory>maven_directory</distributie.directory>

	</properties>

	<distributionManagement>
		<repository>
			<id>healerjean-managemaent_id</id>
			<name>healerjean_managemaent_name</name>
			<url>file://${project.build.directory}/${distributie.directory}</url>
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
					<altDeploymentRepository>internal.repo::default::file://${project.build.directory}/${distributie.directory}</altDeploymentRepository>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>


		

```


#### 1.4、运行命令，发布到我们上面配置的路径target/maven_directory中


```
mvn clean deploy
```



![1571646740203](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1571646740203.png)



#### 1.5、创建一个maven项目`maven_github`,将`maven_directory`里面的所有文件复制到我们创建好的maven项目中去





![1571646899210](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1571646899210.png)





#### 1.6、pom中开始使用这个pom依赖，为了测试，我们应该先删除除掉本地仓库中的依赖包，并新建一个项目





#### 1.8、开始导入我们自己的依赖,这个是及时我们更新pom，发现还是显示红色报错，不过没关系，我们直接使用命令 mvn package,强制下载

```xml
<repositories>
    <repository>
        <!--id任意-->
        <id>healerjean-repo</id>
        <url>https://raw.github.com/HealerJean/maven_github/master/hlj-test-github-maven</url>
    </repository>
</repositories>


<!--github仓库导入的-->
<dependency>
    <groupId>com.healerjean.repo</groupId>
    <artifactId>hlj-github-maven</artifactId>
    <version>1.0.1</version>
</dependency>

```


```
mvn packege
```

![WX20180830-213427](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180830-213427.png)





## 2、jar上传到github制作依赖包

参考下面的，将它先导入本地maven仓库，然后我们直接将生成的文件夹`taobao-sdk-java`上传到上面那个仓库中

```xml

mvn install:install-file -Dfile=taobao-sdk-java-5.2.1.jar -DgroupId=com.healerjean.proj -DartifactId=taobao-api-20191021 -Dversion=1.0.1 -Dpackaging=jar  
 

<dependency>
    <groupId>com.healerjean.proj</groupId>
    <artifactId>taobao-api-20191021</artifactId>
    <version>1.0.1</version>
</dependency>


```

## 3、解释下如何观察这个依赖包的pom Id如果确认

#### 3.1、打开这个`maven-metadata.xml`文件



![1571648404908](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1571648404908.png)




#### 3.2、可以观察到版本信息，已经更新时间


```xml
<?xml version="1.0" encoding="UTF-8"?>
<metadata>
  <groupId>com.healerjean.proj</groupId>
  <artifactId>hlj-github-maven</artifactId>
  <versioning>
    <release>1.0.1</release>
    <versions>
      <version>1.0.1</version>
    </versions>
    <lastUpdated>20191021083140</lastUpdated>
  </versioning>
</metadata>


```

## 4、我自己今后的规范

#### 1、groupId `com.healerjean.proj`
#### 2、artifactId 工具类名字
#### 3、版本 0.0.1（初始）

```xml
<dependency>
	<groupId>com.healerjean.proj</groupId>
	<artifactId>hlj-access-</artifactId>
	<version>0.0.1</version>
</dependency>



```

#### 4、目录发布管理器或者叫githut入口名字 `hlj-artifactId`


```xml
<distributie.directory>maven_directory</distributie.directory>

<distributionManagement>
    <repository>
        <id>healerjean-managemaent_id</id>
        <name>healerjean_managemaent_name</name>
        <url>file://${project.build.directory}/${distributie.directory}</url>
    </repository>
</distributionManagement>

<repositories>
    <repository>
        <!--id任意-->
        <id>hlj-repo</id>
        <url>https://raw.github.com/HealerJean123/maven_github/master</url>
    </repository>
</repositories>

<!--github仓库导入的-->
<dependency>
    <groupId>com.hlj.repo</groupId>
    <artifactId>test-github-maven</artifactId>
    <version>0.0.1</version>
</dependency>
```

   







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

