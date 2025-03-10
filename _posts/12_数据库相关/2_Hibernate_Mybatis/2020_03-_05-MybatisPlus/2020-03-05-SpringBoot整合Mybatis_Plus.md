---
title: SpringBoot整合Mybatis-Plus
date: 2020-03-05 03:33:00
tags: 
- Database
category: 
- Database
description: SpringBoot整合Mybatis-Plus
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           





# 一、依赖  

## 1、`mybatis-plus`依赖

```xml
<!--mybatis-plus-->
<mybatis-plus-boot-starter.version>3.4.1</mybatis-plus-boot-starter.version>

<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus-boot-starter.version}</version>
</dependency>

```



## 2、我的项目依赖 

### 1）主项目 

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
        <!--数据源 注意提高下面的版本，否则不支持LocalDate-->
        <com-alibaba-druid.version>1.1.9</com-alibaba-druid.version>
        <!--mybatis plus-->
        <mybatis-plus-boot-starter.version>3.4.1</mybatis-plus-boot-starter.version>
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
                <artifactId>druid-spring-boot-starter</artifactId>
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
            <artifactId>druid-spring-boot-starter</artifactId>
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



## 3、配置文件  

### 1）、`application.properties`

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

# 本大爷啥也没配
# 配置 mybatis的一些配置，也可以在 application.properties 中配置，如果配置了就不需要了mybatis.xml
#mybatis-plus.config-location=classpath:mybatis.xml
#Maven 多模块项目的扫描路径需以 classpath*: 开头 （即加载多个 jar 包下的 XML 文件）
#mybatis-plus.mapper-locations=classpath*:mapper/*.xml
mybatis-plus.type-aliases-package=com.healerjean.proj.data.entity
##主键类型  0:"数据库ID自增，非常大", 1:"用户输入ID（如果用户不输入，则默认是0）",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
mybatis-plus.id-type: 0
/** mybatis-plus如果希望使用数据库自增 */
// @TableId(value = "id", type = IdType.AUTO)
//private Long id;
    
 #字段策略 0:"忽略判断",1:"非 NULL 判断"),2:"非空判断" （默认2）
mybatis-plus.field-strategy: 2
 #数据库大写下划线转换
mybatis-plus.capital-mode: true
mybatis-plus.refresh-mapper: true

```



### 2）`application-local.properties`

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

### 3）`MybatisPlusConfiguration`

```java

/**
 * mybatisPlusInterceptor
 *
 * @author zhangyujin
 * @date 2023/6/15  11:54.
 */
