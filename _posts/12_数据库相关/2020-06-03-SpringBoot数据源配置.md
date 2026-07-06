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



# 一、数据源配置

## 1、服务配置

### 1）`pom` 依赖

```xml
<!-- 数据源 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
</dependency>
```



### 2）`yml`

```properties
####################################
### DB
####################################
#durid
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

####################################
### DB
####################################
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/healerjean?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=123456
```



### 3）`DataSourcerConfig`

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



# 二、`url` 连接配置

```
jdbc:数据库类型://主机名:端口号/数据库名?参数1=值1&参数2=值2
```

| 参数            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| **`jdbc`**:     | 前缀表明这是一个JDBC（Java Database Connectivity）连接。     |
| **数据库类型**: | 如`mysql`、`postgresql`、`oracle`等，指明了要连接的数据库类型。 |
| **主机名**:     | 数据库服务器的IP地址或域名。                                 |
| **端口号**:     | 数据库服务监听的端口，不同的数据库类型和服务配置可能使用不同的端口号。 |
| **数据库名**:   | 要连接的数据库的名称。                                       |
| **参数**:       | 连接字符串末尾可以附加多个参数，用于指定连接属性，如字符集、超时时间等。 |

## 1、核心参数

| 参数                                          | 说明                                                         | 建议                                                         |
| --------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **`useSSL`**                                  | 指定是否使用SSL加密数据库连接                                | 根据你的应用环境和安全需求设置。在生产环境中，如果数据库服务器支持且配置了SSL，建议使用SSL来加密数据库连接。 |
| **`allowPublicKeyRetrieval`**                 | 在启用 `SSL`c 时，允许客户端从服务器请求公钥。               | 与`useSSL`一起使用，以确保SSL连接的安全。                    |
| **`serverTimezone`**                          | 设置服务器时区，以解决时区不一致导致的问题。                 | 根据你的应用需求和数据库服务器的时区设置来选择。             |
| **`characterEncoding`** 和 **`useUnicode`**： | 设置客户端和服务器之间交换数据的字符编码。                   | 通常设置为`UTF-8`以支持国际化字符集。                        |
| **`connectTimeout`**                          | `connectTimeout`设置建立连接前的超时时间（毫秒）             | 根据你的网络环境和数据库服务器的响应时间来设置。             |
| **`socketTimeout`**                           | `socketTimeout`设置读写操作的超时时间（毫秒）。              | 根据你的网络环境和数据库服务器的响应时间来设置。             |
| **`autoReconnect`** （注意：已不推荐使用）    | 在旧版本的 `MySQL` `Connector`/J中，此参数用于在连接丢失时尝试自动重新连接。但在新版本中，它已被废弃，建议使用连接池来处理连接的重连。 | 不要在新开发的应用中使用此参数。考虑使用连接池（如HikariCP、C3P0等）来管理数据库连 |

```http
jdbc:mysql://healerjean.com:3306/healerjean?createDatabaseIfNotExist=true&characterEncoding=utf-8&useUnicode=true&connectTimeout=3000&socketTimeout=5000&amp;serverTimezone=Asia/Shanghai&useSSL=false]
```



### 1）`tcp` 超时：`socketTimeout`

- **定义**：`socketTimeout` 是 `TCP` 层面的超时设置，它指的是客户端从服务器读取数据时的超时时间。在数据库连接的上下文中，这通常指的是  `JDBC` 客户端与 `MySQL`  服务器之间通过 `Socket` 传输数据时，等待服务器响应的超时时间。       

- **作用**：`socketTimeout` 用于防止在网络延迟或服务器无响应的情况下，客户端无限期地等待数据。如果设置了 `socketTimeout`，当超过这个时间后，客户端将抛出 `SocketTimeoutException`，从而允许应用程序采取相应的错误处理措施。     

- **设置方式**：在 `JDBC` 连接字符串中，可以通过添加 `socketTimeout` 参数来设置，例如：`jdbc:mysql://localhost:3306/mydatabase?socketTimeout=30000`（单位为毫秒）。





### 2）`SQL` 超时（ `statement timeout`）

**定义**：`SQL` 超时，通常通过 `statement timeout` 来实现，指的是单个 `SQL` 语句执行时的超时时间。这是数据库驱动层面或应用层面的设置，用于限制`SQL` 语句的执行时间，以防止长时间运行的查询耗尽系统资源。    

**作用**：`statement timeout` 用于保护数据库和应用免受长时间运行查询的影响，确保系统资源得到合理分配和利用。    

**设置方式**：在` JDBC` 中，可以通过调用 `Statement` 或 `PreparedStatement` 对象的 `setQueryTimeout(int seconds)` 方法来设置 `SQL` 超时时间（以秒为单位）。不过，也可以通过一些框架或连接池的配置来设置默认的 `SQL` 超时时间。

 

### 3）`tcp` 超时 和 `SQL` 超时 差异

