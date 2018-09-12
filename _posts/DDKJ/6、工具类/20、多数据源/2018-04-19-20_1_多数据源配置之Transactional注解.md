---
title: 1、多数据源配置之Transactional注解不同的事务管理器控制
date: 2018-04-24 09:33:00
tags: 
- Database
category: 
- Database
description: 多数据源配置之Transactional注解不同的事务管理器控制
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言
1、一定注意是maven项目，在添加mybatis的Resource中扫描classpath的过程中可能会报错，所以需要加入

2、这里和我后面一章介绍的差不多，但是需要在使用中注意的是，我们的实体类，尽量不要放到一起，尽管可以使用。

```

1、因为加入我们的实体类放到一起。很容易混淆不同数据库的表
2、如果是springBoot关于hibernate自动生成表则可能在每个数据源扫描的时候自动帮我们添加表，则会形成两个库中的表一样，但是却有些重复不使用。
```



```

	<resources>
		<resource>
			<directory>src/main/i18n</directory>
		</resource>
		<resource>
			<directory>src/main/resources</directory>
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

</build>


```

## 1、配置信息中写入多种数据源的配置连接信息

```


########################################################
### jpa
########################################################
spring.jpa.database=MYSQL
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto =update
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect



#datasource admore
admore.datasource.url=jdbc:mysql://localhost:3306/com_hlj_many_database-one
admore.datasource.username=root
admore.datasource.password=123456
admore.datasource.driver-class-name=com.mysql.jdbc.Driver

#datasource admore-data
admore.data.datasource.url=jdbc:mysql://localhost:3306/com_hlj_many_database-two
admore.data.datasource.username=root
admore.data.datasource.password=123456
admore.data.datasource.driver-class-name=com.mysql.jdbc.Driver



```

## 2、启用事物注解

```
package com.hlj.many.datasourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ComHljManyDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComHljManyDatasourceApplication.class, args);
	}
}


```

## 2、配置默认的数据源（通过注解@primary）


### 1、配置JPa扫描的路径和mybatis扫描的路径



```
package com.hlj.many.datasourse.dataresource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 类描述：
 * 创建人： j.sh
 * 创建时间： 2016/9/10
 * version：1.0.0
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="admoreEMF",
        transactionManagerRef="admoreTM",
        basePackages= { "com.hlj.many.datasourse.dataresource.dao.data" })
@MapperScan(basePackages = {"com.hlj.many.datasourse.dataresource.dao.data"}, sqlSessionTemplateRef  = "admoreSessionTemplate")
public class DatasourceOneData {

    @Value("${admore.datasource.url}")
    private String admoreUrl;
    @Value("${admore.datasource.username}")
    private String admoreUsername;
    @Value("${admore.datasource.password}")
    private String admorePassword;


    @Primary
    @Bean(name = "admore")
    public DataSource primaryDataSource() {
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

    @Primary
    @Bean(name = "admoreEMF")
    public LocalContainerEntityManagerFactoryBean admoreEMF(EntityManagerFactoryBuilder builder, @Qualifier("admore") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.hlj.many.datasourse.dataresource.dao.data.entry")
                .persistenceUnit("admore")
                .build();
    }


    @Primary
    @Bean(name = "admoreTM")
    public PlatformTransactionManager admoreTransactionManager(@Qualifier("admoreEMF") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean ) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

    @Primary
    @Bean(name = "admoreSessionFactory")
    public SqlSessionFactory admoreSessionFactory(@Qualifier("admore")DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis.xml"));
        Resource[] resources = ArrayUtils.addAll(
                applicationContext.getResources("classpath*:com/hlj/many/datasourse/dataresource/dao/data/mysql/*.xml")
//             ,applicationContext.getResources("classpath*:com/duodian/admore/dao/db/**/mysql/*.xml")
        );
        sessionFactoryBean.setMapperLocations(resources);
        return sessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "admoreSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("admoreSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}


```


## 4、其他数据源


