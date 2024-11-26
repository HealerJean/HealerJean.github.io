---
title: SpringBoot数据源配置
date: 2020-06-03 03:33:00
tags: 
- Database
category: 
- Database
description: SpringBoot数据源配置
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`Spring` 配置文件

```xml
<!-- 数据源 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
</dependency>
```



```properties
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.druid.driver-class-name=com.mysql.cj.jdbc.Driver
# 初始化时建立物理连接的个数
spring.datasource.druid.initialSize=5
# 最小连接池数量
spring.datasource.druid.minIdle=5
# 最大连接池数量
spring.datasource.druid.maxActive=200
# 获取连接等待超时时间（如果连接池中没有可用连接，则等待获取连接的最大时间（毫秒）。如果等待时间超过这个值，则会抛出异常）
spring.datasource.druid.maxWait=60000
## 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
spring.datasource.druid.timeBetweenEvictionRunsMillis=60000
## 连接保持空闲而不被驱逐的最小时间
spring.datasource.druid.minEvictableIdleTimeMillis=300000
# 用来检测连接是否有效的sql，要求是一个查询语句
spring.datasource.druid.validationQuery=SELECT 1 FROM DUAL
# 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
spring.datasource.druid.testWhileIdle=true
# 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
spring.datasource.druid.testOnBorrow=false
# 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
spring.datasource.druid.testOnReturn=false

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/healerjean?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=123456
```



# 二、`DataSourcerConfig`

```properties
####################################
### DB
####################################
#durid
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.druid.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.druid.initialSize=5
spring.datasource.druid.minIdle=5
spring.datasource.druid.maxActive=20
spring.datasource.druid.maxWait=60000
spring.datasource.druid.timeBetweenEvictionRunsMillis=60000
spring.datasource.druid.minEvictableIdleTimeMillis=300000
spring.datasource.druid.validationQuery=SELECT 1 FROM DUAL
spring.datasource.druid.testWhileIdle=true
spring.datasource.druid.testOnBorrow=false
spring.datasource.druid.testOnReturn=false


####################################
### DB
####################################
spring.datasource.druid.url=jdbc:mysql://localhost:3306/hlj_jpa_mybatis_plus?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.druid.username=root
spring.datasource.druid.password=123456
####################################

```



```java
@Configuration
public class DataResourceConfig {
	
	
	@Value("${spring.datasource.druid.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.druid.url}")
	private String dbUrl;
	@Value("${spring.datasource.druid.username}")
	private String username;
	@Value("${spring.datasource.druid.password}")
	private String password;
	@Value("${spring.datasource.druid.initialSize}")
	private int initialSize;
	@Value("${spring.datasource.druid.minIdle}")
	private int minIdle;
	@Value("${spring.datasource.druid.maxActive}")
	private int maxActive;
	@Value("${spring.datasource.druid.maxWait}")
	private int maxWait;
	@Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;
	@Value("${spring.datasource.druid.validationQuery}")
	private String validationQuery;
	@Value("${spring.datasource.druid.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${spring.datasource.druid.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${spring.datasource.druid.testOnReturn}")
	private boolean testOnReturn;
	@Value("${keycenter.client.encrypt}")
	private boolean encrypt;
	
	@Bean(name = "dataSource")
	public DataSource dataSource(KeycenterUtils keycenterUtils) {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setDriverClassName(driverClassName);
		datasource.setUrl(dbUrl);
		if (encrypt) {
			datasource.setUsername(keycenterUtils.decrypt(username));
			datasource.setPassword(keycenterUtils.decrypt(password));
		} else {
			datasource.setUsername(username);
			datasource.setPassword(password);
		}
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		return datasource;
	}
	
}
```



# 三、连接配置

```http
jdbc:数据库类型://主机名:端口号/数据库名?参数1=值1&参数2=值2
```

## 1、基本配置

| 参数            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| **`jdbc`**:     | 前缀表明这是一个JDBC（Java Database Connectivity）连接。     |
| **数据库类型**: | 如`mysql`、`postgresql`、`oracle`等，指明了要连接的数据库类型。 |
| **主机名**:     | 数据库服务器的IP地址或域名。                                 |
| **端口号**:     | 数据库服务监听的端口，不同的数据库类型和服务配置可能使用不同的端口号。 |
| **数据库名**:   | 要连接的数据库的名称。                                       |
| **参数**:       | 连接字符串末尾可以附加多个参数，用于指定连接属性，如字符集、超时时间等。 |

## 2、参数

