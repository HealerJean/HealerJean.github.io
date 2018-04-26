---
title: 1、SpringBoot继承mybatis实现类似JPA方法
date: 2018-04-26 11:29:00
tags: 
- SpringBoot
- Mybatis
category: 
- SpringBoot
- Mybatis
description: SpringBoot继承mybatis实现类似JPA方法
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

主要是使用到了逆向工程（和之前逆向工程有点区别 ，就是不需要生成exmaple类）和插件`tk.mybatis
`


## 1、依赖如下

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.hlj.mybatisxml</groupId>
	<artifactId>springboot-mybatisxml</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>springboot-mybatisxml</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.6.RELEASE</version>
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
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.1</version>
		</dependency>

		<dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper-spring-boot-starter</artifactId>
            <version>1.1.1</version>
        </dependency>

		<!-- 逆向生成代码 -->
		<dependency>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-core</artifactId>
			<version>1.3.6</version>
		</dependency>


	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<!--
			貌似是没有用到
			 <plugin>
			    <groupId>org.mybatis.generator</groupId>
			    <artifactId>mybatis-generator-maven-plugin</artifactId>
			    <version>1.3.2</version>
			    <configuration>
			        <configurationFile>${basedir}/src/main/resources/generatorConfig.xml</configurationFile>
			        <overwrite>true</overwrite>
			        <verbose>true</verbose>
			    </configuration>
			    <dependencies>
			        <dependency>
			        <groupId>mysql</groupId>
			        <artifactId>mysql-connector-java</artifactId>
			        <version>5.1.43</version>
			        </dependency>
			        <dependency>
			        <groupId>tk.mybatis</groupId>
			        <artifactId>mapper</artifactId>
			        <version>3.4.0</version>
			        </dependency>
			    </dependencies>
			</plugin>-->
		</plugins>
	</build>
</project>


   
```

## 2、配置数据库配置信息以及扫描的mapper的xml路径

#### 2.1、注意下面的BaseMapper 为我们将来自己定义的mapper的实现接口，它里面具备了各种我们需要的方法，类似于CruRepository

```
package com.hlj.mybatisxml.utility;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
/**
 * 
 * @author hlj
 * @date 2017年9月2日
 * @param <T>
 */
public interface BaseMapper<T> extends Mapper<T>,MySqlMapper<T> {

}

```

```

spring.datasource.url=jdbc:mysql://localhost:3306/springboot_mybatis_aotumatic_method?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver


### jpa
########################################################
spring.jpa.database=MYSQL
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto =update
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect



