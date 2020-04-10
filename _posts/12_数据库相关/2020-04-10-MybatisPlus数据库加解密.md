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



## 1.2、Java数据库连接 

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
 * @author HealerJean
 * @ClassName AES
 * @date 2020/4/9  14:28.
 * @Description
 */
@Service
public class KeyCenterUtils {

    /**
     * 自己写加密逻辑
     */
    public  String encrypt(String src) {
        try {
            String result = Base64.getEncoder().encodeToString(src.getBytes("UTF-8"));
            return result;
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
            String result = new String(asBytes, "UTF-8");
            return result;
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

}

```



## 2.2、`CustomTypeHandler`数据库字段加解密控制器  

```java
package com.healerjean.proj.config.keycenter.one;

/**
 * @author HealerJean
 * @ClassName AESTypeHandler
 * @date 2020/4/9  14:27.
 * @Description
 */

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomTypeHandler<T> extends BaseTypeHandler<T> {

    @Autowired
    private KeyCenterUtils keyCenterUtils;

    public CustomTypeHandler() {
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
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }
}


```



## 2.3、Handle的使用  



### 2.3.1、数据层实体类注解  

```java
package com.healerjean.proj.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healerjean.proj.config.keycenter.one.CustomTypeHandler;
import lombok.Data;

import java.util.Date;

@Data
@TableName(autoResultMap = true) //有了这个BaseMapper查询的结果才能解密
public class User {
    private Long id;
    private String name;
    private Integer age;

    //有了这个数据库BaseMapper插入的时候才能加密
    @TableField(typeHandler = CustomTypeHandler.class)
    private String telPhone;

    @TableField(typeHandler = CustomTypeHandler.class)
    private String email;

    private Date createDate;
    private Date createTime;
}

```



### 2.3.2、自定义sql查询的配置  

> 如果不是mybatisPlus的 BaseMapper内部的方法，则需要我们自己放入我们自定义的`typeHandler`

```java
@Results({
    @Result(column = "email", property = "email", typeHandler = CustomTypeHandler.class),
    @Result(column = "tel_phone", property = "telPhone", typeHandler = CustomTypeHandler.class)})
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














  **<font  color="red">感兴趣的，欢迎添加博主微信 </font>**       

​    

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
		id: 'dhHAyQOmr62scqYG',
    });
    gitalk.render('gitalk-container');
</script> 