- **级别差异**：`socketTimeout` 是 `TCP` 层面的超时，它作用于整个 `Socket` 连接上的数据传输；而 `SQL` 超时（`statement timeout`）是应用或数据库驱动层面的超时，它作用于单个 `SQL` 语句的执行。     

- **相互影响**：虽然 `socketTimeout` 和 `SQL` 超时作用于不同的层面，但它们之间存在一定的相互影响。如果 `SQL` 语句的执行时间超过了 `socketTimeout` 设置的时间，那么即使 `SQL` 语句本身没有超时（即没有超过 `statement timeout`），客户端也可能因为 `socketTimeout` 而抛出异常。因此，在设置这些超时时，需要确保 `socketTimeout` 的值大于或等于可能的最大 `SQL` 执行时间（包括 `statement timeout`）。     

- **配置建议**：为了避免死连接和长时间等待，建议同时设置 `socketTimeout` 和 `SQL` 超时（`statement timeout`）。`socketTimeout` 应该设置为足够长的时间，以容纳可能的网络延迟和服务器响应时间；而 `SQL` 超时则应该根据应用程序的实际需求和数据库的性能来设置。



# 三、连接数配置

> `MT`  (`70` 多台机器呢)

```java
  private int minPoolSize = 5;
  private int maxPoolSize = 20;
  private int initialPoolSize = 5;
  private int checkoutTimeout = 3000;
  private boolean removeAbandoned = true;
  private int timeBetweenEvictionRunsMillis = 600000;
  private int minEvictableIdleTimeMillis = 300000;
```



## 1、初始连接数和最小连接数

| 场景             | 推荐值  | 原因                                                         |
| ---------------- | ------- | ------------------------------------------------------------ |
| **单库架构**     | `1 ~ 2` | 应用启动后立即有少量可用连接，避免首次请求冷启动延迟；同时不浪费资源。 |
| **分库分表架构** | **`0`** | 每台应用只访问部分分片，若为每个分库都保活连接（如设为 2），会导致大量**空闲但真实的数据库连接**堆积，浪费内存并可能打满 `max_connections`。设为 0 可按需创建、用完回收，更安全高效。 |

### 1）**`minimumIdle=0` 在分库场景下是安全、高效、推荐的做法。**

- 它 **不会导致“每次请求都建连”**
- 只有 **长时间不用的分库** 才会释放连接
- 偶尔的建连开销（`5ms`）**远小于保活大量空闲连接的资源浪费和风险**



### 2）`minimumIdle=0` 如何避免“频繁建连”？

**方案 1：适当调大 `idleTimeout`**

```
idle-timeout: 1800000  # 30分钟（默认10分钟）
```

→ 让连接活得更久，减少重建概率。



**方案 2：对高频分库做“预热”（启动时主动访问）**

- 应用启动后，自动查一下自己负责的分片
- 触发连接创建，避免首次用户请求等待



**方案 3：监控 + 动态调整**

- 如果发现某个分库连接频繁重建（通过日志或指标）
- 说明它其实“不算冷”，可考虑设 `minimumIdle=1`（仅对该库）



## 2、最大连接数

| 推荐做法            | 说明                                                         |
| ------------------- | ------------------------------------------------------------ |
| **从 `10` 开始**    | 这是绝大多数 `Web` 应用的合理起点。`MySQL` 处理简单查询通常只需几毫秒，10 个连接可轻松支撑数百 QPS。 |
| **通过压测调整**    | 逐步增加并发压力，观察： - 应用是否出现“获取连接超时”或等待 - 数据库 `Threads_running` 是否持续 > 20 若无瓶颈，可小幅上调（如 12 → 15）；若 DB 已 CPU/I/O 打满，则调大连接池无效。 |
| **99% 的业务 ≤ 20** | 超过 20 往往意味着： - SQL 未优化（慢查询） - 事务过长 - 架构问题（如未读写分离） **优先优化 SQL 和架构，而不是堆连接数。** |



实战：**如果想继续调大数据库最大连接数，请先通过压测找到数据库服务器的最大负载。**

**内存规划：**`MySQL`官方博客建议每个连接按 `10M` 内存进行规划

- 限制：**连接池最大连接数 <= 数据库可供连接使用的最大内存 / 一个连接占用的内存大小 / 应用服务器数**
- 例如：某应用使用了规格为`16C` `64G` 的 `MySQL` 服务器，该应用有 `8` 台应用服务器，则从数据库稳定的角度考虑，数据库连接池最大连接数不能超过 `16*1024M` / `10M` / `8` = `200`

| MySQL规格 | 可供连接使用的最大内存（理论上） |
| --------- | -------------------------------- |
| 1C4G      | 2G                               |
| 2C8G      | 3G                               |
| 4C16G     | 5G                               |
| 8C32G     | 8G                               |
| 16C64G    | 16G                              |
| 32C128G   | 32G                              |