mapper.plugin = tk.mybatis.mapper.generator.MapperPlugin
mapper.Mapper = tk.mybatis.mapper.common.Mapper
mybatis.type-aliases-package=com.study.model
mybatis.mapper-locations=classpath:mapper/*/*.xml
mapper.mappers=com.hlj.mybatisxml.utility.BaseMapper
mapper.not-empty=false
mapper.identity=MYSQL

```



## 3、启动类，启动扫描mapper类

```
package com.hlj.mybatisxml;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hlj.mybatisxml.mapper")
public class SpringbootMybatisxmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisxmlApplication.class, args);
	}
}

```

## 4、逆向工程配置文件


```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>   
    
 
	<context id="baseset"  targetRuntime="MyBatis3Simple" defaultModelType="flat">




		<!-- 根据Mapper生成实体类中引入接口BaseMapper的-->
        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="com.hlj.mybatisxml.utility.BaseMapper"/>
        </plugin>

		<commentGenerator>
			<!-- 是否去除自动生成的注释 true：是 ： false:否 -->
			<property name="suppressAllComments" value="true" />
		</commentGenerator>

		<!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
		<jdbcConnection driverClass="com.mysql.jdbc.Driver"
							connectionURL="jdbc:mysql://localhost:3306/springboot_mybatis_aotumatic_method"
							userId="root"
							password="123456">
			</jdbcConnection>

		<!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
			NUMERIC 类型解析为java.math.BigDecimal -->
		<javaTypeResolver>
			<property name="forceBigDecimals" value="false" />
		</javaTypeResolver>




		<!-- 指定生成“entity实体类、mybatis映射xml文件、mapper接口”的具体位置 -->	
		 <javaModelGenerator targetPackage="com.hlj.mybatisxml.entity.baseset" targetProject="src/main/java" >
            <property name="enableSubPackages" value="false"/>
            <property name="trimStrings" value="true"/>  
        </javaModelGenerator>

		<!-- targetProject:mapper映射文件生成的位置 -->
		<sqlMapGenerator targetPackage="mapper.baseset" targetProject="src/main/resources" >  
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>

		<!-- targetPackage：mapper接口生成的位置 -->
		<javaClientGenerator targetPackage="com.hlj.mybatisxml.mapper.baseset" targetProject="src/main/java"   type="XMLMAPPER" >
            <property name="enableSubPackages" value="false"/>
        </javaClientGenerator>


		<!-- 具体要生成的表，如果有多个表，复制这一段，改下表名即可 -->
		<table   tableName="country"  domainObjectName="Country"
									 enableCountByExample="false"
									 enableUpdateByExample="false"
									 enableDeleteByExample="false"
									 enableSelectByExample="false"
									 selectByExampleQueryId="false">
		</table>
  </context>
</generatorConfiguration>

```

## 5、逆向工程生成类(将来不会生成Example，应该是具备了插件的原因)


```
package com.hlj.mybatisxml;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/26  上午11:02.
 */
public class GeneratorSqlmap {

    public void generator() throws Exception{

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件
            String path = this.getClass().getClassLoader().getResource("generatorConfig.xml").getPath();
        File configFile = new File(path);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

    }
    public static void main(String[] args) throws Exception {
        try {
            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

```

## 6、逆向生成如下

### 6.1、mapper

```
package com.hlj.mybatisxml.mapper.baseset;

import com.hlj.mybatisxml.entity.baseset.Country;
import com.hlj.mybatisxml.utility.BaseMapper;

public interface CountryMapper extends BaseMapper<Country> {
}

```

### 6.2、mapper.xml

#### resultMap 可以直接删掉，没什么用

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.hlj.mybatisxml.mapper.baseset.CountryMapper">
  <resultMap id="BaseResultMap" type="com.hlj.mybatisxml.entity.baseset.Country">
    <!--
      WARNING - @mbg.generated
    -->
    <id column="id" jdbcType="INTEGER" property="id" />
    <result column="countryname" jdbcType="VARCHAR" property="countryname" />
    <result column="countrycode" jdbcType="VARCHAR" property="countrycode" />
  </resultMap>
</mapper>

```

### 6.3、实体类


```
package com.hlj.mybatisxml.entity.baseset;

import javax.persistence.*;

public class Country {
    @Id
    private Integer id;

    private String countryname;

    private String countrycode;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return countryname
     */
    public String getCountryname() {
        return countryname;
    }

    /**
     * @param countryname
     */
    public void setCountryname(String countryname) {
        this.countryname = countryname == null ? null : countryname.trim();
    }

    /**
     * @return countrycode
     */
    public String getCountrycode() {
        return countrycode;
    }

    /**
     * @param countrycode
     */
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode == null ? null : countrycode.trim();
    }
}

```

## 7、其实很简单的，我们需要注意的是。
### 1、可以利用jpa生成数据库表
### 2、可以利用数据库表生成实体，但是实体要进入使用，需要加上id主键信息，因为逆向工程不会加上主键信息的。

## 3、要和jpa结合则需要加上 @Entry 等表格信息

```
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

```


## 8、[代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_04_26_7_springBoot%E7%BB%A7%E6%89%BFmybatis%E5%AE%9E%E7%8E%B0%E7%B1%BB%E4%BC%BCJPA%E6%96%B9%E6%B3%95/com-hlj-mybatis-Automatic-method.zip)





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
		id: 'No8hH4UDT5ZNpUVL',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

