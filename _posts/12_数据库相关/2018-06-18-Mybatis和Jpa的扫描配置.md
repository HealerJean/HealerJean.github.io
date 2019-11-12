---
title: Mybatis和Jpa的扫描配置
date: 2018-06-18 03:33:00
tags: 
- Database
category: 
- Database
description: Mybatis和Jpa的扫描配置
---

<!-- __

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    





###  1、mybatis配置

####  1.1、扫描Pojo和Example、mapper.java和mapper.xml


```xml

    
      <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="typeAliasesPackage">
        	<value>
        		org.dsp.core.model.entity
        		org.dsp.ea.pay.model.entity
        		org.dsp.ea.pout.model.entity
        		org.dsp.ea.accestablish.model.entity
        		org.dsp.ea.contribution.model.entity
        		org.dsp.ea.modifyInfo.model.entity
        		org.dsp.oa.model.entity
        	</value>
        </property>
    </bean>

 <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="sqlSessionFactory" ref="sqlSessionFactory" />
        <property name="basePackage">
        	<value>
        		org.dsp.core.dao.mybatis
	        	org.dsp.ea.pay.dao.mybatis
	        	org.dsp.ea.pout.dao.mybatis
	        	org.dsp.ea.accestablish.dao.mybatis
	        	org.dsp.ea.contribution.dao.mybatis
	        	org.dsp.ea.modifyInfo.dao.mybatis
	        	org.dsp.oa.dao.mybatis
        	</value>
        </property>
    </bean>
    
    
    
    

```



#### 1.2、mapper和xml的扫描



```xml

        
  <!--mybatis-->
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis</artifactId>
                <version>3.4.0</version>
            </dependency>

            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis-spring</artifactId>
                <version>>1.3.0</version>
            </dependency>

```



##### 1.2.1、`MybatisConfig`

```java
@Configuration
public class MybatisConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.hlj.dao.mybatis.*");
        return configurer;
    }

    @Bean
    public SqlSessionFactoryBean sessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws IOException {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis.xml"));

        Resource[] resources = ArrayUtils.addAll(
                applicationContext.getResources("classpath*:com/hlj/dao/mybatis/**/mysql/*.xml")
        );
        sessionFactoryBean.setMapperLocations(resources);
        return sessionFactoryBean;
    }


}

```



##### 1.2.2、mybatis.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="cacheEnabled" value="false" />
        <setting name="lazyLoadingEnabled" value="false" />
        <setting name="defaultStatementTimeout" value="25000" />
    </settings>

</configuration>
```



#### 1.3、@MapperScan注解 和xml的扫描 ，其实和1.2是一回事


```java

//可以没有sqlSessionTemplateRef 以及关于它的Bean，使用它的情况是 当你有多个Datasource时，需要指定使用哪一个SqlSessionTemplate

@MapperScan(basePackages = {"com.hlj.proj.data.dao.mybatis"}, sqlSessionTemplateRef  = "sqlSessionTemplate")



    /**
     * 配置mybatis
     * @param dataSource
     * @param applicationContext
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis.xml"));
        Resource[] resources = ArrayUtils.addAll(
                applicationContext.getResources("classpath*:com/hlj/proj/data/dao/mybatis/**/mysql/*.xml")
        );
        sessionFactoryBean.setMapperLocations(resources);
        return sessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }




```



#### 1.4、配置文件扫描xml和pojo



```xml
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
```



```java
//如果是分业务的xml
mybatis.mapper-locations=classpath*:/mapper/mysql/**/*.xml
//如果有路径的实体的包
mybatis.type-aliases-package=com.fintech.sc.pojo,com.fintech.scf.pojo




mybatis.mapper-locations=classpath:/mapper/mysql/*.xml
mybatis.type-aliases-package=com.fintech.scf.data.pojo

```


```java
@Value("${mybatis.mapper-locations}")
private String mapperLocation;

@Value("${mybatis.type-aliases-package}")
private String typeAliasesPackage;
    
    

@Bean(name = "sqlSessionFactory")
public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource)
    throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources(mapperLocation));
    sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
    return sessionFactory.getObject();
}

```


### 2、Jpa扫描



#### 2.1、实体扫描不使用@EntityScan，

1、`entityManagerFactoryRef` 配置实体扫描    

2、下面是采用了我们自己设置的事务，当然也可以不设置 也就是说取消`transactionManagerRef`

```java
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef="localEntityManagerFactoryBean",
        transactionManagerRef="dataSourceTransactionManager",
        basePackages= { "com.hlj.proj.data.dao.db" })

@MapperScan(basePackages = {"com.hlj.proj.data.dao.mybatis"}, sqlSessionTemplateRef  = "sqlSessionTemplate")
@PropertySource("classpath:db.properties")
public class DatasourceConfig {

    @Value("${hlj.datasource.url}")
    private String admoreUrl;
    @Value("${hlj.datasource.username}")
    private String admoreUsername;
    @Value("${hlj.datasource.password}")
    private String admorePassword;


    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(admoreUrl);
        druidDataSource.setUsername(admoreUsername);
        druidDataSource.setPassword(admorePassword);
        druidDataSource.setMaxActive(150);
        druidDataSource.setInitialSize(10);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setMaxWait(3000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);

        return druidDataSource;
    }

    /**
     * 配置实体扫描 和 事务管理器，
     * 1、不配置配置entityManagerFactoryBean， 可以直接使用注解 @EntityScan(basePackages = {"com.hlj.proj.data.pojo"})
     * 2、可以不配置下面的 因为导入的maven是  spring-boot-starter-data-jpa 默认的事务管理器就是 JpaTransactionManager
     * @param builder
     * @param dataSource
     * @return
     */
    @Bean("localEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactoryBean (EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.hlj.proj.data.pojo")
                //任意
                .persistenceUnit("hlj")
                .build();
   }
    @Bean("jpaTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("localEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean ) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

 


}

```





#### 4、@EntityScan扫描和使用默认的事务管理器

```java
//EnableJpaRepositories 
//1、entityManagerFactoryRef 有默认值entityManagerFactory ，写了EntityScan不传递该参数，而如果不写该参数就必须写EntityScan了，因为实体必须要扫描的
//2、EnableJpaRepositories 中 transactionManagerRef   有默认值 transactionManager，所以可以不写
@EnableJpaRepositories( basePackages= { "com.hlj.proj.data.dao.db" })
@EntityScan(basePackages = {"com.hlj.proj.data.pojo"})

```







<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

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
		id: '9BpmF2boUCxjsXlI',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

