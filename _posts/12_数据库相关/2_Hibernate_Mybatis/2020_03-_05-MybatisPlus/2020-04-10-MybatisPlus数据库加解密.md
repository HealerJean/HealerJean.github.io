---
title: MybatisPlus数据库加解密
date: 2020-02-20 03:33:00
tags: 
- Database
category: 
- Database
description: MybatisPlus数据库加解密
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            





为了保护用户隐私，我们需要对数据库用户关键数据，入库加密，取出来解密。为了我们系统自身的安全数据库连接用户名和密码都要加解密    



# 1、数据库连接加解密  

## 1.1、数据库连接配置  

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



## 1.2、`Java` 数据库连接 

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



# 2、数据库字段加解密  

## 2.1、`KeyCenterUtils`：加解密工具类  

```java
package com.healerjean.proj.config.keycenter.one;

import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * KeyCenterUtils
 * @author zhangyujin
 * @date 2023/6/15  13:48
 */
@Service
public class KeyCenterUtils {

    /**
     * 自己写加密逻辑
     */
    public  String encrypt(String src) {
        try {
            return Base64.getEncoder().encodeToString(src.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 自己写解密逻辑
     */
    public  String decrypt(String src) {
        try {
            byte[] asBytes = Base64.getDecoder().decode(src);
            return new String(asBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

}

```



## 2.2、`SecretTypeHandler`数据库字段加解密控制器  

```java
package com.healerjean.proj.utils.db;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CustomTypeHandler
 * @author zhangyujin
 * @date 2023/6/15  13:48
 */
@Service
public class SecretTypeHandler<T> extends BaseTypeHandler<T> {

    /**
     * keyCenterUtils
     */
    @Autowired
    private KeyCenterUtils keyCenterUtils;

    /**
     * CustomTypeHandler
     */
    public SecretTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.keyCenterUtils.encrypt((String)parameter));
    }
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        //有一些可能是空字符
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        ;
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }
}


```



## 2.3、`Handle` 的使用  



### 2.3.1、数据层实体类注解  

```java
package com.healerjean.proj.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healerjean.proj.config.keycenter.one.SecretTypeHandler;
import lombok.Data;

import java.util.Date;

@Data
@TableName(autoResultMap = true) //有了这个BaseMapper查询的结果才能解密
public class User {
    private Long id;
    private String name;
    private Integer age;

    //有了这个数据库BaseMapper插入的时候才能加密
    @TableField(typeHandler = SecretTypeHandler.class)
    private String telPhone;

    @TableField(typeHandler = SecretTypeHandler.class)
    private String email;

    private Date createDate;
    private Date createTime;
}

```



### 2.3.2、自定义 `sql`查询的配置  

> 如果不是 `mybatisPlus` 的 `BaseMapper` 内部的方法，则需要我们自己放入我们自定义的 `typeHandler`

```java
@Results({
    @Result(column = "email", property = "email", typeHandler = SecretTypeHandler.class),
    @Result(column = "tel_phone", property = "telPhone", typeHandler = SecretTypeHandler.class)})
@Select("select * from user where id = #{id}")
List<User> selectDncryptList(Long id);
```

### 2.3.3、测试     



> **User中的数据都是正常的 。不是密文。因为我们只讲入库的数据设置了密文。并不会改变User对象本身**  



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



## 2.4、敏感字段查询：（需要精确查询） 

> 因为数据库中是密文，所以查询的时候，需要我们先加密后才能查

```java
// 根据敏感字段查询
Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
    .select(User::getEmail)
    .eq(User::getEmail, keyCenterUtils.encrypt("healerjean@gmail.com"));

users  = userMapper.selectList(userWrapper);
System.out.println(users);
```



# 3、敏感字段模糊查询-分词密文映射表

## 3.1、原理

> 主流的方法。新建一张分词密文映射表，在敏感字段数据新增、修改的后，对敏感字段进行分词组合，     
>
> 1、如“15503770537”的分词组合有“155”、“0377”、“0537”等，再对每个分词进行加密，建立起敏感字段的分词密文与目标数据行主键的关联关系；    
>
> 2、在处理模糊查询的时候，对模糊查询关键字进行加密，用加密后的模糊查询关键字，对分词密文映射表进行`like`查询，得到目标数据行的主键，再以目标数据行的主键为条件返回目标表进行精确查询。



## 3.2、敏感字段模糊查询方案

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



## 3.3、实现

### 3.3.1、分词密文映射表

```sql
create table if not exists sys_person_phone_encrypt
(
   id bigint auto_increment comment '主键' primary key,
   person_id int not null comment '关联人员信息表主键',
   phone_key varchar(500) not null comment '手机号码分词密文'
)
comment '人员的手机号码分词密文映射表';
```



### 3.3.2、`aop` 切面进行分词

> 敏感字段数据在保存入库的时候，对敏感字段进行分词组合并加密码，存储在分词密文映射表    
>
> 在注册人员信息的时候，先取出通过 `AOP` 进行加密过的手机号码进行解密；手机号码解密之后，对手机号码按照连续四位进行分词组合，并对每一个手机号码的分词进行加密，最后把所有的加密后手机号码分词拼接成一个字符串，与人员id一起保存到人员的手机号码分词密文映射表；



```java
ublic Person registe(Person person) {
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



### 3.3.3、模糊查询

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
