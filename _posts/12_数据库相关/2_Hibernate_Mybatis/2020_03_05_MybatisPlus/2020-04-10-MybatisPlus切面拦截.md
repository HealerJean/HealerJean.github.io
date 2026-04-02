---
title: MybatisPlus切面拦截
date: 2020-02-20 03:33:00
tags: 
- Database
category: 
- Database
description: MybatisPlus切面拦截
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            





为了保护用户隐私，我们需要对数据库用户关键数据，入库加密，取出来解密。为了我们系统自身的安全数据库连接用户名和密码都要加解密    



# 一、数据库连接加解密  

## 1、数据库连接配置  

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



#####################################
#### DB
####################################
spring.datasource.druid.url=jdbc:mysql://127.0.0.1:3306/hlj_demo?useUnicode=true&amp;characterEncoding=UTF-8&amp;autoReconnect=true
spring.datasource.druid.username=GCBeAUOZNANpmXfIUPO42qx/dQP80Lae3BI7ABxQN2AzWhgQAG+S6Dhe
spring.datasource.druid.password=GCAfE1p20be+BX5TZsVlFe1/T1bQ+f2IhnjqOQKe7CJT7xgQ8YOQrf7U
####################################



#是否需要数据连接加密
spring.datasource.encrypt=true
```



## 2、`Java` 数据库连接 

```java
package com.fintech.confin.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fintech.confin.sensitivity.KeycenterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author HealerJean
 * @ClassName DateSourceConfig
 * @date 2020/4/9  10:43.
 * @Description
 */