```
package com.hlj.many.datasourse.dataresource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 类描述：
 * 创建人： j.sh
 * 创建时间： 2016/9/10
 * version：1.0.0
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="admoreDataEMF",
        transactionManagerRef="admoreDataTM",
        basePackages= { "com.hlj.many.datasourse.dataresource.dao.db" })
        @MapperScan(basePackages = {"com.hlj.many.datasourse.dataresource.dao.db"}, sqlSessionTemplateRef  = "admoreDataSessionTemplate")
//@MapperScan(basePackages = {"com.hlj.many.datasourse.dataresource.dao.db","com.hlj.many.datasourse.dataresource.dao.data.mydata"}, sqlSessionTemplateRef  = "admoreSessionTemplate")
public class DatasourceTwoDb {

    @Value("${admore.data.datasource.url}")
    private String admoreDataUrl;
    @Value("${admore.data.datasource.username}")
    private String admoreDataUsername;
    @Value("${admore.data.datasource.password}")
    private String admoreDataPassword;

    @Bean(name = "admoreData")
    public DataSource primaryDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(admoreDataUrl);
        druidDataSource.setUsername(admoreDataUsername);
        druidDataSource.setPassword(admoreDataPassword);
        druidDataSource.setMaxActive(150);
        druidDataSource.setInitialSize(10);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setMaxWait(3000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);

        return druidDataSource;
    }

    @Bean(name = "admoreDataEMF")
    public LocalContainerEntityManagerFactoryBean admoreDataEMF(EntityManagerFactoryBuilder builder, @Qualifier("admoreData") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.hlj.many.datasourse.dataresource.dao.db.entry")
                .persistenceUnit("admoreData")
                .build();
    }

    @Bean(name = "admoreDataTM")
    public PlatformTransactionManager admoreTransactionManager(@Qualifier("admoreDataEMF") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean ) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

    @Bean(name = "admoreDataSessionFactory")
    public SqlSessionFactory admoreDataSessionFactory(@Qualifier("admoreData") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis.xml"));
        Resource[] resources = ArrayUtils.addAll(
                applicationContext.getResources("classpath*:com/hlj/many/datasourse/dataresource/dao/db/mysql/*.xml")
//                applicationContext.getResources("classpath*:com/duodian/admore/dao/mydata/**/mysql/*.xml")
        );
        sessionFactoryBean.setMapperLocations(resources);
        return sessionFactoryBean.getObject();
    }



    @Bean(name = "admoreDataSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("admoreDataSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


```


## 5、多数据源直接的使用

### 5.1、默认的数据源

```
package com.hlj.many.datasourse.dataresource.service;

import com.hlj.many.datasourse.dataresource.dao.data.PersonOneDao;
import com.hlj.many.datasourse.dataresource.dao.data.entry.PersonOne;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/19  下午1:58.
 */
@Service
public class MainService {

    @Resource
    private PersonOneDao personOneDao;



    /**
     * 默认选择第一种数据源
     * @param personOne
     * @return
     */
    public PersonOne save(PersonOne personOne){
       return personOneDao.save(personOne);
    }

    
}


```


### 5.2、指定第一种数据源

```
package com.hlj.many.datasourse.dataresource.service;

import com.hlj.many.datasourse.dataresource.dao.data.PersonOneDao;
import com.hlj.many.datasourse.dataresource.dao.data.PersonOneMapper;
import com.hlj.many.datasourse.dataresource.dao.data.entry.PersonOne;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/19  下午1:58.
 */
@Service
@Transactional(rollbackFor = Exception.class,transactionManager = "admoreTM")
public class OneService {

    @Resource
    private PersonOneDao personOneDao;

    @Resource
    private PersonOneMapper personOneMapper;

    public PersonOne save(PersonOne personOne){
        return personOneDao.save(personOne);
    }

    public PersonOne findById(Long id){
       return personOneMapper.findById(id);
    }

}


```


### 5.3、指定第二种数据源


```
package com.hlj.many.datasourse.dataresource.service;

import com.hlj.many.datasourse.dataresource.dao.db.PersonTwoDao;
import com.hlj.many.datasourse.dataresource.dao.db.PersonTwoMapper;
import com.hlj.many.datasourse.dataresource.dao.db.entry.PersonTwo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/19  下午1:58.
 */
@Service
@Transactional(rollbackFor = Exception.class,transactionManager = "admoreDataTM")
public class TwoService {


    @Resource
     private PersonTwoDao personTwoDao;


    @Resource
    private PersonTwoMapper personTwoMapper;

    public PersonTwo save(PersonTwo personTwo){
        return personTwoDao.save(personTwo);
    }

    public PersonTwo findById(Long id){
        return personTwoMapper.findById(id);
    }


    public List<PersonTwo> findALL(){
        return personTwoMapper.findALL();
    }

}


```


## 6、项目结构

![WX20180424-153641](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180424-153641.png)


## 7、[代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2019-04-19-20_1_%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E9%85%8D%E7%BD%AE%E4%B9%8BTransactional%E6%B3%A8%E8%A7%A3/com-hlj-many-datasource.zip)



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
		id: 'BaTOzRtu8bdz0Ug7',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