@Slf4j
@MapperScan("com.healerjean.proj.data.mapper")
@Configuration
public class MybatisPlusConfiguration {
    /**
     * MyBatis支持
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        long t1 = System.currentTimeMillis();
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        long t2 = System.currentTimeMillis();
        log.info("MybatisPlusInterceptor injected! times:{}ms", t2 - t1);
        return interceptor;
    }

}

```



## 4、数据库语句

```java
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



## 5、`Mapper` 扫描配置

> 两种方式，一种是每个mapper上使用注解@Mapper  
>
> 另一种如下 ，全局配置



```java
@Configuration
@MapperScan("com.healerjean.proj.data.mapper")
public class MybatisPlusConfig {

}
```



## 6、`User`实体类 

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



## 7、`Mapper`类

```java
package com.healerjean.proj.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.data.entity.User;
public interface UserMapper  extends BaseMapper<User> {


}

```



## 8、测试 

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





# 二、 `Wrapper` 对象



## 1、`QueryWrapper`  

> 继承自 ·`AbstractWrapper` ,自身的内部属性 `entity` 也用于生成` where` 条件 及` LambdaQueryWrapper`,

| 区别                                          | `QueryWrapper`               | `LambdaQueryWrapper`               |
| --------------------------------------------- | ---------------------------- | ---------------------------------- |
| `eq(boolean condition, R column, Object val)` | 不支持 `condition` 条件判断  | 支持条件判断                       |
| `orderByDesc` 字符串                          | 支持字符串，不支持方法引用符 | 不支持排序字符串，只支持方法引用符 |
| ` select `字符串                              | 支持字符串，不支持方法引用符 | 不支持排序字符串，只支持方法引用符 |



### 1）、获取`QueryWrapper`对象  

#### a、`Wrappers.<User>lambdaQuery()`

```java
LambdaQueryWrapper<User> userWrapper = Wrappers.<User>lambdaQuery()
   .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())

List<User> users = userMapper.selectList(userWrapper);
System.out.println(JsonUtils.toJsonString(users));
```



#### b、`Wrappers.lambdaQuery(User.class)`  

```java
LambdaQueryWrapper<User> userWrapper = Wrappers.lambdaQuery(User.class)
   .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())

List<User> users = userMapper.selectList(userWrapper);
System.out.println(JsonUtils.toJsonString(users));
```



#### c、`new QueryWrapper<User>().lambda()`

```java
LambdaQueryWrapper<User> userWrapper = new QueryWrapper<User>().lambda()
   .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())


List<User> users = userMapper.selectList(userWrapper);
System.out.println(JsonUtils.toJsonString(users));
```



#### d、`new QueryWrapper<UserDemo>()`

```java
public Page<UserDemo> queryUserDemoPage(PageQueryBO<UserDemoQueryBO> pageQuery) {
    UserDemoQueryBO query = pageQuery.getData();
    QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
    if (!CollectionUtils.isEmpty(query.getSelectFields())) {
        queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
    }
    if (!CollectionUtils.isEmpty(query.getOrderByList())) {
        query.getOrderByList().forEach(item -> queryWrapper.orderBy(Boolean.TRUE, 
                                                                    item.getDirection(), 
                                                                    item.getProperty()));
    }
    LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
            .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
            .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())

    Page<UserDemo> page = new Page<>(pageQuery.getCurrPage(), pageQuery.getPageSize(), 
                                     pageQuery.getSearchCountFlag());
    return userDemoDao.page(page, lambdaQueryWrapper);
}
```



### 2）`select`：设置查询字段 

> 分法为两类.第二类方法为:过滤查询字段(主键除外),入参不包含 class 的调用前需要`wrapper`内的`entity`属性有值!      
>
> 这两类方法重复调用以最后一次为准

```java
select(String... sqlSelect)
select(Predicate<TableFieldInfo> predicate)
select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)
    
    
select("id", "name", "age")
select(i -> i.getProperty().startsWith("test"))
```



```java
QueryWrapper<SysDistrict> queryWrapper = new QueryWrapper();
queryWrapper.select("distinct province_code, province_name")
    .lambda()
    .eq(SysDistrict::getProvinceCode, provinceCode)
    .eq(SysDistrict::getStatus, StatusEnum.EFFECT.getCode());
SysDistrict sysDistrict = publicSysDistrictDao.selectOne(queryWrapper);
```



```java
package com.jd.merchant.business.platform.core.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;

/**
* MybatisPlusUtil
* @author zhangyujin
* @date 2023/6/5  15:23.
*/
public class MybatisPlusUtil {


