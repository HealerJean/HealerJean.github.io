---
title: ShardingSphere和MybatisPlus版本问题
date: 2020-04-06 03:33:00
tags: 
- Database
category: 
- Database
description: ShardingSphere和MybatisPlus版本问题
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           





### 错误来源

**我使用的ShardingSphere和MybatisPlus做的分库分表，当版本分表为如下的时候没有任何问题**



```xml
<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.1.tmp</version>
</dependency>

<!--sharding jdbc 开始-->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC1</version>
</dependency>
```





**但是在我要测试`XA事务`的时候，要用到`ShardingSphere`版本为`4.0.0-RC2`，这个时候启动不论怎么样都是报错，错误信息如下**  



```log
Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext.refresh[557]
```



### 解决错误 

博主上网一顿百度，用尽了全身力气，从下午干到了晚上9点。终于在Github上找到了问题，同时给我很好了找到了一个学习和找错的经验。一有问题先Google，如果关键词很明显再Google上搜不到。那怎么办？   

找源头呗，找`ShardingSphere`和`MybatisPlus`的开源项目，肯定有人去提交bug，或者去问问题。那里才是程序员的天堂。   

果然：我看到了 [https://github.com/apache/incubator-shardingsphere/issues/2712](https://github.com/apache/incubator-shardingsphere/issues/2712)   

**原因就在于我使用的`Springboot`版本的`alibaba.druid`**



大神给出了两种解决思路，第一种当然是屁话       

1.ShardingSphere 4.0.0-RC1 搭配 MyBatis、druid-spring-boot-starter使用。      

2.ShardingSphere 4.0.0-RC2 或者RC3 搭配 MyBatis、com.alibaba.druid（非springboot版本）使用。     

3.希望引起ShardingSphere注意，修复bug





```xml
<!-- 数据源 -->
<!--<dependency>-->
<!--<groupId>com.alibaba</groupId>-->
<!--<artifactId>druid-spring-boot-starter</artifactId>-->
<!--</dependency>-->  

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.21</version>
</dependency>



<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>


<!--sharding jdbc 开始-->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC2</version>
</dependency>       
        
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
		id: 'jhNiCW3ovRLOYEQc',
    });
    gitalk.render('gitalk-container');
</script> 

