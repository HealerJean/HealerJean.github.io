---
title: SpringBoot整合Mybatis-Plus
date: 2018-03-05 03:33:00
tags: 
- Database
category: 
- Database
description: SpringBoot整合Mybatis-Plus
---

<!-- __

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->



**前言**
[博主github](https://github.com/HealerJean)    
[博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



# 1、SpringBoot整合Mybatis-Plus



## 1.1、依赖  

### 1.1.1、`mybatis-plus`依赖

```xml
<!--mybatis-plus-->
<mybatis-plus-boot-starter.version>3.3.1.tmp</mybatis-plus-boot-starter.version>

<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus-boot-starter.version}</version>
</dependency>

```



### 1.1.2、我的项目依赖 

#### 1.1.2.1、主项目 

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.healerjean.proj</groupId>
    <artifactId>hlj-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>hlj-parent</name>
    <description>Demo project for Spring Boot</description>
    <packaging>pom</packaging>

    <properties>
        <java.version>1.8</java.version>
        <lombok.version>1.18.4</lombok.version>
        <!--swagger 版本-->
        <swagger.version>2.7.0</swagger.version>
        <!--数据源-->
        <com-alibaba-druid.version>1.1.9</com-alibaba-druid.version>
        <!--mybatis plus-->
        <mybatis-plus-boot-starter.version>3.3.1.tmp</mybatis-plus-boot-starter.version>
    </properties>

    <modules>
        <module>hlj-client</module>
    </modules>

    <dependencyManagement>
        <dependencies>

            <!--lombok-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>

            <!--swagger-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>${swagger.version}</version>
            </dependency>


            <!--数据源-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>${com-alibaba-druid.version}</version>
            </dependency>
            <!--mybatis-plus-->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis-plus-boot-starter.version}</version>
            </dependency>

        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```



#### 1.1.2.2、子工程 

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.healerjean.proj</groupId>
        <artifactId>hlj-parent</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <groupId>com.healerjean.proj</groupId>
    <artifactId>hlj-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>hlj-client</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--web-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.9.RELEASE</version>
            <scope>text</scope>
        </dependency>


        <!-- Jackson jsonUtils-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>jackson-annotations</artifactId>
                    <groupId>com.fasterxml.jackson.core</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-parameter-names</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jdk8</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
        </dependency>


        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
        </dependency>

        <!-- 数据源 -->

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>

        <!--StringUtils-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```



## 1.2、配置文件  



### 1.2.1、`application.properties`

```properties
spring.application.name=hlj-mybatis-plus
spring.profiles.active=local


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

#mybatis-plus.config-location=classpath:mapper/*.xml
mybatis-plus.type-aliases-package=com.healerjean.proj.data.entity
##主键类型  0:"数据库ID自增，非常大", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
mybatis-plus.id-type: 0
 #字段策略 0:"忽略判断",1:"非 NULL 判断"),2:"非空判断"
mybatis-plus.field-strategy: 2
 #数据库大写下划线转换
mybatis-plus.capital-mode: true
mybatis-plus.refresh-mapper: true

```

### 1.2.2、`application-local.properties`

```properties
server.port=8888


####################################
### DB
####################################
spring.datasource.druid.url=jdbc:mysql://localhost:3306/hlj_mybatis_plus?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.druid.username=root
spring.datasource.druid.password=123456
####################################

```



## 1.3、数据库语句

```java
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



## 1.4、Mapper扫描配置

> 两种方式，一种是每个mapper上使用注解@Mapper  
>
> 另一种如下 ，全局配置



```java
@Configuration
@MapperScan("com.healerjean.proj.data.mapper")
public class MybatisPlusConfig {

}
```



## 1.4、`User`实体类 

```java
package com.healerjean.proj.data.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}

```



## 1.5、`Mapper`类

```java
package com.healerjean.proj.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.data.entity.User;
public interface UserMapper  extends BaseMapper<User> {


}

```



## 1.6、测试 

```java
@RestController
@RequestMapping("hlj/user")
@Api(description = "用户管理")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping(value = "selectById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean selectById(UserDTO userDTO) {
        User user = userMapper.selectById(userDTO.getId());
        log.info("用户管理--------selectById：【{}】", JsonUtils.toJsonString(user));
        return ResponseBean.buildSuccess(user);
    }
    
}
```



**测试结果：**

```json
http://127.0.0.1:8888/hlj/user/selectById?id=1235553744515612673