  /**
   * fieldValues
   * @param fieldValues fieldValues
   * @param queryWrapper queryWrapper
   * @param aClass aClass
   * @param <T>  <T>
   */
  public static <T> void fieldValues(List<String> fieldValues, 
                                     LambdaQueryWrapper<T> queryWrapper, 
                                     Class<T> aClass) {
      if (CollectionUtils.isEmpty(fieldValues)) {
          return;
      }
      Predicate<TableFieldInfo> predicate = null;
      for (String field : fieldValues) {
          predicate = predicate == null ? p -> p.getColumn().equals(field) : predicate.or(p -> p.getColumn().equals(field));
      }
      queryWrapper.select(aClass, predicate);
  }

}

```



## 2、`UpdateWrapper、LambdaUpdateChainWrapper`

> 继承自 `AbstractWrapper` ,自身的内部属性 `entity` 、也用于生成 `where` 条件（主要是这个功能）
> 及 `LambdaUpdateWrapper`, `LambdaUpdateChainWrapper`可以通过 `new UpdateWrapper().lambda()` 方法获取!



### 1）`UpdateWrapper`

```java
Wrapper<UserDemo> updateWrapper = Wrappers.lambdaUpdate(UserDemo.class)
              .eq(Objects.nonNull(userDemo.getId()), UserDemo::getId, userDemo.getId())
              .set(Objects.nonNull(userDemo.getEndTime()), UserDemo::getEndTime, userDemo.getEndTime());
      return userDemoDao.update(updateWrapper);
```



```java
Wrapper<BPrizeVenderDetail> updateWrapper = new UpdateWrapper<BPrizeVenderDetail>().lambda()
          .set(BPrizeVenderDetail::getStatus, status).
          eq(BPrizeVenderDetail::getBenefitsId, benefitsId);
  return bPrizeVenderDetailDao.update(updateWrapper);
```



```java
/**
* userMapper.updateById(user); //根据Id更新
* userLambdaUpdateChainWrapper.update(); //条件更新
*/
@Test
public void update(){

    User user = userMapper.selectById(1L);
    Wrapper userWrapper = new UpdateWrapper<User>().lambda()
        .set(User::getEmail, "h@gmail.com")
        .setSql("age = 24").eq(User::getId, user.getId())
        .eq(User::getId, 2L);
    user.setName("jjjjjk"); //会生效
    user.setEmail("66666"); //不会生效 以updateWrapper里面的为主
    //一般不用这二者的结合，毫无意义，写出来的sql也很有可能是错误的
    userMapper.update(user, userWrapper);


    //使用下面这两个
    userMapper.updateById(user);

    LambdaUpdateChainWrapper<User> userLambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(userMapper)
        .set(User::getName,"Name")
        .set(User::getEmail, "update@gmail.com")
        .eq(User::getId, 1L);
    //一般我们使用这个非常好
    userLambdaUpdateChainWrapper.update();

    user.setAge(63); //会生效，
    user.setEmail("66666"); //不会生效 以userLambdaUpdateChainWrapper里面的为主
    //使用下面这个其实也毫无意义
    userLambdaUpdateChainWrapper.update(user);
}

```



### 2）`set`：SET 字段

```java
set(String column, Object val)
set(boolean condition, String column, Object val)
    


例: set("name", "老李头")
例: set("name", "")--->数据库字段值变为空字符串
例: set("name", null)--->数据库字段值变为null
```



### 3）`setSql`：设置 SET 部分 SQL

```java
setSql(String sql)

例: setSql("name = '老李头'")
```









# 三、Wrapper方法的的使用 

## 1、`eq`：等于 =

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



## 2、`ne`：不等于 <>

```java
ne(R column, Object val)
ne(boolean condition, R column, Object val)
    
例: ne("name", "老王")--->name <> '老王'
```



## 3、`gt`：大于 >

```java
gt(R column, Object val)
gt(boolean condition, R column, Object val)
    
例: gt("age", 18)--->age > 18
```





## 4、`ge`：大于等于 >=

```java
lt(R column, Object val)
lt(boolean condition, R column, Object val)


ge("age", 18)--->age >= 18
```

```java

```


## 5、`lt`：小于 <

```java
lt(R column, Object val)
lt(boolean condition, R column, Object val)

    
例: lt("age", 18)--->age < 18
```



## 6、`le`：小于等于 <=

```java
le(R column, Object val)
le(boolean condition, R column, Object val)
    
例: le("age", 18)--->age <= 18
```



## 7、`between`：ETWEEN 值1 AND 值2

```java
between(R column, Object val1, Object val2)
between(boolean condition, R column, Object val1, Object val2)
    
例: between("age", 18, 30)--->age between 18 and 30
```



## 8、`notBetween`：NOT BETWEEN 值1 AND 值2

```java
notBetween(R column, Object val1, Object val2)
notBetween(boolean condition, R column, Object val1, Object val2)
    
例: notBetween("age", 18, 30)--->age not between 18 and 30
```



## 9、`like`：LIKE '%值%'

```java
like(R column, Object val)
like(boolean condition, R column, Object val)
    
例: like("name", "王")--->name like '%王%'
```



## 10、`notLike`：NOT LIKE '%值%'

```java
notLike(R column, Object val)
notLike(boolean condition, R column, Object val)
    
例: notLike("name", "王")--->name not like '%王%'
```



## 11、`likeLeft`：LIKE '%值'

```java
likeLeft(R column, Object val)
likeLeft(boolean condition, R column, Object val)
    
例: likeLeft("name", "王")--->name like '%王'
```



## 12、`likeRight`：LIKE '值%'

```java
likeRight(R column, Object val)
likeRight(boolean condition, R column, Object val)
    
例: likeRight("name", "王")--->name like '王%'
```



## 13、`isNull`：字段 IS NULL

```java
isNull(R column)
isNull(boolean condition, R column)
    
例: isNull("name")--->name is null
```


## 14、`isNotNull`：字段 IS NOT NULL

```java
isNotNull(R column)
isNotNull(boolean condition, R column)
    
例: isNotNull("name")--->name is not null    
```



## 15、`in`：字段 IN

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



## 16、`notIn`：字段 IN (value.get(0), value.get(1), ...)

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



## 17、`inSql`：字段 IN ( sql语句 )

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





## 18、`notInSql`：字段 NOT IN ( sql语句 )

```java
notInSql(R column, String inValue)
notInSql(boolean condition, R column, String inValue)
    

例: notInSql("id", "select id from table where id < 3")
    