## 3、`FAQ`

### 1）错误：大量扩容和缩容时，不关注数据库连接数-

大量扩容和缩容时，都会极大的影响数据库连接数，从而影响到数据库的性能和稳定性，需要及时调整数据库连接池的配置。



### 2）错误：数据库连接数越大，数据库吞吐量越高

**错误：**`MySQL` 服务器允许同时连接的最大客户端数由 `max_connections` 系统变量决定，当达到此最大值时，服务器将不会接受新连接。因此，有一种观念认为只要不超 `max_connections`，数据库连接数越大，数据库性能越好，吞吐量越高。

**误区澄清：**连接数 ≠ 吞吐量。**连接数过多反而会导致性能下降，存在一个最优连接数区间，超过后系统吞吐不升反降**。

- **内存资源耗尽**：数据库连接数越大，对应的用户线程越多，占用的内存越大
- **上下文切换开销剧增**：每个连接对应一个 `OS` 线程，当活跃连接数远超 `CPU` 核心数时（如 `100` 个线程跑在 8 核 CPU 上），操作系统必须频繁进行线程上下文切换。
- **连接本身也有性能成本**：建立连接需要  `TCP` 握手 + 认证 + 初始化 THD → 耗时 ~1~10ms，频繁创建/销毁连接（如无连接池）会显著增加延迟。即使使用连接池，大量空闲连接也白白占用资源









# 四、多数据源配置

## 1、`dynamic-datasource-spring-boot-starter`

### 1）`pom`

```xml
<!--数据源-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>3.6.1</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.22</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.45</version>
</dependency>
```



### 2）`yml`

```yaml
####################################
### DB
####################################
spring:
  redis:
    host: 127.0.0.1
    port: 6379
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为master
      primary: healerjean
      # 设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: true
      druid:
        # 初始化时建立物理连接的个数
        initialSize: 5
        # 最小连接池数量
        minIdle: 5
        # 最大连接池数量
        maxActive: 20
        # 获取连接等待超时时间
        maxWait: 60000
        ## 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        ## 连接保持空闲而不被驱逐的最小时间
        minEvictableIdleTimeMillis: 300000
        # 用来检测连接是否有效的sql，要求是一个查询语句
        validationQuery: SELECT 1 FROM DUAL
        # 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        testWhileIdle: true
        # 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        testOnBorrow: false
        # 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        testOnReturn: false
        #打開PSCache，並指定每個連接上PSCache的大小。oracle設爲true，mysql設爲false。分庫分表較多推薦設置爲false
        pool-prepared-statements: false
        max-pool-prepared-statement-per-connection-size: 20
        stat:
          #日志输出执行慢的SQL
          log-slow-sql: true
          #slowSqlMillis的缺省值为3000，也就是3秒。
          slow-sql-millis: 2000
      datasource:
        healerjean:
          # 3.2.0开始支持SPI可省略此配置
          driver-class-name: com.mysql.jdbc.Driver
          url: jdbc:mysql://localhost:3306/healerjean?characterEncoding=utf-8&useUnicode=true&autoReconnect=true&connectTimeout=3000&initialTimeout=1&socketTimeout=5000&useSSL=false&serverTimezone=CTT
          username: root
          password: 12345678
        test:
          # 3.2.0开始支持SPI可省略此配置
          driver-class-name: com.mysql.jdbc.Driver
          url: jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true&connectTimeout=3000&initialTimeout=1&socketTimeout=5000&useSSL=false&serverTimezone=CTT
          username: root
          password: 12345678



```

### 3）样例 `@DS(DateSourceConstant.SLAVE_TEST)`

```java
@DS(DateSourceConstant.SLAVE_TEST)
@ApiOperation(value = "用户信息-单条查询", notes = "用户信息-单条查询-描述")
@RedisCache(RedisConstants.CacheEnum.COMMON)
@LogIndex
@GetMapping("user/{userId}")
@ResponseBody
public BaseRes<UserDemoVO> selectById(@ElParam @PathVariable("userId") Long userId) {
    UserDemoBO userDemoBo = userDemoService.selectById(userId);
    UserDemoVO userDemoVo = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
    return BaseRes.buildSuccess(userDemoVo);
}
```





## 2、`FAQ`

### 1）版本选择

|       维度        |    3.6.1     |         2.5.3          |    2.3.4    |
| :---------------: | :----------: | :--------------------: | :---------: |
|  上下文丢失问题   |   完全修复   |        完全修复        |    存在     |
| 内部调用 @DS 失效 | 内置兜底机制 | 部分兜底（需少量适配） |   无兜底    |
| 事务与数据源冲突  |   完全修复   |        基本修复        |  严重冲突   |
| Spring Boot 兼容  |  2.7.x/3.x   |      2.1.x-2.6.x       | 2.0.x-2.3.x |
|   官方维护状态    |   长期维护   |       仅紧急修复       |  停止维护   |



























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