{
  "success": true,
  "result": {
    "id": 1235553744515612700,
    "name": "healer",
    "age": 22,
    "email": "22"
  },
  "msg": "",
  "code": 200,
  "date": "1583469689318"
}
```





# 2、Wrapper方法的的使用 

## 2.1、`eq`：等于 =

```java
eq(R column, Object val)
eq(boolean condition, R column, Object val)
```

```java
Wrapper<User> userWrapper = new QueryWrapper<User>().lambda()
    .eq(User::getName, userDTO.getName());
List<User>  users = userMapper.selectList(userWrapper);




//只有一个条件，如果并且不成立则查询书所有的数据
userWrapper = new QueryWrapper<User>().lambda().
    eq(StringUtils.isNotBlank(userDTO.getName()), User::getName, userDTO.getName());
List<User>  users = userMapper.selectList(userWrapper);
```



## 2.2、`ne`：不等于 <>

```java
ne(R column, Object val)
ne(boolean condition, R column, Object val)
    
例: ne("name", "老王")--->name <> '老王'
```



## 2.3、`gt`：大于 >

```java
gt(R column, Object val)
gt(boolean condition, R column, Object val)
    
例: gt("age", 18)--->age > 18
```





## 2.4、`ge`：大于等于 >=

```java
lt(R column, Object val)
lt(boolean condition, R column, Object val)


ge("age", 18)--->age >= 18
```

```java

```


## 2.5、`lt`：小于 <

```java
lt(R column, Object val)
lt(boolean condition, R column, Object val)

    
例: lt("age", 18)--->age < 18
```



## 2.6、`le`：小于等于 <=

```java
le(R column, Object val)
le(boolean condition, R column, Object val)
    
例: le("age", 18)--->age <= 18
```



## 2.7、`between`：ETWEEN 值1 AND 值2

```java
between(R column, Object val1, Object val2)
between(boolean condition, R column, Object val1, Object val2)
    
例: between("age", 18, 30)--->age between 18 and 30
```



## 2.8、`notBetween`：NOT BETWEEN 值1 AND 值2

```java
notBetween(R column, Object val1, Object val2)
notBetween(boolean condition, R column, Object val1, Object val2)
    
例: notBetween("age", 18, 30)--->age not between 18 and 30
```



## 2.9、`like`：LIKE '%值%'

```java
like(R column, Object val)
like(boolean condition, R column, Object val)
    
例: like("name", "王")--->name like '%王%'
```


## 2.10、`notLike`：NOT LIKE '%值%'

```java
notLike(R column, Object val)
notLike(boolean condition, R column, Object val)
    
例: notLike("name", "王")--->name not like '%王%'
```



## 2.11、`likeLeft`：LIKE '%值'

```java
likeLeft(R column, Object val)
likeLeft(boolean condition, R column, Object val)
    
例: likeLeft("name", "王")--->name like '%王'
```



## 2.12、`likeRight`：LIKE '值%'

```java
likeRight(R column, Object val)
likeRight(boolean condition, R column, Object val)
    
例: likeRight("name", "王")--->name like '王%'
```



## 2.13、`isNull`：字段 IS NULL

```java
isNull(R column)
isNull(boolean condition, R column)
    
例: isNull("name")--->name is null
```


## 2.14、`isNotNull`：字段 IS NOT NULL

```java
isNotNull(R column)
isNotNull(boolean condition, R column)
    
例: isNotNull("name")--->name is not null    
```



## 2.15、`in`：字段 IN

```java
in(R column, Collection<?> value)
in(boolean condition, R column, Collection<?> value)
    
例: in("age",{1,2,3})--->age in (1,2,3)    
```
```java
in(R column, Object... values)
in(boolean condition, R column, Object... values)

例: in("age", 1, 2, 3)--->age in (1,2,3)
```



**测试** 

```java
@Test
public void in() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    userWrapper = Wrappers.<User>lambdaQuery().in(User::getAge, 20, 18);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));

    List<Integer> integers = new ArrayList<>();
    integers.add(18);
    integers.add(20);
    userWrapper = Wrappers.<User>lambdaQuery().in(User::getAge, integers);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}

```



## 2.16、`notIn`：字段 IN (value.get(0), value.get(1), ...)

```java
notIn(R column, Collection<?> value)
notIn(boolean condition, R column, Collection<?> value)
    
例: notIn("age",{1,2,3})--->age not in (1,2,3)
```





```java
notIn(R column, Object... values)
notIn(boolean condition, R column, Object... values)