    --->age not in (select id from table where id < 3)
```





## 19、`groupBy`：分组：GROUP BY 字段, .

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





## 20、`orderBy/orderByAsc`：ORDER BY 字段, ... ASC


```java
orderBy(boolean condition, boolean isAsc, R... columns)
```

```java
orderByAsc(R... columns)
orderByAsc(boolean condition, R... columns)

例: orderByAsc("id", "name")
    
    --->order by id ASC,name ASC
```




## 21、`orderByDesc`：排序：ORDER BY 字段, ... DESC

```java
orderByDesc(R... columns)
orderByDesc(boolean condition, R... columns)
   
例: orderByDesc("id", "name")--->order by id DESC,name DESC
```





## 22、`having`：HAVING ( sql语句 )

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



## 23、`or`：拼接 OR 

> 注意事项：主动调用`or`表示紧接着下一个**方法**不是用`and`连接!(不调用`or`则默认为使用`and`连接)

```java
or()
or(boolean condition)

例: `eq("id",1).or().eq("name","老王")`--->`id = 1 or name = '老王'`
```



### 1）`OR` 嵌套 

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



## 24、`and`：默认为and

```java
and(Consumer<Param> consumer)
and(boolean condition, Consumer<Param> consumer)

```

### 1）`and` 嵌套

```java
例:  and(i -> i.eq("name", "李白")
        .ne("status", "活着"))
    
 --->and (name = '李白' and status <> '活着')
```





## 25、`nested`：正常嵌套 不带 AND 或者 OR

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





## 26、`apply`：拼接 sql 

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

```java
LambdaQueryWrapper<SystemHelpDoc> queryWrapper = Wrappers.lambdaQuery(SystemHelpDoc.class)
    .like(StringUtils.isNotBlank(queryBo.getTitle()), SystemHelpDoc::getTitle, queryBo.getTitle())
    .in(CollectionUtils.isNotEmpty(queryBo.getIds()), SystemHelpDoc::getId, queryBo.getIds())
    .eq(Objects.nonNull(queryBo.getStatus()), SystemHelpDoc::getStatus, queryBo.getStatus());
if (StringUtils.isNotBlank(queryBo.getUserTypes())) {
		String[] userTypes = queryBo.getUserTypes().split(",");
		for (String userType : userTypes) {
   		 String applySql = "find_in_set(?, user_types)";
   		 queryWrapper.apply(StringUtils.isNotBlank(queryBo.getUserTypes()), applySql, userType);
		}
}
queryWrapper.orderByDesc(SystemHelpDoc::getModifiedTime);
```



## 27、`last`：无视优化规则直接拼接到 sql 的最后

> 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
>
>   



```java
last(String lastSql)
last(boolean condition, String lastSql)
    
    
例: last("limit 1")  
```



## 28、`exists`：拼接 EXISTS ( sql语句 )

```java
exists(String existsSql)
exists(boolean condition, String existsSql)
    
例: exists("select id from table where age = 1")
    
    --->exists (select id from table where age = 1)
```



## 29、`notExists`：拼接 NOT EXISTS ( sql语句 )

```java
notExists(String notExistsSql)
notExists(boolean condition, String notExistsSql)
    
    
例: notExists("select id from table where age = 1")
    --->not exists (select id from table where age = 1)
```





# 四、复杂`SQL `

## 1、返回自定义对象 

### 1）Mapper

```java
 public interface UserMapper  extends BaseMapper<User> {


