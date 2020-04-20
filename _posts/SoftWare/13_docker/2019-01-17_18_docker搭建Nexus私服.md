---
title: Docker搭建Nexus私服
date: 2019-01-17 03:33:00
tags: 
- Docker
- Maven
category: 
- Docker
- Maven
- description: Docker搭建Nexus私服
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



## 1、docekr配置

```java

nexus:
  restart: always
  ports:
    - '8089:8081/tcp'
  environment:
    - JAVA_HOME=/opt/java
    - SONATYPE_DIR=/opt/sonatype
    - NEXUS_HOME=/opt/sonatype/nexus
    - NEXUS_DATA=/nexus-data
    - SONATYPE_WORK=/opt/sonatype/sonatype-work
    - DOCKER_TYPE=docker
    - >-
      INSTALL4J_ADD_VM_PARAMS=-Xms1200m -Xmx1200m -XX:MaxDirectMemorySize=2g
      -Djava.util.prefs.userRoot=/nexus-data/javaprefs
  memswap_limit: 0
  labels:
    aliyun.scale: '1'
  shm_size: 0
  image: 'registry.cn-beijing.aliyuncs.com/sonatype-mirror/nexus3:latest'
  memswap_reservation: 0
  volumes:
    - '/data/nexus/data:/nexus-data'
  kernel_memory: 0
  mem_limit: 1073741824


```


## 2、maven中

server.xml

```xml

<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
<!-- <localRepository>/Users/gaopengzhi/java/repository</localRepository> -->
 <servers>
      <server>
      <id>releases</id>
      <username>admin</username>
      <password>passwor</password>
    </server>

    <server>
      <id>snapshots</id>
      <username>admin</username>
      <password>passwor</password>
    </server>
    <server>
      <id>healerejean</id>
      <username>admin</username>
      <password>passwor</password>
    </server>


  </servers>
 <mirrors>
    <mirror>
      <id>healerejean</id>
      <mirrorOf>*</mirrorOf>
      <url>http://47.105.49.88:8089/repository/healerejean/</url>
    </mirror>

    <!-- <mirror>
      <id>CN</id>
      <name>OSChina Central</name>
      <url>http://maven.oschina.net/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
    </mirror>-->

  <mirror>
        <id>repo2</id>
        <mirrorOf>central</mirrorOf>
        <name>Human Readable Name for this Mirror.</name>
        <url>http://repo2.maven.org/maven2/</url>
  </mirror>
  <mirror>
        <id>net-cn</id>
        <mirrorOf>central</mirrorOf>
        <name>Human Readable Name for this Mirror.</name>
        <url>http://maven.net.cn/content/groups/public/</url>
  </mirror>
  <mirror>
        <id>ui</id>
        <mirrorOf>central</mirrorOf>
        <name>Human Readable Name for this Mirror.</name>
       <url>http://uk.maven.org/maven2/</url>
  </mirror>
  <mirror>
        <id>jboss-public-repository-group</id>
        <mirrorOf>central</mirrorOf>
        <name>JBoss Public Repository Group</name>
       <url>http://repository.jboss.org/nexus/content/groups/public</url>
  </mirror>
    <mirror>
     <id>offical</id>
     <name>Maven Official Repository</name>
     <url>http://repo1.maven.org/maven2</url>
    <mirrorOf>central</mirrorOf>
  </mirror>

  <mirror>
    <id>jboss</id>
    <name>Jboss Repository</name>
    <url>http://repository.jboss.org/nexus/content/groups/public-jboss/</url>
    <mirrorOf>central</mirrorOf>
  </mirror>

    <mirror>
      <id>UK</id>
      <name>UK Central</name>
      <url>http://uk.maven.org/maven2</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>

  <profiles>
     <profile>
       <id>dev</id>
          <activation>
                  <activeByDefault>false</activeByDefault>
                  <jdk>1.8</jdk>
              </activation>
          <repositories>
            <repository>
            <id>healerejean</id>
            <url>http://47.105.49.88:8089/repository/healerejean/</url>
            <releases>
              <enabled>true</enabled>
            </releases>
            <snapshots>
              <enabled>true</enabled>
            </snapshots>
          </repository>
        </repositories>
        <pluginRepositories>
          <pluginRepository>
            <id>healerejean</id>
            <url>http://47.105.49.88:8089/repository/healerejean/</url>
            <releases>
              <enabled>true</enabled>
            </releases>
            <snapshots>
              <enabled>true</enabled>
             </snapshots>
          </pluginRepository>
        </pluginRepositories>
      </profile>
  </profiles>


  <activeProfiles>
    <activeProfile>dev</activeProfile>
  </activeProfiles>
</settings>


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
		id: 'MmOoLCh0ixruIanX',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

