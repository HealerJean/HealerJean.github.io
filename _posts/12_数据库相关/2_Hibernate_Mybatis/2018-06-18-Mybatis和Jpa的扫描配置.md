---
title: Mybatis和Jpa的扫描配置
date: 2018-06-18 03:33:00
tags: 
- Database
category: 
- Database
description: Mybatis和Jpa的扫描配置
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



#  1、`mybatis`配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="ScfSysDictionaryTypeMapper">
	<resultMap id="BaseResultMap" type="ScfSysDictionaryType">
		<id column="ID" jdbcType="BIGINT" property="id" />
		<result column="type_key" jdbcType="VARCHAR" property="typeKey" />
		<result column="type_desc" jdbcType="VARCHAR" property="typeDesc" />
		<result column="status" jdbcType="VARCHAR" property="status" />
		<result column="create_user" jdbcType="BIGINT" property="createUser" />
		<result column="create_name" jdbcType="VARCHAR" property="createName" />
		<result column="create_time" jdbcType="TIMESTAMP" property="createTime" />
		<result column="update_user" jdbcType="BIGINT" property="updateUser" />
		<result column="update_name" jdbcType="VARCHAR" property="updateName" />
		<result column="update_time" jdbcType="TIMESTAMP" property="updateTime" />
	</resultMap>
	<sql id="Example_Where_Clause">
		<where>
			<trim prefix="(" prefixOverrides="and" suffix=")">
				<if test="typeKey != null">
					and type_key = #{typeKey,jdbcType=VARCHAR}
				</if>
				<if test="typeDesc != null">
					and type_desc = #{typeDesc,jdbcType=VARCHAR}
				</if>
				<if test="status != null">
					and status = #{status,jdbcType=VARCHAR}
				</if>
				<if test="createUser != null and createUser != ''">
					and create_user = #{createUser,jdbcType=BIGINT}
				</if>
				<if test="createName != null">
					and create_name = #{createName,jdbcType=VARCHAR}
				</if>
				<if test="updateUser != null and updateUser != ''">
					and update_user = #{updateUser,jdbcType=BIGINT}
				</if>
				<if test="updateName != null">
					and update_name = #{updateName,jdbcType=VARCHAR}
				</if>
			</trim>
		</where>
	</sql>
	<sql id="Example_Set_Clause">
		<set>
			<trim suffixOverrides=",">
				<if test="typeKey != null">
					type_key = #{typeKey,jdbcType=VARCHAR},
				</if>
				<if test="typeDesc != null">
					type_desc = #{typeDesc,jdbcType=VARCHAR},
				</if>
				<if test="status != null">
					status = #{status,jdbcType=VARCHAR},
				</if>
				<if test="createUser != null">
					create_user = #{createUser,jdbcType=BIGINT},
				</if>
				<if test="createName != null">
					create_name = #{createName,jdbcType=VARCHAR},
				</if>
				<if test="updateUser != null">
					update_user = #{updateUser,jdbcType=BIGINT},
				</if>
				<if test="updateName != null">
					update_name = #{updateName,jdbcType=VARCHAR},
				</if>
				UPDATE_TIME = NOW(),
			</trim>
		</set>
	</sql>
	<sql id="Base_Column_List">
		id,
		type_key,
		type_desc,
		status,
		create_user,
		create_name,
		create_time,
		update_user,
		update_name,
		update_time
	</sql>
	<sql id="Base_Set_Clause">
		<set>
			<trim suffixOverrides=",">
				type_key = #{typeKey,jdbcType=VARCHAR},
				type_desc = #{typeDesc,jdbcType=VARCHAR},
				status = #{status,jdbcType=VARCHAR},
				create_user = #{createUser,jdbcType=BIGINT},
				create_name = #{createName,jdbcType=VARCHAR},
				update_user = #{updateUser,jdbcType=BIGINT},
				update_name = #{updateName,jdbcType=VARCHAR},
			</trim>
		</set>
	</sql>
	<sql id="Selective_Column_List">
		ID,
		<if test="typeKey != null">
			type_key,
		</if>
		<if test="typeDesc != null">
			type_desc,
		</if>
		<if test="status != null">
			status,
		</if>
		<if test="createUser != null">
			create_user,
		</if>
		<if test="createName != null">
			create_name,
		</if>
		CREATE_TIME,
		<if test="updateUser != null">
			update_user,
		</if>
		<if test="updateName != null">
			update_name,
		</if>
	</sql>
	<sql id="Base_Value_List">
		#{id,jdbcType=BIGINT},
		#{typeKey,jdbcType=VARCHAR},
		#{typeDesc,jdbcType=VARCHAR},
		#{status,jdbcType=VARCHAR},
		#{createUser,jdbcType=BIGINT},
		#{createName,jdbcType=VARCHAR},
		#{createTime,jdbcType=TIMESTAMP},
		#{updateUser,jdbcType=BIGINT},
		#{updateName,jdbcType=VARCHAR},
		#{updateTime,jdbcType=TIMESTAMP},
	</sql>
	<sql id="Selective_Value_List">
		#{id},
		<if test="typeKey != null">
			#{typeKey,jdbcType=VARCHAR},
		</if>
		<if test="typeDesc != null">
			#{typeDesc,jdbcType=VARCHAR},
		</if>
		<if test="status != null">
			#{status,jdbcType=VARCHAR},
		</if>
		<if test="createUser != null">
			#{createUser,jdbcType=BIGINT},
		</if>
		<if test="createName != null">
			#{createName,jdbcType=VARCHAR},
		</if>
		NOW(),
		<if test="updateUser != null">
			#{updateUser,jdbcType=BIGINT},
		</if>
		<if test="updateName != null">
			#{updateName,jdbcType=VARCHAR},
		</if>
	</sql>

	<select id="selectByExample" parameterType="ScfSysDictionaryTypeQuery" resultMap="BaseResultMap">
		select
		<trim suffixOverrides=",">
			<include refid="Base_Column_List" />
		</trim>
		from scf_sys_dictionary_type
		<include refid="Example_Where_Clause" />
		<!-- order by CREATE_DATE -->
	</select>
	<select id="selectByPrimaryKey" parameterType="Long" resultMap="BaseResultMap">
		select
		<trim suffixOverrides=",">
			<include refid="Base_Column_List" />
		</trim>
		from scf_sys_dictionary_type
		where ID = #{id,jdbcType=BIGINT}
	</select>
	<delete id="deleteByPrimaryKey" parameterType="Long">
		delete from scf_sys_dictionary_type
		where ID = #{id,jdbcType=BIGINT}
	</delete>
	<delete id="deleteByExample" parameterType="ScfSysDictionaryTypeQuery">
		delete from scf_sys_dictionary_type
		<include refid="Example_Where_Clause" />
	</delete>
	<insert id="insert" parameterType="ScfSysDictionaryType">
		insert into scf_sys_dictionary_type
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<include refid="Base_Column_List" />
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
			<include refid="Base_Value_List" />
		</trim>
	</insert>
	<insert id="insertSelective" parameterType="ScfSysDictionaryType" useGeneratedKeys="true" keyProperty="id">
		insert into scf_sys_dictionary_type
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<include refid="Selective_Column_List" />
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
			<include refid="Selective_Value_List" />
		</trim>
	</insert>
	<select id="countByExample" parameterType="ScfSysDictionaryTypeQuery" resultType="java.lang.Integer">
		select count(*) from scf_sys_dictionary_type
		<include refid="Example_Where_Clause" />
	</select>
	<update id="updateByPrimaryKeySelective" parameterType="ScfSysDictionaryType">
		update scf_sys_dictionary_type
		<include refid="Example_Set_Clause" />
		where ID = #{id,jdbcType=BIGINT}
	</update>
	<update id="updateByPrimaryKey" parameterType="ScfSysDictionaryType">
		update scf_sys_dictionary_type
		<include refid="Base_Set_Clause" />
		where ID = #{id,jdbcType=BIGINT}
	</update>

	<select id="selectPageByExample" parameterType="ScfSysDictionaryTypeQuery" resultMap="BaseResultMap">
		select
		<trim suffixOverrides=",">
			<include refid="Base_Column_List" />
		</trim>
		from scf_sys_dictionary_type
		<include refid="Example_Where_Clause" />
        <![CDATA[ limit #{startRow},#{endRow} ]]>
  		<!-- order by CREATE_DATE -->
	</select>



	<select id="countDictTypeLikes" parameterType="ScfSysDictionaryTypeQuery" resultType="java.lang.Integer">
		select count(*) from scf_sys_dictionary_type
		<include refid="Example_Where_Like_Clause" />
	</select>

	<select id="queryDictTypePageLikes" parameterType="ScfSysDictionaryTypeQuery" resultMap="BaseResultMap">
		select
		<trim suffixOverrides=",">
			<include refid="Base_Column_List" />
		</trim>
		from scf_sys_dictionary_type
		<include refid="Example_Where_Like_Clause" />
		<![CDATA[ limit #{startRow},#{endRow} ]]>
	</select>

	<select id="queryDictTypeLikes" parameterType="ScfSysDictionaryTypeQuery" resultMap="BaseResultMap">
		select
		<trim suffixOverrides=",">
			<include refid="Base_Column_List" />
		</trim>
		from scf_sys_dictionary_type
		<include refid="Example_Where_Like_Clause" />
	</select>


	<sql id="Example_Where_Like_Clause">
		<where>
			<trim prefix="(" prefixOverrides="and" suffix=")">
				<if test="typeKey != null">
					and type_key    LIKE CONCAT('%', #{typeKey,jdbcType=VARCHAR},'%')
				</if>
				<if test="status != null">
					and status = #{status,jdbcType=VARCHAR}
				</if>
			</trim>
		</where>
	</sql>

</mapper>
```





##  1.1、扫描Pojo和Example、mapper.java和mapper.xml

> zzr


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



## 1.2、mapper和xml的扫描

> 多点的时候

### 1.2.1、pom依赖

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

### 1.2.2、`MybatisConfig`

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



### 1.2.2、`mybatis.xml`

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



## 1.3、@MapperScan注解和xml的扫描 

> 其实和1.2是一回事  

### 1.3.1、`Configuration`


```java

//可以没有sqlSessionTemplateRef 以及关于它的Bean，使用它的情况是 当你有多个Datasource时，需要指定使用哪一个SqlSessionTemplate
@MapperScan(basePackages = {"com.hlj.proj.data.dao.mybatis"}, sqlSessionTemplateRef  = "sqlSessionTemplate")




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



## 1.4、配置文件扫描xml和pojo

### 1.4.1、pom依赖

```xml
<!-- mybatis -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>
```

### 1.4.2、properties配置

```properties
//如果是分业务的xml
mybatis.mapper-locations=classpath*:/mapper/mysql/**/*.xml
//如果有多个路径的实体的包
mybatis.type-aliases-package=com.fintech.sc.pojo,com.fintech.scf.pojo



mybatis.mapper-locations=classpath:/mapper/mysql/*.xml
mybatis.type-aliases-package=com.fintech.scf.data.pojo
```



### 1.4.3、`@Configuration`

```java
@Value("${hlj.datasource.url}")
private String admoreUrl;
@Value("${hlj.datasource.username}")
private String admoreUsername;
@Value("${hlj.datasource.password}")
private String admorePassword;


@Value("${mybatis.mapper-locations}")
private String mapperLocation;

@Value("${mybatis.type-aliases-package}")
private String typeAliasesPackage;

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


@Bean(name = "sqlSessionFactory")
public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
    throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources(mapperLocation));
    sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
    return sessionFactory.getObject();
}

```




# 2、Jpa扫描



## 2.1、实体扫描不使用@EntityScan，

> 1、`entityManagerFactoryRef` 配置实体扫描 ，       
>
> 2、下面是采用了我们自己设置的事务，也就是说不配置`transactionManagerRef`

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
     */
    @Bean("localEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactoryBean (
        EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.hlj.proj.data.pojo")
            //任意
            .persistenceUnit("hlj")
            .build();
    }
    
    @Bean("jpaTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(
        @Qualifier("localEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean ) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }


}

```



## 2.2、`@EntityScan`扫描和使用默认的事务管理器

```java
//EnableJpaRepositories 
//1、entityManagerFactoryRef 有默认值entityManagerFactory ，写了EntityScan不传递该参数，而如果不写该参数就必须写EntityScan了，因为实体必须要扫描的
//2、EnableJpaRepositories 中 transactionManagerRef   有默认值 transactionManager，所以可以不写
@EnableJpaRepositories( basePackages= { "com.hlj.proj.data.dao.db" })
@EntityScan(basePackages = {"com.hlj.proj.data.pojo"})
```





# 3、MybatisPlus

> 具体看MybatisPlus文章吧











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
		id: '9BpmF2boUCxjsXlI',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