    @Select("select * from user where name = #{name}")
    //写不写下面这行都行
    // @ResultType(UserDTO.class)
    @Results(
            @Result(property = "userId", column = "id")
    )
    List<UserDTO> selectUserDtoList(UserDTO userDTO);
}

```

### 2）测试类 

```java
@Test
public void userDTO(){
    UserDTO userDTO = new UserDTO();
    userDTO.setName("healer");
    List<UserDTO> userDTOS = userMapper.selectUserDtoList(userDTO);
    System.out.println(JsonUtils.toJsonString(userDTOS));
}


[{"userId":1235553744515612673,"name":"healer","age":22,"email":"22"}]
```



## 2、原生·SQL·使用 ·Script· 语句 

### 1）Mapper

```java
@Select({
    "<script>",
    "select * from user where",
    "<if test='ids != null and ids.size > 0'> ",
    " id in",
    "<foreach collection='ids' index='index' item='item' open='(' separator=',' close=')'>",
    " #{item} ",
    "</foreach>",
    "</if>",
    "</script>"
})
List<UserDTO> selectListByScript(UserDTO userDTO);
```



### 2）测试类 

```java
@Test
public void selectListByScript() {
    UserDTO userDTO = new UserDTO();
    userDTO.setIds(Arrays.asList(1L, 2L));
    List<UserDTO> userDTOS = userMapper.selectListByScript(userDTO);
    System.out.println(JsonUtils.toJsonString(userDTOS));
}


[{"id":1,"name":"healer","age":22,"email":"healerjean@gmial.com"}]
```



## 3、配置`mapper.xml` 

### 1）`application.properties`配置

```properties
# 配置 mybatis的一些配置，也可以在 application.properties 中配置，如果配置了就不需要了mybatis.xml
#mybatis-plus.config-location=classpath:mybatis.xml
#Maven 多模块项目的扫描路径需以 classpath*: 开头 （即加载多个 jar 包下的 XML 文件）
mybatis-plus.mapper-locations=classpath*:mapper/*.xml
mybatis-plus.type-aliases-package=com.healerjean.proj.data.entity
##主键类型  0:"数据库ID自增，非常大", 1:"用户输入ID（如果用户不输入，则默认是0）",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
mybatis-plus.id-type: 0
/** mybatis-plus如果希望使用数据库自增 */
// @TableId(value = "id", type = IdType.AUTO)
//private Long id;
 #字段策略 0:"忽略判断",1:"非 NULL 判断"),2:"非空判断"
mybatis-plus.field-strategy: 2
 #数据库大写下划线转换
mybatis-plus.capital-mode: true
mybatis-plus.refresh-mapper: true

```



### 2）`mybatis.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="cacheEnabled" value="false"/>
        <setting name="lazyLoadingEnabled" value="false"/>
        <setting name="defaultStatementTimeout" value="25000"/>
    </settings>

</configuration>

```



### 3）`mapper.xml`  

![1584524650597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584524650597.png)



```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.healerjean.proj.data.mapper.UserMapper">


    <select id = "selectByMappeXml" resultType="com.healerjean.proj.dto.UserDTO">
              select * from user where name = #{name}
    </select>

</mapper>

```



### 4）`Mapepr.java`

```java
List<UserDTO> selectByMappeXml(UserDTO userDTO);
```



### 5）测试

```java

@Test
public void test(){
    UserDTO userDTO = new UserDTO();
    userDTO.setName("healer");
    List<UserDTO> userDTOS = userMapper.selectByMappeXml(userDTO);
    System.out.println(JsonUtils.toJsonString(userDTOS));
}


[{"id":1,"name":"healer","age":22,"email":"healerjean@gmial.com"}]
```





## 4、分页查询

### 1）、准备数据

#### a、`@Configuration`

```java
@Configuration
@MapperScan("com.healerjean.proj.data.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

}

```

#### b、`BeanUtils`封装

```java
public static <T> PageDTO<T> toPageDTO(IPage iPage, List<T> datas) {
    if (datas == null || datas.isEmpty() || iPage == null) {
        return null;
    } else {
        return new PageDTO(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getPages(), datas);
    }
}
```



### 2）简单分页

#### a、简单分页

```java
/** 传入QueryWrapper分页 */
@Test
public void page1 (){    
    Page<User> page = new Page<>(1, 2);
    Wrapper<User> userWrapper = new QueryWrapper<User>().lambda();
    IPage<User> userIPage = userMapper.selectPage(page, userWrapper);
    System.out.println(userIPage);
}

/** 传入普通参数分页 */
@Test
public void page2(){
    Page<UserDTO> page = new Page<>(1, 2);
    String name = "name";
    IPage<UserDTO> users = userMapper.selectMapperXmlPage(page, name);
    System.out.println(users);
}

```

#### b、`Mapper`

```java
public interface UserMapper extends BaseMapper<User> {