| 参数                                          | 说明                                                         | 建议                                                         |
| --------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **`useSSL`**                                  | 指定是否使用SSL加密数据库连接                                | 根据你的应用环境和安全需求设置。在生产环境中，如果数据库服务器支持且配置了SSL，建议使用SSL来加密数据库连接。 |
| **`allowPublicKeyRetrieval`**                 | 在启用SSL时，允许客户端从服务器请求公钥。                    | 与`useSSL`一起使用，以确保SSL连接的安全。                    |
| **`serverTimezone`**                          | 设置服务器时区，以解决时区不一致导致的问题。                 | 根据你的应用需求和数据库服务器的时区设置来选择。             |
| **`characterEncoding`** 和 **`useUnicode`**： | 设置客户端和服务器之间交换数据的字符编码。                   | 通常设置为`UTF-8`以支持国际化字符集。                        |
| **`connectTimeout`**                          | `connectTimeout`设置建立连接前的超时时间（毫秒）             | 根据你的网络环境和数据库服务器的响应时间来设置。             |
| **`socketTimeout`**                           | `socketTimeout`设置读写操作的超时时间（毫秒）。              | 根据你的网络环境和数据库服务器的响应时间来设置。             |
| **`autoReconnect`** （注意：已不推荐使用）    | 在旧版本的 `MySQL` `Connector`/J中，此参数用于在连接丢失时尝试自动重新连接。但在新版本中，它已被废弃，建议使用连接池来处理连接的重连。 | 不要在新开发的应用中使用此参数。考虑使用连接池（如HikariCP、C3P0等）来管理数据库连 |

```http
jdbc:mysql://healerjean.com:3306/healerjean?createDatabaseIfNotExist=true&characterEncoding=utf-8&useUnicode=true&connectTimeout=1000&socketTimeout=2000&amp;serverTimezone=Asia/Shanghai&useSSL=false]
```



### 1）`socketTimeout`

> **定义**：`socketTimeout` 是 `TCP` 层面的超时设置，它指的是客户端从服务器读取数据时的超时时间。在数据库连接的上下文中，这通常指的是  `JDBC` 客户端与 `MySQL`  服务器之间通过 `Socket` 传输数据时，等待服务器响应的超时时间。       
>
> **作用**：`socketTimeout` 用于防止在网络延迟或服务器无响应的情况下，客户端无限期地等待数据。如果设置了 `socketTimeout`，当超过这个时间后，客户端将抛出 `SocketTimeoutException`，从而允许应用程序采取相应的错误处理措施。     
>
> **设置方式**：在 JDBC 连接字符串中，可以通过添加 `socketTimeout` 参数来设置，例如：`jdbc:mysql://localhost:3306/mydatabase?socketTimeout=30000`（单位为毫秒）。





### 2）`SQL` 超时（statement timeout）

> **定义**：`SQL` 超时，通常通过 `statement timeout` 来实现，指的是单个 `SQL` 语句执行时的超时时间。这是数据库驱动层面或应用层面的设置，用于限制`SQL` 语句的执行时间，以防止长时间运行的查询耗尽系统资源。    
>
> **作用**：`statement timeout` 用于保护数据库和应用免受长时间运行查询的影响，确保系统资源得到合理分配和利用。    
>
> **设置方式**：在` JDBC` 中，可以通过调用 `Statement` 或 `PreparedStatement` 对象的 `setQueryTimeout(int seconds)` 方法来设置 `SQL` 超时时间（以秒为单位）。不过，也可以通过一些框架或连接池的配置来设置默认的 `SQL` 超时时间。

 

#### 关系

**级别差异**：`socketTimeout` 是 `TCP` 层面的超时，它作用于整个 `Socket` 连接上的数据传输；而 SQL 超时（`statement timeout`）是应用或数据库驱动层面的超时，它作用于单个 `SQL` 语句的执行。     

**相互影响**：虽然 `socketTimeout` 和 `SQL` 超时作用于不同的层面，但它们之间存在一定的相互影响。如果 `SQL` 语句的执行时间超过了 `socketTimeout` 设置的时间，那么即使 `SQL` 语句本身没有超时（即没有超过 `statement timeout`），客户端也可能因为 `socketTimeout` 而抛出异常。因此，在设置这些超时时，需要确保 `socketTimeout` 的值大于或等于可能的最大 `SQL` 执行时间（包括 `statement timeout`）。     

**配置建议**：为了避免死连接和长时间等待，建议同时设置 `socketTimeout` 和 `SQL` 超时（`statement timeout`）。`socketTimeout` 应该设置为足够长的时间，以容纳可能的网络延迟和服务器响应时间；而 `SQL` 超时则应该根据应用程序的实际需求和数据库的性能来设置。







# 四、项目应用

## 1、`MT`  (`70` 多台机器呢)

```java
  private int minPoolSize = 5;
  private int maxPoolSize = 20;
  private int initialPoolSize = 5;
  private int checkoutTimeout = 3000;
  private boolean removeAbandoned = true;
  private int timeBetweenEvictionRunsMillis = 600000;
  private int minEvictableIdleTimeMillis = 300000;
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
		id: '2JmawADTILxFblYn',
    });
    gitalk.render('gitalk-container');
</script> 
