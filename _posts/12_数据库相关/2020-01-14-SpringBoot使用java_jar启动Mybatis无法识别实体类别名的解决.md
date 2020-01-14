---
title: SpringBoot使用java_jar启动Mybatis无法识别实体类别名的解决
date: 2020-01-14 03:33:00
tags: 
- Database
category: 
- Database
description: SpringBoot使用java_jar启动Mybatis无法识别实体类别名的解决
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



#### 恶心到吐血，找一天错误了，恶心啊。一直在找代码问题，快下班了，看到了一位大神的博客，解决了问题 ，真的快抑郁了



**大神博客 [https://blog.csdn.net/rainbow702/article/details/63255736](https://blog.csdn.net/rainbow702/article/details/63255736)**



### 1、报错信息 

```java
        ...] org.hibernate.jpa.internal.util.LogHelper.logPersistenceUnitInformation[31]
2020-01-14 17:46:49 INFO  -[                                ]- HHH000412: Hibernate Core {5.3.10.Final} org.hibernate.Version.logVersion[46]
2020-01-14 17:46:49 INFO  -[                                ]- HHH000206: hibernate.properties not found org.hibernate.cfg.Environment.<clinit>[213]
2020-01-14 17:46:49 INFO  -[                                ]- HCANN000001: Hibernate Commons Annotations {5.0.4.Final} org.hibernate.annotations.common.Version.<clinit>[49]
2020-01-14 17:46:51 INFO  -[                                ]- {dataSource-1} inited com.alibaba.druid.pool.DruidDataSource.init[928]
2020-01-14 17:46:51 INFO  -[                                ]- HHH000400: Using dialect: org.hibernate.dialect.MySQL5Dialect org.hibernate.dialect.Dialect.<init>[157]
2020-01-14 17:46:54 INFO  -[                                ]- Initialized JPA EntityManagerFactory for persistence unit 'default' org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.buildNativeEntityManagerFactory[415]
2020-01-14 17:46:54 WARN  -[                                ]- Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'requireDomainAspect': Unsatisfied dependency expressed through field 'sysDomainManager'; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sysDomainManager': Unsatisfied dependency expressed through field 'sysDomainDao'; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sysDomainDao': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/healerjean/proj/config/DatasourceConfig.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'clusterSqlSessionFactory' threw exception; nested exception is org.springframework.core.NestedIOException: Failed to parse mapping resource: 'URL [jar:file:/D:/workspace/origin/iku/iku-parent/iku-client-webh5/iku-client-webh5.jar!/BOOT-INF/lib/iku-data-1.0.0-SNAPSHOT.jar!/mapper/system/SysAlimamaInfoMapper.xml]'; nested exception is org.apache.ibatis.builder.BuilderException: Error parsing Mapper XML. The XML location is 'URL [jar:file:/D:/workspace/origin/iku/iku-parent/iku-client-webh5/iku-client-webh5.jar!/BOOT-INF/lib/iku-data-1.0.0-SNAPSHOT.jar!/mapper/system/SysAlimamaInfoMapper.xml]'. Cause: org.apache.ibatis.builder.BuilderException: Error resolving class. Cause: org.apache.ibatis.type.TypeException: Could not resolve type alias 'SysAlimamaInfo'.  Cause: java.lang.ClassNotFoundException: Cannot find class: SysAlimamaInfo org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext.refresh[557]
2020-01-14 17:46:54 INFO  -[                                ]- Closing JPA EntityManagerFactory for persistence unit 'default' org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.destroy[597]
2020-01-14 17:46:54 INFO  -[                                ]- {dataSource-1} closed com.alibaba.druid.pool.DruidDataSource.close[1823]
2020-01-14 17:46:54 INFO  -[                                ]- Stopping service [Tomcat] org.apache.catalina.core.StandardService.log[173]

```



### 2、问题来源  



1、使用Spring Boot，并使用Spring Boot的Maven插件打包     

2、使用MyBatis   

3、将实体Maven多模块

4、使用 `SqlSessionFactoryBean.setTypeAliasesPackage` 指定包扫描Domain

然后会发现：在开发时直接使用IDEA执行main方法运行时一切正常，但是打成Jar包后使用java -jar启动时配置的Domain别名均会失效。





### 3、解决方案 



```java
sqlSessionFactory.setVfs(SpringBootVFS.class);
```



```java
@Bean(name = "sqlSessionFactory")
public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
    throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources(mapperLocation));
    sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
    //MyBatis无法扫描Spring Boot别名的Bug 添加下面这行代码
    sessionFactory.setVfs(SpringBootVFS.class);
    return sessionFactory.getObject();
}
```













<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>       

   



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'Em7UEJiesXRkWLqp',
    });
    gitalk.render('gitalk-container');
</script> 