    IPage<UserDTO> selectMapperXmlPage(Page<UserDTO> page , String name);

}
```

```xml
<select id="selectMapperXmlPage" resultType="com.healerjean.proj.dto.UserDTO">
    select * from user where name = #{name}
</select>
```



### 3、复杂分页 

#### a、`Service`

```java
@Test
public void page3() {
    Page page = new Page<>(1, 2);
    QueryWrapper wrapper = Wrappers.<User>query().eq("name", "name");
    IPage<UserDTO> users = userMapper.selectMapperXmlFZPage(page, wrapper);
    System.out.println(users);
}
```

#### b、`Mapper`

```java
public interface UserMapper extends BaseMapper<User> {

    IPage<UserDTO> selectMapperXmlFZPage(Page page,   @Param(Constants.WRAPPER) QueryWrapper<UserDTO> queryWrapper);
}

```

```xml
<select id="selectMapperXmlFZPage" resultType="com.healerjean.proj.dto.UserDTO">
    select * from user  ${ew.customSqlSegment}
</select>
```



### 4、传入实体对象分页

#### a、`Service`

```java
@Test
public void page4() {
    Page page = new Page<>(1, 2);
    UserDTO userDTO = new UserDTO();
    userDTO.setName("name");
    //分页查询
    IPage<UserDTO>  users = userMapper.selectMapperXmlEntity(page, userDTO);
    System.out.println(users);

    //list查询
    List<UserDTO>  user2s = userMapper.selectMapperXmlEntity( userDTO);
    System.out.println(user2s);
}

```

#### b、`Mapper`

```java
public interface UserMapper extends BaseMapper<User> {