@Configuration
public class DateSourceConfig {
	
	
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
	@Value("${spring.datasource.encrypt}")
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



# 二、数据库字段加解密  

## 1、`KeyCenterUtils`：加解密工具类  

```java
package com.healerjean.proj.config.keycenter;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author HealerJean
 * @ClassName KeyCenterUtils
 * @date 2020/4/9 14:28
 * @Description 密钥中心工具类
 * 提供字符串加密和解密的功能
 * 当前实现基于Base64编码，可根据实际安全需求替换为更强的加密算法
 */
public class KeyCenterUtils {

    /**
     * 加密字符串
     * 将明文字符串转换为加密后的字符串
     *
     * @param plainText 待加密的明文字符串
     * @return 加密后的字符串
     * @throws RuntimeException 加密过程中发生异常时抛出
     */
    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        
        try {
            return Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("字符串加密失败", e);
        }
    }

    /**
     * 解密字符串
     * 将加密后的字符串转换为明文字符串
     *
     * @param encryptedText 待解密的字符串
     * @return 解密后的明文字符串
     * @throws RuntimeException 解密过程中发生异常时抛出
     */
    public static String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }
        
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("字符串解密失败", e);
        }
    }
}

```



## 2、`SecretTypeHandler`数据库字段加解密控制器  

```java
package com.healerjean.proj.config.keycenter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName SecretTypeHandler
 * @date 2020/4/9 14:27
 * @Description
 * 用于MyBatis ORM框架中自动处理数据的加密和解密操作
 * 在数据写入数据库前自动加密，从数据库读取后自动解密
 */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SecretTypeHandler extends BaseTypeHandler<String> {

    /**
     * 设置非空参数，将Java对象转换为JDBC类型
     * 在向数据库写入数据时，对非空字符串进行加密处理
     *
     * @param ps PreparedStatement对象
     * @param i 参数位置索引
     * @param parameter 参数值（待加密的字符串）
     * @param jdbcType JDBC类型
     * @throws SQLException SQL异常
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) 
      throws SQLException {
        // 只对非空字符串进行加密处理
        ps.setString(i, StringUtils.isNotBlank(parameter) ? KeyCenterUtils.encrypt(parameter) : parameter);
    }

    /**
     * 通过列名获取可为空的结果，并进行解密处理
     *
     * @param rs 结果集
     * @param columnName 列名
     * @return 解密后的字符串
     * @throws SQLException SQL异常
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decryptString(rs.getString(columnName));
    }

    /**
     * 通过列索引获取可为空的结果，并进行解密处理
     *
     * @param rs 结果集
     * @param columnIndex 列索引
     * @return 解密后的字符串
     * @throws SQLException SQL异常
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decryptString(rs.getString(columnIndex));
    }

    /**
     * 通过列索引获取存储过程调用的可为空的结果，并进行解密处理
     *
     * @param cs CallableStatement对象
     * @param columnIndex 列索引
     * @return 解密后的字符串
     * @throws SQLException SQL异常
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decryptString(cs.getString(columnIndex));
    }

    /**
     * 解密字符串工具方法
     * 只对非空字符串进行解密处理
     *
     * @param value 待解密的字符串
     * @return 解密后的字符串
     */
    private String decryptString(String value) {
        if (StringUtils.isNotBlank(value)) {
            return KeyCenterUtils.decrypt(value);
        }
        return value;
    }
}

```



## 3、`Handle` 的使用  

### 1）实体类 相关配置

```java
package com.healerjean.proj.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healerjean.proj.config.keycenter.SecretTypeHandler;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author HealerJean
 * @ClassName User
 * @date 2020/3/5  16:11.
 * @Description
 */
@Data
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;


    /**
     * 有了这个数据库 BaseMapper 插入的时候才能加密
     */
    @TableField(
            value = "`tel_phone`",
            typeHandler = SecretTypeHandler.class
    )
    private String telPhone;

    private String email;

    /**
     * 添加日期进行测试
     */
    private LocalDate createDate;

    private LocalDateTime createTime;


}

```



### 2）类 相关配置  

> 如果不是 `mybatisPlus` 的 `BaseMapper` 内部的方法，则需要我们自己放入我们自定义的 `typeHandler`

```java
@Results({
    @Result(column = "email", property = "email", typeHandler = SecretTypeHandler.class),
    @Result(column = "tel_phone", property = "telPhone", typeHandler = SecretTypeHandler.class)})
@Select("select * from user where id = #{id}")
List<User> selectDncryptList(Long id);
```



### 3）`xml` 中相关配置

```xml
<resultMap id="BaseResultMap" type="com.healerjean.proj.data.entity.User">
      <id column="ID" jdbcType="BIGINT" property="id" />
      <result column="name" jdbcType="VARCHAR" property="name" />
      <result column="age" jdbcType="INTEGER" property="age" />
      <result column="tel_phone" jdbcType="VARCHAR" property="telPhone" 
              typeHandler="com.healerjean.proj.config.keycenter.SecretTypeHandler" />
      <result column="email" jdbcType="VARCHAR" property="email" />
      <result column="create_date" jdbcType="DATE" property="createDate" />
      <result column="create_time" jdbcType="TIMESTAMP" property="createTime" />
</resultMap>

```



# 4、测试     

> **User中的数据都是正常的 。不是密文。因为我们只讲入库的数据设置了密文。并不会改变User对象本身**  

### 1）普通分页查询

```java

@Test
public void encrypt(){
    List<User> users = null ;

    //插入数据
    User user = new User();
    user.setName("name");
    user.setAge(12);
    user.setEmail("healerjean@gmail.com");
    user.setTelPhone("18841256");
    userMapper.insert(user);


    //更新
    user.setEmail("12456@gmail.com");
    userMapper.updateById(user);


    //查询 ：列表查询
    users = userMapper.selectList(null);
    System.out.println(users);

    //查询 ：根据Id查询
    User user1 = userMapper.selectById(user.getId());
    System.out.println(user1);


    //自定义sql查询
    users  = userMapper.selectDncryptList(user.getId());
    System.out.println(users);


}

```

### 2）精确查询

> 因为数据库中是密文，所以查询的时候，需要我们先加密后才能查

```java
// 根据敏感字段查询
Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
    .select(User::getEmail)
    .eq(User::getEmail, keyCenterUtils.encrypt("healerjean@gmail.com"));

users  = userMapper.selectList(userWrapper);
System.out.println(users);
```













## 5、进阶：敏感字段模糊查询-分词密文映射表

### 1）原理

> 主流的方法。新建一张分词密文映射表，在敏感字段数据新增、修改的后，对敏感字段进行分词组合，     
>
> 1、如“15503770537”的分词组合有“155”、“0377”、“0537”等，再对每个分词进行加密，建立起敏感字段的分词密文与目标数据行主键的关联关系；    
>
> 2、在处理模糊查询的时候，对模糊查询关键字进行加密，用加密后的模糊查询关键字，对分词密文映射表进行`like`查询，得到目标数据行的主键，再以目标数据行的主键为条件返回目标表进行精确查询。



### 2）敏感字段模糊查询方案

淘宝密文字段检索方案 ：https://open.taobao.com/docV3.htm?docId=106213&docType=1     

阿里巴巴文字段检索方案：https://jaq-doc.alibaba.com/docs/doc.htm?treeId=1&articleId=106213&docType=1    

拼多多密文字段检索方案：https://open.pinduoduo.com/application/document/browse?idStr=3407B605226E77F2    

京东密文字段检索方案：https://jos.jd.com/commondoc?listId=345



**问题1：为什么推荐这种方案？**

答案：这种方法的优点就是原理简单，实现起来也不复杂，但是有一定的局限性，算是一个对性能、业务相折中的一个方案，相比较之下，在能想的方法中，比较推荐这种方法，             



**问题2：分词太多，势必会对性能有影响，怎么解决呢？**   

答案：**对模糊查询的关键字的长度，要在业务层面进行限制**；以手机号为例，可以要求对模糊查询的关键字是四位或者是五位，具体可以再根据具体的场景进行详细划分。               



**问题3：为什么要增加这样的限制呢？**     

答案：因为明文加密后长度为变长，有额外的存储成本和查询性能成本，分词组合越多，需要的存储空间以及所消耗的查询性能成本也就更大，**并且分词越短，被硬破解的可能性也就越大，也会在一定程度上导致安全性降低**；



## 3）实现

#### a、分词密文映射表

```sql
create table if not exists sys_person_phone_encrypt
(
   id bigint auto_increment comment '主键' primary key,
   person_id int not null comment '关联人员信息表主键',
   phone_key varchar(500) not null comment '手机号码分词密文'
)
comment '人员的手机号码分词密文映射表';
```



#### b、`aop` 切面进行分词

> 敏感字段数据在保存入库的时候，对敏感字段进行分词组合并加密码，存储在分词密文映射表    
>
> 在注册人员信息的时候，先取出通过 `AOP` 进行加密过的手机号码进行解密；手机号码解密之后，对手机号码按照连续四位进行分词组合，并对每一个手机号码的分词进行加密，最后把所有的加密后手机号码分词拼接成一个字符串，与人员id一起保存到人员的手机号码分词密文映射表；

```java
public Person registe(Person person) {
    this.personDao.insert(person);
    String phone = this.decrypt(person.getPhoneNumber());
    String phoneKeywords = this.phoneKeywords(phone);
    this.personDao.insertPhoneKeyworkds(person.getId(),phoneKeywords);
    return person;
}

private String phoneKeywords(String phone) {
    String keywords = this.keywords(phone, 4);
    System.out.println(keywords.length());
    return keywords;
}
 

//分词组合加密
private String keywords(String word, int len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < word.length(); i++) {
        int start = i;
        int end = i + len;
        String sub1 = word.substring(start, end);
        sb.append(this.encrypt(sub1));
        if (end == word.length()) {
            break;
        }
    }
    return sb.toString();
}


public String encrypt(String val) {
    //这里特别注意一下，对称加密是根据密钥进行加密和解密的，加密和解密的密钥是相同的，一旦泄漏，就无秘密可言，
    //“fanfu-csdn”就是我自定义的密钥，这里仅作演示使用，实际业务中，这个密钥要以安全的方式存储；
    byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(), "fanfu-csdn".getBytes()).getEncoded();
    SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
    String encryptValue = aes.encryptBase64(val);
    return encryptValue;
}

public String decrypt(String val) {
    //这里特别注意一下，对称加密是根据密钥进行加密和解密的，加密和解密的密钥是相同的，一旦泄漏，就无秘密可言，
    //“fanfu-csdn”就是我自定义的密钥，这里仅作演示使用，实际业务中，这个密钥要以安全的方式存储；
    byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(), "fanfu-csdn".getBytes()).getEncoded();
    SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
    String encryptValue = aes.decryptStr(val);
    return encryptValue;
}
```



#### c、模糊查询

> 模糊查询的时候，对模糊查询关键字进行加密，以加密后的关键字密文为查询条件，查询密文映射表，得到目标数据行的id，再以目标数据行id为查询条件，查询目标数据表；      
>
> 根据手机号码的四位进行模糊查询的时候，以加密后模糊查询的关键字为条件，查询 `sys_person_phone_encrypt`表（人员的手机号码分词密文映射表），得到人员信息id；再以人员信息id，查询人员信息表；

```java
public List<Person> getPersonList(String phoneVal) {
    if (phoneVal != null) {
       return this.personDao.queryByPhoneEncrypt(this.encrypt(phoneVal));
    }
    return this.personDao.queryList(phoneVal);
}

<select id="queryByPhoneEncrypt" resultMap="personMap">
    select * from sys_person where id in 
    (select person_id from sys_person_phone_encrypt
     where phone_key like concat('%',#{phoneVal},'%'))
</select>
```





# 三、切面打印日志

```java
package com.healerjean.proj.config.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SqlLogInterceptor - MyBatis SQL 日志拦截器
 * 用于记录 SQL 执行的详细信息，包括 SQL 语句、参数、执行时间等
 *
 * @author zhangyujin
 * @date 2024/9/23
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlLogInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(SqlLogInterceptor.class);
    
    /**
     * SQL 执行时间阈值，超过该值将以 WARN 级别记录日志（单位：毫秒）
     */
    private long slowSqlThreshold = 1000;
    
    /**
     * 是否格式化 SQL
     */
    private boolean formatSql = false;
    
    /**
     * 是否打印参数
     */
    private boolean showParams = true;
    
    /**
     * 是否打印执行结果
     */
    private boolean showResult = true;
    
    /**
     * 最大 SQL 长度，超过该长度将被截断
     */
    private int maxSqlLength = 5000;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 如果日志级别不是 DEBUG，则直接执行原方法
        if (!log.isDebugEnabled()) {
            return invocation.proceed();
        }
        
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        
        // 执行 SQL
        Object result = null;
        try {
            // 执行真正的 SQL
            result = invocation.proceed();
            return result;
        } finally {
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            try {
                // 生成完整的 SQL 语句
                String completeSql = generateCompleteSql(configuration, boundSql, parameter);
                
                // 如果需要格式化 SQL
                if (formatSql) {
                    completeSql = formatSql(completeSql);
                }
                
                // 如果 SQL 过长，则截断
                if (completeSql.length() > maxSqlLength) {
                    completeSql = completeSql.substring(0, maxSqlLength) + "... [SQL too long, truncated]";
                }
                
                // 构建日志信息
                StringBuilder logMessage = new StringBuilder(256);
                logMessage.append("\n=========================== SQL执行详情 ===========================\n");
                logMessage.append("SQL ID: ").append(sqlId).append("\n");
                logMessage.append("执行时间: ").append(costTime).append("ms\n");
                logMessage.append("完整SQL: ").append(completeSql).append("\n");
                
                // 如果需要显示执行结果
                if (showResult && result != null) {
                    if (result instanceof Collection) {
                        logMessage.append("返回结果数: ").append(((Collection<?>) result).size()).append("\n");
                    } else if (result instanceof Map) {
                        logMessage.append("返回结果数: ").append(((Map<?, ?>) result).size()).append("\n");
                    } else if (result instanceof Integer) {
                        logMessage.append("影响行数: ").append(result).append("\n");
                    }
                }
                logMessage.append("==================================================================");
                
                // 根据执行时间决定日志级别
                if (costTime > slowSqlThreshold) {
                    log.warn(logMessage.toString());
                } else {
                    log.debug(logMessage.toString());
                }
            } catch (Exception e) {
                log.error("SQL日志记录异常", e);
            }
        }
    }

    /**
     * 生成完整的 SQL 语句（包含参数值）
     */
    private String generateCompleteSql(Configuration configuration, BoundSql boundSql, Object parameterObject) {
        // 如果不显示参数，则直接返回原始 SQL
        if (!showParams) {
            return boundSql.getSql().replaceAll("[\\s]+", " ");
        }
        
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        
        // 如果没有参数，直接返回 SQL
        if (CollectionUtils.isEmpty(parameterMappings) || parameterObject == null) {
            return sql;
        }
        
        // 使用 StringBuilder 提高性能
        StringBuilder sqlBuilder = new StringBuilder(sql);
        
        // 使用 TypeHandlerRegistry 处理参数
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        
        // 如果参数是基本类型
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            // 替换第一个问号
            replaceFirstPlaceholder(sqlBuilder, getParameterValue(parameterObject));
        } else {
            // 处理复杂参数
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            
            // 收集所有参数值
            List<String> paramValues = new ArrayList<>(parameterMappings.size());
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                Object value = null;
                
                // 从对象中获取属性值
                if (metaObject.hasGetter(propertyName)) {
                    value = metaObject.getValue(propertyName);
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    // 从附加参数中获取值
                    value = boundSql.getAdditionalParameter(propertyName);
                }
                
                paramValues.add(getParameterValue(value));
            }
            
            // 批量替换所有问号
            replacePlaceholders(sqlBuilder, paramValues);
        }
        
        return sqlBuilder.toString();
    }
    
    /**
     * 替换 SQL 中的第一个问号
     */
    private void replaceFirstPlaceholder(StringBuilder sql, String value) {
        int questionMarkIndex = sql.indexOf("?");
        if (questionMarkIndex != -1) {
            sql.replace(questionMarkIndex, questionMarkIndex + 1, value);
        }
    }
    
    /**
     * 批量替换 SQL 中的所有问号
     */
    private void replacePlaceholders(StringBuilder sql, List<String> values) {
        // 使用正则表达式查找所有问号
        Pattern pattern = Pattern.compile("\\?");
        Matcher matcher = pattern.matcher(sql);
        
        int valueIndex = 0;
        int offset = 0;
        
        // 逐个替换问号
        while (matcher.find() && valueIndex < values.size()) {
            int start = matcher.start() + offset;
            int end = matcher.end() + offset;
            String value = values.get(valueIndex++);
            
            // 替换问号为实际值
            sql.replace(start, end, value);
            
            // 更新偏移量
            offset += value.length() - 1;
            
            // 重新创建匹配器
            matcher = pattern.matcher(sql);
            matcher.region(start + value.length(), sql.length());
        }
    }
    
    /**
     * 获取参数的字符串表示
     */
    private String getParameterValue(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        
        if (obj instanceof String) {
            return "'" + escapeString((String) obj) + "'";
        } else if (obj instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + dateFormat.format((Date) obj) + "'";
        } else if (obj instanceof Boolean || obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            Collection<?> collection = (Collection<?>) obj;
            int i = 0;
            for (Object item : collection) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(getParameterValue(item));
                i++;
            }
            sb.append(")");
            return sb.toString();
        } else if (obj.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            Object[] array = (Object[]) obj;
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(getParameterValue(array[i]));
            }
            sb.append(")");
            return sb.toString();
        } else {
            return "'" + escapeString(obj.toString()) + "'";
        }
    }
    
    /**
     * 转义字符串中的特殊字符
     */
    private String escapeString(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.replace("'", "''");
    }
    
    /**
     * 格式化 SQL 语句
     */
    private String formatSql(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return "";
        }
        
        // 简单的 SQL 格式化
        sql = sql.replaceAll("(?i)SELECT", "\nSELECT")
                .replaceAll("(?i)FROM", "\nFROM")
                .replaceAll("(?i)WHERE", "\nWHERE")
                .replaceAll("(?i)AND", "\n  AND")
                .replaceAll("(?i)OR", "\n   OR")
                .replaceAll("(?i)GROUP BY", "\nGROUP BY")
                .replaceAll("(?i)HAVING", "\nHAVING")
                .replaceAll("(?i)ORDER BY", "\nORDER BY")
                .replaceAll("(?i)LIMIT", "\nLIMIT")
                .replaceAll("(?i)OFFSET", "\nOFFSET")
                .replaceAll("(?i)UPDATE", "\nUPDATE")
                .replaceAll("(?i)SET", "\nSET")
                .replaceAll("(?i)INSERT INTO", "\nINSERT INTO")
                .replaceAll("(?i)VALUES", "\nVALUES")
                .replaceAll("(?i)DELETE FROM", "\nDELETE FROM");
        
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 从配置中读取属性
        if (properties != null) {
            String threshold = properties.getProperty("slowSqlThreshold");
            if (threshold != null && !threshold.isEmpty()) {
                try {
                    this.slowSqlThreshold = Long.parseLong(threshold);
                } catch (NumberFormatException e) {
                    log.warn("Invalid slowSqlThreshold value: {}", threshold);
                }
            }
            
            String formatSql = properties.getProperty("formatSql");
            if (formatSql != null && !formatSql.isEmpty()) {
                this.formatSql = Boolean.parseBoolean(formatSql);
            }
            
            String showParams = properties.getProperty("showParams");
            if (showParams != null && !showParams.isEmpty()) {
                this.showParams = Boolean.parseBoolean(showParams);
            }
            
            String showResult = properties.getProperty("showResult");
            if (showResult != null && !showResult.isEmpty()) {
                this.showResult = Boolean.parseBoolean(showResult);
            }
            
            String maxSqlLength = properties.getProperty("maxSqlLength");
            if (maxSqlLength != null && !maxSqlLength.isEmpty()) {
                try {
                    this.maxSqlLength = Integer.parseInt(maxSqlLength);
                } catch (NumberFormatException e) {
                    log.warn("Invalid maxSqlLength value: {}", maxSqlLength);
                }
            }
        }
    }
    
    // Getter and Setter methods for configuration properties
    public long getSlowSqlThreshold() {
        return slowSqlThreshold;
    }
    
    public void setSlowSqlThreshold(long slowSqlThreshold) {
        this.slowSqlThreshold = slowSqlThreshold;
    }
    
    public boolean isFormatSql() {
        return formatSql;
    }
    
    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }
    
    public boolean isShowParams() {
        return showParams;
    }
    
    public void setShowParams(boolean showParams) {
        this.showParams = showParams;
    }
    
    public boolean isShowResult() {
        return showResult;
    }
    
    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }
    
    public int getMaxSqlLength() {
        return maxSqlLength;
    }
    
    public void setMaxSqlLength(int maxSqlLength) {
        this.maxSqlLength = maxSqlLength;
    }
}
```



```java
=========================== SQL执行详情 ===========================
SQL ID: com.healerjean.proj.data.mapper.UserDemoMapper.selectById
执行时间: 66ms
完整SQL: SELECT id,name,age,phone,email,valid_flag,start_time,end_time,create_time,update_time FROM user_demo WHERE id=1 
返回结果数: 1
==================================================================
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
		id: 'dhHAyQOmr62scqYG',
    });
    gitalk.render('gitalk-container');
</script> 