例: notIn("age", 1, 2, 3)--->age not in (1,2,3)
```



## 2.17、`inSql`：字段 IN ( sql语句 )

```java
inSql(R column, String inValue)
inSql(boolean condition, R column, String inValue)
    
例: inSql("id", "select id from table where id < 3")--->id in (select id from table where id < 3)
```



**测试** 

```java
@Test
public void inSql() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    userWrapper = Wrappers.<User>lambdaQuery()
        .inSql(User::getAge, "select age from user where name = 'healer' ");
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}
```





## 2.18、`notInSql`：字段 NOT IN ( sql语句 )

```java
notInSql(R column, String inValue)
notInSql(boolean condition, R column, String inValue)
    

例: notInSql("id", "select id from table where id < 3")
    
    --->age not in (select id from table where id < 3)
```





## 2.19、`groupBy`：分组：GROUP BY 字段, .

```java
groupBy(R... columns)
groupBy(boolean condition, R... columns)
    
例: groupBy("id", "name")
    
    --->group by id,name
```

**测试**

```java
@Test
public void groupBy() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    //这样会报错，肯定的啊，我们是分组
    // userWrapper = Wrappers.<User>lambdaQuery().groupBy(User::getAge);
    userWrapper = Wrappers.<User>lambdaQuery().select(User::getAge).groupBy(User::getAge).having("age = {0}", 18);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}
```





## 2.20、`orderBy/orderByAsc`：ORDER BY 字段, ... ASC


```java
orderBy(boolean condition, boolean isAsc, R... columns)
```

```java
orderByAsc(R... columns)
orderByAsc(boolean condition, R... columns)

例: orderByAsc("id", "name")
    
    --->order by id ASC,name ASC
```




## 2.21、`orderByDesc`：排序：ORDER BY 字段, ... DESC

```java
orderByDesc(R... columns)
orderByDesc(boolean condition, R... columns)
   
例: orderByDesc("id", "name")--->order by id DESC,name DESC
```





## 2.22、`having`：HAVING ( sql语句 )

```java
having(String sqlHaving, Object... params)
having(boolean condition, String sqlHaving, Object... params)


例: having("sum(age) > {0}", 11)--->having sum(age) > 1
```

**测试**

```java
@Test
public void groupBy() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    //这样会报错，肯定的啊，我们是分组
    // userWrapper = Wrappers.<User>lambdaQuery().groupBy(User::getAge);
    userWrapper = Wrappers.<User>lambdaQuery().select(User::getAge).groupBy(User::getAge).having("age = {0}", 18);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}
```



## 2.23、`or`：拼接 OR 

> 注意事项：主动调用`or`表示紧接着下一个**方法**不是用`and`连接!(不调用`or`则默认为使用`and`连接)

```java
or()
or(boolean condition)

例: `eq("id",1).or().eq("name","老王")`--->`id = 1 or name = '老王'`
```



### 2.23.1、OR嵌套 

```java
例: or(i -> i.eq("name", "李白")
      .ne("status", "活着"))
    
--->or (name = '李白' and status <> '活着')
```



```java
@Test
public void or() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    userWrapper = Wrappers.<User>lambdaQuery()
        .and(userLambdaQueryWrapper -> userLambdaQueryWrapper
             .eq(User::getAge, 20)
             .eq(User::getAge, 20))
        .or().eq(User::getName, "healer");
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}


SELECT id,name,age,email FROM user WHERE ((age = ? AND age = ?) OR name = ?) 
```



## 2.24、`and`：默认为and

```java
and(Consumer<Param> consumer)
and(boolean condition, Consumer<Param> consumer)

```

### 2.24.1、AND 嵌套

```java
例:  and(i -> i.eq("name", "李白")
        .ne("status", "活着"))
    
 --->and (name = '李白' and status <> '活着')
```





## 2.25、`nested`：正常嵌套 不带 AND 或者 OR

> 默认都是自动加and

```java
nested(Consumer<Param> consumer)
nested(boolean condition, Consumer<Param> consumer)



例: nested(i -> i.eq("name", "李白").ne("status", "活着"))
    
     --->(name = '李白' and status <> '活着')
```



```java
@Test
    public void or() {
        Wrapper<User> userWrapper = null;
        List<User> users = null;
        userWrapper = Wrappers.<User>lambdaQuery()
                .or().eq(User::getName, "healer")
                .nested(wapper -> wapper.eq(User::getAge, 21 ).eq(User::getAge, 32));
        users = userMapper.selectList(userWrapper);
        System.out.println(JsonUtils.toJsonString(users));
    }