    IPage<UserDTO> selectMapperXmlEntity(Page page,   @Param("userDTO") UserDTO userDTO);
    List<UserDTO> selectMapperXmlEntity( @Param("userDTO") UserDTO userDTO);
}
```

```xml
<select id="selectMapperXmlEntity" resultType="com.healerjean.proj.dto.UserDTO">
    select * from user where  name = #{userDTO.name}
</select>
```



## 3、`Wrapepr`复用

```java
@Test
public void queryManyUse() {
    QueryWrapper<User> queryWrapper = new QueryWrapper();
    for (int i = 0; i < 2; i++) {
        queryWrapper.lambda().clear();
        queryWrapper.lambda().eq(User::getName, "healerjean");
        queryWrapper.lambda().eq(User::getEmail, i==1 ? "healer@gmail.com" : "healerjean@gmail.com");
        List<User> users =  userMapper.selectList(queryWrapper);
        System.out.println(users);
    }
}
```



## 4、返回自定义函数和字段

> 注意大 使用双引号，否则出来是大写 `MINID`、`MAXID`

```java
QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
queryWrapper.select("min(id) as \"minId\"", "max(id) as \"maxId\"");
LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
        .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
        .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
        .like(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
        .like(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
        .like(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
        .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
        .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
        .le(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
        .ge(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());
Map<String, Object> map = userDemoDao.getMap(lambdaQueryWrapper);
return ImmutablePair.of(MapUtils.getLong(map, "minId"), MapUtils.getLong(map, "maxId"));
```



# 五、问题

## 1、日期问题 

### 1）日期格式

> 可能会遇到日期格式的时间段问题，当数据库的时间为 `DATE` 类型时，`MyBatis` 的`jdbcType`应该使用 `DATE`
> `jdbcType=DATE`，而不是使用`jdbcType=TIMESTAMP`



### 2）返回 `LocalDate `和 `LocalDateTime` 报错

> 提高 `druid `版本,我提高到了1.1.21

```java
org.springframework.dao.InvalidDataAccessApiUsageException: Error attempting to get column 'create_date' from result set.  Cause: java.sql.SQLFeatureNotSupportedException
; null; nested exception is java.sql.SQLFeatureNotSupportedException
```



```xml
<!--数据源-->
<com-alibaba-druid.version>1.1.20</com-alibaba-druid.version>


<!--数据源-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>${com-alibaba-druid.version}</version>
</dependency>
```



## 2、更新 `Null` 字段

> **`updateById(`) 方法不能更新字段为 `null` 的原因及解决办法。**
>
> `Mybatis-plus` 的字段策略（`FieldStrategy`）有三种策略：默认的更新策略是 `NOT_NULL`，即通过接口更新数据时数据为 `NULL`值时将不更新进数据库。

| 策略              | 说明                |
| ----------------- | ------------------- |
| **`IGNORED`**`：  | 0 忽略              |
| **`NOT_NULL`**：  | 1 非 NULL，默认策略 |
| **`NOT_EMPTY`**： | 2 非空              |

### 1）方案1：在配置文件中修改全局策略

> 这样做是进行全局配置，在更新时会忽略对所有字段的判断。但是如果一些字段没有传值过来，会被直接更新为null，可能会影响其它业务数据的准确性。不推荐使用此方法。

```yaml
mybatis-plus.global-config.db-config.field-strategy=ignored

#yml文件格式：
mybatis-plus:
  global-config:
      #字段策略 0:"忽略判断",1:"非 NULL 判断",2:"非空判断"
    field-strategy: 0
```



### 2）方案2：对指定的字段单独设置 `field-strategy`

> 设置好了之后，在更新时就可以直接使用 `mybatis-plus` 中的 `updateById` 方法就可以成功将字段更新为 `null,`但是这样做存在一定的弊端，就是当需要这样处理的字段比较多时，要给对应的字段都要添加上这样的注解。

```java
@TableField(updateStrategy = FieldStrategy.IGNORED)
private String updateBy;
```



### 3）方案2：使用 `update`方法结合`UpdateWrapper`方式更新

```java
User user=userService.lambdaQuery().eq(User::getUserId,userId).one();
if(user!=null){
    userService.update(user,new UpdateWrapper<User>().lambda()
               .set(User::getUserName,null)
               .eq(User::getUserId,user.getUserId()));
}

```



## 3、返回值问题

#### 1）更新返回 `boolean`

| 数据情况     | 说明                                                         |      |
| ------------ | ------------------------------------------------------------ | ---- |
| 数据不存在   | 用于表示更新操作是否成功。如果数据不存在，方法通常会返回 `false`，因为更新操作没有影响到任何记录。 |      |
| 数据并未修改 | 这通常意味着没有满足更新条件的记录被更新，因此该方法通常会返回 `false` |      |
| 返回 `int`   | `userDemoDao.getBaseMapper().update(null, updateWrapper)`    |      |



## 4、`SQL` 执行问题

### 1）多出现 `limit`

#### a、字段名冲突

> **原因：**在执行 `sql` 的时候，使用了自定义的分页，但是执行之后自动在后面加上了一个 `limit` ，导致 `sql` 执行报错，虽然自己没有使用，但是项目中配置了 `PageHelper` 分页插件，然后查找资料，才发现只要自定义的 `sql` 中有 `pageSize` 和 `pageNum`参数值，就会在执行之后自动加上 `limit `  
>
> **解决：**在执行的 `sql` 中，把 `pageSize` 和 `pageNum` 规避掉，可以换成 `startIndex` 和 `maxCount`

```sql
limit #{pageNum,jdbcType=INTEGER},  #{pageSize,jdbcType=INTEGER}

替换成limit #{maxCount,jdbcType=INTEGER}, #{startIndex,jdbcType=INTEGER}

```

#### b、多线程复用

> https://developer.aliyun.com/article/1009244      
>
> 1、保证`startPage() `后紧跟 `sql` 命令         
>
> 2、担心有嫌犯潜逃，在有问题的方法使用 `clearPage()` 来打补丁。有问题的方法使用 `clearPage()` 来打补丁。









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
		id: 'yCRH6FSZfMt4E7Ki',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->