SELECT id,name,age,email FROM user WHERE
((age = ? AND age = ?) OR name = ? AND (age = ? AND age = ?)) 
```





## 2.26、`apply`：拼接 sql 

> 该方法可用于数据库**函数** 动态入参的`params`对应前面`applySql`内部的`{index}`部分.这样是不会有sql注入风险的,反之会有!

```java
apply(String applySql, Object... params)
apply(boolean condition, String applySql, Object... params)
    
    
    
例: apply("id = 1")--->id = 1
例: apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")
    --->date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")
例: apply("date_format(dateColumn,'%Y-%m-%d') = {0}", "2008-08-08")
    --->date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")
    
```



**测试**

```java
@Test
public void apply() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    userWrapper = Wrappers.<User>lambdaQuery().eq(User::getAge, 22).apply("name =  'healer'");
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}



SELECT id,name,age,email FROM user WHERE (age = ? AND name = 'healer')
```





## 2.27、`last`：无视优化规则直接拼接到 sql 的最后

> 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
>
>   



```java
last(String lastSql)
last(boolean condition, String lastSql)
    
    
例: last("limit 1")  
```



## 2.28、`exists`：拼接 EXISTS ( sql语句 )

```java
exists(String existsSql)
exists(boolean condition, String existsSql)
    
例: exists("select id from table where age = 1")
    
    --->exists (select id from table where age = 1)
```



## 2.29、`notExists`：拼接 NOT EXISTS ( sql语句 )

```java
notExists(String notExistsSql)
notExists(boolean condition, String notExistsSql)
    
    
例: notExists("select id from table where age = 1")
    --->not exists (select id from table where age = 1)
```



# 3、`QueryWrapper`

> 继承自 AbstractWrapper ,自身的内部属性 entity 也用于生成 where 条件
> 及` LambdaQueryWrapper`, 可以通过 `new QueryWrapper().lambda()` 方法获取 

```
mysqlMapper.getAll(Wrappers.<MysqlData>lambdaQuery().eq(MysqlData::getGroup, 1));
```



## 3.1、`select`：设置查询字段 

> 分法为两类.第二类方法为:过滤查询字段(主键除外),入参不包含 class 的调用前需要`wrapper`内的`entity`属性有值! 这两类方法重复调用以最后一次为准

```java
select(String... sqlSelect)
select(Predicate<TableFieldInfo> predicate)
select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)
    
    
select("id", "name", "age")
select(i -> i.getProperty().startsWith("test"))
```





# 4、`UpdateWrapper`

> 继承自 `AbstractWrapper` ,自身的内部属性 `entity` 也用于生成 where 条件
> 及 `LambdaUpdateWrapper`, 可以通过 `new UpdateWrapper().lambda()` 方法获取!



## 4.1、`set`：SQL SET 字段



```java
set(String column, Object val)
set(boolean condition, String column, Object val)
    


例: set("name", "老李头")
例: set("name", "")--->数据库字段值变为空字符串
例: set("name", null)--->数据库字段值变为null
```



## 4.2、`setSql`：设置 SET 部分 SQL

```java
setSql(String sql)

例: setSql("name = '老李头'")
```



# 5、使用 Wrapper 自定义SQL



```java
/**
* 1、构造LambdaQueryWrapper 的方式
*/
@Test
public void lambdaQuery() {
    Wrapper<User> userWrapper = null;
    List<User> users = null;
    String name = "healer";
    userWrapper = Wrappers.<User>lambdaQuery().eq(User::getName, name);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));

    userWrapper = Wrappers.lambdaQuery(User.class).eq(User::getName, name);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));

    userWrapper = new QueryWrapper<User>().lambda().eq(User::getName, name);
    users = userMapper.selectList(userWrapper);
    System.out.println(JsonUtils.toJsonString(users));
}
```



##  方案一： 注解方式 `Mapper.java`

```java
@Select("select * from mysql_data ${ew.customSqlSegment}")
List<MysqlData> getAll(@Param(Constants.WRAPPER) Wrapper wrapper);
```



## 方案二 ：XML形式 `Mapper.xml`

```xml
<select id="getAll" resultType="MysqlData">
	SELECT * FROM mysql_data ${ew.customSqlSegment}
</select>
```






















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
		id: 'yCRH6FSZfMt4E7Ki',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->

