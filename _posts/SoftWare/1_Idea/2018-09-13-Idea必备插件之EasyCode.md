---
title: Idea必备插件之EasyCode
date: 2018-09-13 00:00:00
tags: 
- SoftWare
category: 
- SoftWare
description: Idea必备插件之EasyCode
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





## 一、安装  



## 1、`idea` 安装 `EasyCode`

![1585881348499](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585881348499.png)



## 2、配置数据库添加数据源



![1585881359804](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585881359804.png)



```sql
CREATE TABLE `user` (
    `id` bigint(20) unsigned NOT NULL,
    `city` varchar(20) NOT NULL DEFAULT '',
    `name` varchar(20) NOT NULL DEFAULT '',
    `status` int(10) NOT NULL DEFAULT '0' COMMENT '状态',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



## 3、配置映射：

### 1）字段类型

> 有些数据库的字段类型，这个插件识别不了，添加的时候注意`\\`

| 数据库类型                | Java类型             |
| ------------------------- | -------------------- |
| varchar(\(\d+\))?         | java.lang.String     |
| char(\(\d+\))?            | java.lang.String     |
| text                      | java.lang.String     |
|                           |                      |
| int(\(\d+\))?             | java.lang.Integer    |
|                           |                      |
| bigint(\(\d+\))?          | java.lang.Long       |
| bigint(\(\d+\))? unsigned | java.lang.Long       |
|                           |                      |
| datetime                  | java.util.Date       |
| timestamp                 | java.util.Date       |
| date                      | java.util.Date       |
|                           |                      |
| decimal(\(\d+,\d+\))?     | java.lang.BigDecimal |
| decimal(\(\d+\))?         | java.lang.BigDecimal |
|                           |                      |
| boolean                   | java.lang.Boolean    |





![1585882246601](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585882246601.png)



### 2）字段备注

> 如果有的备注数据库中就没有，需要我们手动添加



![1585881793630](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585881793630.png)



### 3）是否出现 



![1585881869173](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585881869173.png)



### 4）最终映射

![1585882901262](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585882901262.png)



# 二、模版

**默认的模板一 Defualt**  

![1585882554516](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585882554516.png)



**默认的模板2 MybatisPlus**  

![1585882565203](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585882565203.png)



## 1、`Default` 模板     

![1585883184416](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585883184416.png)





### 1）`Entity`

```java
package com.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2020-04-03 11:09:05
 */
public class User implements Serializable {
    private static final long serialVersionUID = -36714741080529376L;
    
    private Long id;
    
    private String city;
    /**
    * 姓名
    */
    private String name;
    /**
    * 状态
    */
    private Integer status;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
```





### 2）`Dao`

```java
package com.dao;

import com.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-03 11:09:05
 */
public interface UserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param user 实例对象
     * @return 对象列表
     */
    List<User> queryAll(User user);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
```

### 3）`Mappe.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dao.UserDao">

    <resultMap type="com.entity.User" id="UserMap">
        <result property="id" column="id" jdbcType="INTEGER"/>
        <result property="city" column="city" jdbcType="VARCHAR"/>
        <result property="name" column="name" jdbcType="VARCHAR"/>
        <result property="status" column="status" jdbcType="INTEGER"/>
        <result property="createTime" column="create_time" jdbcType="TIMESTAMP"/>
        <result property="updateTime" column="update_time" jdbcType="TIMESTAMP"/>
    </resultMap>

    <!--查询单个-->
    <select id="queryById" resultMap="UserMap">
        select
          id, city, name, status, create_time, update_time
        from ds_0.user
        where id = #{id}
    </select>

    <!--查询指定行数据-->
    <select id="queryAllByLimit" resultMap="UserMap">
        select
          id, city, name, status, create_time, update_time
        from ds_0.user
        limit #{offset}, #{limit}
    </select>

    <!--通过实体作为筛选条件查询-->
    <select id="queryAll" resultMap="UserMap">
        select
          id, city, name, status, create_time, update_time
        from ds_0.user
        <where>
            <if test="id != null">
                and id = #{id}
            </if>
            <if test="city != null and city != ''">
                and city = #{city}
            </if>
            <if test="name != null and name != ''">
                and name = #{name}
            </if>
            <if test="status != null">
                and status = #{status}
            </if>
            <if test="createTime != null">
                and create_time = #{createTime}
            </if>
            <if test="updateTime != null">
                and update_time = #{updateTime}
            </if>
        </where>
    </select>

    <!--新增所有列-->
    <insert id="insert" keyProperty="id" useGeneratedKeys="true">
        insert into ds_0.user(city, name, status, create_time, update_time)
        values (#{city}, #{name}, #{status}, #{createTime}, #{updateTime})
    </insert>

    <!--通过主键修改数据-->
    <update id="update">
        update ds_0.user
        <set>
            <if test="city != null and city != ''">
                city = #{city},
            </if>
            <if test="name != null and name != ''">
                name = #{name},
            </if>
            <if test="status != null">
                status = #{status},
            </if>
            <if test="createTime != null">
                create_time = #{createTime},
            </if>
            <if test="updateTime != null">
                update_time = #{updateTime},
            </if>
        </set>
        where id = #{id}
    </update>

    <!--通过主键删除-->
    <delete id="deleteById">
        delete from ds_0.user where id = #{id}
    </delete>

</mapper>
```



### 4）`Service`

```java
package com.service;

import com.entity.User;
import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2020-04-03 11:09:05
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
```



### 5）`ServiceImpl`

```java
package com.service.impl;

import com.entity.User;
import com.dao.UserDao;
import com.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-04-03 11:09:05
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long id) {
        return this.userDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.userDao.deleteById(id) > 0;
    }
}
```



### 6）`Controller`

```java
package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-04-03 11:09:05
 */
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public User selectOne(Long id) {
        return this.userService.queryById(id);
    }

}
```





## 2、`MybatisPlus`模板 

![1585883174915](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1585883174915.png)





### 1）`Entity`

```java
##导入宏定义
$!{define.vm}

##保存文件（宏定义）
#save("/entity", ".java")

##包路径（宏定义）
#setPackageSuffix("entity")

##自动导入包（全局变量）
$!{autoImport.vm}
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

##表注释（宏定义）
#tableComment("表实体类")
@SuppressWarnings("serial")
public class $!{tableInfo.name} extends Model<$!{tableInfo.name}> {
#foreach($column in $tableInfo.fullColumn)
    #if(${column.comment})//${column.comment}#end

    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

#foreach($column in $tableInfo.fullColumn)
#getSetMethod($column)
#end

#foreach($column in $tableInfo.pkColumn)
    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.$!column.name;
    }
    #break
#end
}

```



```java
package com.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2020-04-03 11:02:03
 */
@SuppressWarnings("serial")
public class User extends Model<User> {

    private Long id;

    private String city;
    //姓名
    private String name;
    //状态
    private Integer status;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
```





### 2）`Dao`

```java
package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.User;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-03 11:02:03
 */
public interface UserDao extends BaseMapper<User> {

}
```





### 3）`Service`

```java
package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.User;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2020-04-03 11:02:03
 */
public interface UserService extends IService<User> {

}
```



### 4）`ServiceImpl`

```java
package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.UserDao;
import com.entity.User;
import com.service.UserService;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-04-03 11:02:03
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}
```



### 5）`Controller`

```java
package com.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-04-03 11:02:03
 */
@RestController
@RequestMapping("user")
public class UserController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<User> page, User user) {
        return success(this.userService.page(page, new QueryWrapper<>(user)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.userService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody User user) {
        return success(this.userService.save(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody User user) {
        return success(this.userService.updateById(user));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.userService.removeByIds(idList));
    }
}
```





## 3、自定义模板：`V1-MybatisPlus`

```v
## 自定义全局变量
#set($poPath=           "com.healerjean.proj.template.po")
#set($boPath=           "com.healerjean.proj.template.bo")
#set($dtoPath=          "com.healerjean.proj.template.dto")
#set($voPath=           "com.healerjean.proj.template.vo")
#set($mapperPath=       "com.healerjean.proj.template.mapper")
#set($mapperXmlPath=    "com.healerjean.proj.template.mapper")
#set($daoPath=          "com.healerjean.proj.template.dao")
#set($daoImplPath=      "com.healerjean.proj.template.dao.impl")
#set($managerPath=      "com.healerjean.proj.template.manager")
#set($managerImplPath=  "com.healerjean.proj.template.manager.impl")
#set($servicePath=      "com.healerjean.proj.template.service")
#set($serviceImplPath=  "com.healerjean.proj.template.service.impl")
#set($controllerPath=   "com.healerjean.proj.template.controller")
#set($converterPath=    "com.healerjean.proj.template.converter")
#set($reqPath=          "com.healerjean.proj.template.req")
#set($resourcePath=     "com.healerjean.proj.template.resource")
#set($resourceImplPath= "com.healerjean.proj.template.resource.impl")
```



### 1）实体

#### a、`PO.java.vm`

```java
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##保存文件（宏定义）
#save("/po", ".java")

##包路径（宏定义）
#setPackageSuffix(${poPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

##表注释（宏定义）
#tableComment("表实体类")
@Data
@EqualsAndHashCode(callSuper = false)
public class $!{tableInfo.name} extends Model<$!{tableInfo.name}> {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
#if(${column.name}=="id")
    @TableId(value = "id", type = IdType.AUTO)
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end


#foreach($column in $tableInfo.fullColumn)
    
#if(${column.name})
    /**
     * ${column.obj.name}
     */
#end
    public static final String ${column.obj.name.toUpperCase()} = "${column.obj.name}";
#end

#foreach($column in $tableInfo.pkColumn)
    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.$!column.name;
    }
#break
#end
}

```



#### b、`BO.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("BO")

##保存文件（宏定义）
#save("/bo", "BO.java")

##包路径（宏定义）
#setPackageSuffix(${boPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("BO对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)BO对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoBO implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



#### c、`QueryBO.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("QueryBO")

##保存文件（宏定义）
#save("/bo", "QueryBO.java")

##包路径（宏定义）
#setPackageSuffix(${boPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("QueryBO对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)QueryBO对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoQueryBO implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



#### d、`DTO.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("DTO")

##保存文件（宏定义）
#save("/dto", "DTO.java")

##包路径（宏定义）
#setPackageSuffix(${dtoPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("DTO对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.dto;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)DTO对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoDTO implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



#### e、`VO.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("VO")

##保存文件（宏定义）
#save("/vo", "VO.java")

##包路径（宏定义）
#setPackageSuffix(${voPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("VO对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)VO对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoVO implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



#### f、`SaveReq.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("SaveReq")

##保存文件（宏定义）
#save("/req", "SaveReq.java")

##包路径（宏定义）
#setPackageSuffix(${reqPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("SaveReq对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.req;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)SaveReq对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoSaveReq implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



#### g、`QueryReq.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("QueryReq")

##保存文件（宏定义）
#save("/req", "QueryReq.java")

##包路径（宏定义）
#setPackageSuffix(${reqPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("QueryReq对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.req;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)QueryReq对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoQueryReq implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



#### h、`DeleteReq.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("DeleteReq")

##保存文件（宏定义）
#save("/req", "DeleteReq.java")

##包路径（宏定义）
#setPackageSuffix(${reqPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

##表注释（宏定义）
#tableComment("DeleteReq对象")
@Accessors(chain = true)
@Data
public class $!{tableName} implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

#foreach($column in $tableInfo.fullColumn)
    
#if(${column.comment})
    /**
     * ${column.comment}
     */
#end
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

}

```

```java
package com.healerjean.proj.template.req;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (UserDemo)DeleteReq对象
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Accessors(chain = true)
@Data
public class UserDemoDeleteReq implements Serializable {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 1有效 0 废弃
     */
    private Integer validFlag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;

}


```



### 2）`Converter.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("Converter")

##保存文件（宏定义）
#save("/converter", "Converter.java")

##包路径（宏定义）
#setPackageSuffix(${converterPath})

##自动导入包（全局变量）
$!{autoImport.vm}
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
import ${poPath}.${tableInfo.name};
import ${boPath}.${tableInfo.name}BO;
import ${boPath}.${tableInfo.name}QueryBO;
import ${dtoPath}.${tableInfo.name}DTO;
import ${voPath}.${tableInfo.name}VO;
import ${reqPath}.${tableInfo.name}SaveReq;
import ${reqPath}.${tableInfo.name}QueryReq;
import ${reqPath}.${tableInfo.name}DeleteReq;



##表注释（宏定义）
#tableComment("Converter")
@Mapper
public interface $!{tableName} {

    /**
     * INSTANCE
     */
    $!{tableName} INSTANCE = Mappers.getMapper($!{tableName}.class);

   
     /**
     * convert${tableInfo.name}PoToBO
     *
     * @param po po
     * @return ${tableInfo.name}BO
     */
    ${tableInfo.name}BO convert${tableInfo.name}PoToBo(${tableInfo.name} po);
    
    
     /**
     * convert${tableInfo.name}PoToBoLists
     *
     * @param pos pos
     * @return ${tableInfo.name}BO
     */
    List<${tableInfo.name}BO> convert${tableInfo.name}PoToBoList(List<${tableInfo.name}> pos);
    
    
     /**
     * convert${tableInfo.name}BoToPo
     *
     * @param bo bo
     * @return ${tableInfo.name}BO
     */
    ${tableInfo.name} convert${tableInfo.name}BoToPo(${tableInfo.name}BO bo);
    
     /**
     * convert${tableInfo.name}BoToPoList
     *
     * @param bos bos
     * @return List<${tableInfo.name}>
     */
     List<${tableInfo.name}> convert${tableInfo.name}BoToPoList(List<${tableInfo.name}BO> bos);
    
    
     /**
     * convert${tableInfo.name}BoToDto
     *
     * @param bo bo
     * @return ${tableInfo.name}DTO
     */
    ${tableInfo.name}DTO convert${tableInfo.name}BoToDto(${tableInfo.name}BO bo);

     /**
     * convert${tableInfo.name}BoToDtoList
     *
     * @param bos bos
     * @return List<${tableInfo.name}DTO>
     */
     List<${tableInfo.name}DTO> convert${tableInfo.name}BoToDtoList(List<${tableInfo.name}BO> bos);
      
     /**
     * convert${tableInfo.name}DtoToBo
     *
     * @param dto dto
     * @return ${tableInfo.name}BO
     */
    ${tableInfo.name}BO convert${tableInfo.name}DtoToBo(${tableInfo.name}DTO dto);
  
     /**
     * convert${tableInfo.name}DtoToBoList
     *
     * @param dtos dtos
     * @return ${tableInfo.name}BO
     */
    List<${tableInfo.name}BO> convert${tableInfo.name}DtoToBoList(List<${tableInfo.name}DTO> dtos);
  
   
    /**
     * convert${tableInfo.name}VO
     *
     * @param bo bo
     * @return ${tableInfo.name}VO
     */
    ${tableInfo.name}VO convert${tableInfo.name}BoToVo(${tableInfo.name}BO bo);
    
     /**
     * convert${tableInfo.name}BoToVoList
     *
     * @param bos bos
     * @return ${tableInfo.name}VO
     */
    List<${tableInfo.name}VO> convert${tableInfo.name}BoToVoList(List<${tableInfo.name}BO> bos);
        
     /**
     * convert${tableInfo.name}SaveReqToBo
     *
     * @param req req
     * @return ${tableInfo.name}BO
     */
    ${tableInfo.name}BO convert${tableInfo.name}SaveReqToBo(${tableInfo.name}SaveReq req);

     /**
     * convert${tableInfo.name}DeleteReqToBo
     *
     * @param req req
     * @return ${tableInfo.name}BO
     */
    ${tableInfo.name}BO convert${tableInfo.name}DeleteReqToBo(${tableInfo.name}DeleteReq req);

     /**
     * convert${tableInfo.name}QueryReqToBo
     *
     * @param req req
     * @return ${tableInfo.name}BO
     */
    ${tableInfo.name}QueryBO convert${tableInfo.name}QueryReqToBo(${tableInfo.name}QueryReq req);
                      
}

```

```java
package com.healerjean.proj.template.converter;

import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
import com.healerjean.proj.template.po.UserDemo;
import com.healerjean.proj.template.bo.UserDemoBO;
import com.healerjean.proj.template.bo.UserDemoQueryBO;
import com.healerjean.proj.template.dto.UserDemoDTO;
import com.healerjean.proj.template.vo.UserDemoVO;
import com.healerjean.proj.template.req.UserDemoSaveReq;
import com.healerjean.proj.template.req.UserDemoQueryReq;
import com.healerjean.proj.template.req.UserDemoDeleteReq;



/**
 * (UserDemo)Converter
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Mapper
public interface UserDemoConverter {

    /**
     * INSTANCE
     */
    UserDemoConverter INSTANCE = Mappers.getMapper(UserDemoConverter.class);

   
     /**
     * convertUserDemoPoToBO
     *
     * @param po po
     * @return UserDemoBO
     */
    UserDemoBO convertUserDemoPoToBo(UserDemo po);
    
    
     /**
     * convertUserDemoPoToBoLists
     *
     * @param pos pos
     * @return UserDemoBO
     */
    List<UserDemoBO> convertUserDemoPoToBoList(List<UserDemo> pos);
    
    
     /**
     * convertUserDemoBoToPo
     *
     * @param bo bo
     * @return UserDemoBO
     */
    UserDemo convertUserDemoBoToPo(UserDemoBO bo);
    
     /**
     * convertUserDemoBoToPoList
     *
     * @param bos bos
     * @return List<UserDemo>
     */
     List<UserDemo> convertUserDemoBoToPoList(List<UserDemoBO> bos);
    
    
     /**
     * convertUserDemoBoToDto
     *
     * @param bo bo
     * @return UserDemoDTO
     */
    UserDemoDTO convertUserDemoBoToDto(UserDemoBO bo);

     /**
     * convertUserDemoBoToDtoList
     *
     * @param bos bos
     * @return List<UserDemoDTO>
     */
     List<UserDemoDTO> convertUserDemoBoToDtoList(List<UserDemoBO> bos);
      
     /**
     * convertUserDemoDtoToBo
     *
     * @param dto dto
     * @return UserDemoBO
     */
    UserDemoBO convertUserDemoDtoToBo(UserDemoDTO dto);
  
     /**
     * convertUserDemoDtoToBoList
     *
     * @param dtos dtos
     * @return UserDemoBO
     */
    List<UserDemoBO> convertUserDemoDtoToBoList(List<UserDemoDTO> dtos);
  
   
    /**
     * convertUserDemoVO
     *
     * @param bo bo
     * @return UserDemoVO
     */
    UserDemoVO convertUserDemoBoToVo(UserDemoBO bo);
    
     /**
     * convertUserDemoBoToVoList
     *
     * @param bos bos
     * @return UserDemoVO
     */
    List<UserDemoVO> convertUserDemoBoToVoList(List<UserDemoBO> bos);
        
     /**
     * convertUserDemoSaveReqToBo
     *
     * @param req req
     * @return UserDemoBO
     */
    UserDemoBO convertUserDemoSaveReqToBo(UserDemoSaveReq req);

     /**
     * convertUserDemoDeleteReqToBo
     *
     * @param req req
     * @return UserDemoBO
     */
    UserDemoBO convertUserDemoDeleteReqToBo(UserDemoDeleteReq req);

     /**
     * convertUserDemoQueryReqToBo
     *
     * @param req req
     * @return UserDemoBO
     */
    UserDemoQueryBO convertUserDemoQueryReqToBo(UserDemoQueryReq req);
                      
}


```



### 3）`Mapper.java.vm`

```
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("Mapper")

##保存文件（宏定义）
#save("/mapper", "Mapper.java")

##包路径（宏定义）
#setPackageSuffix(${mapperPath})

import ${poPath}.$!tableInfo.name;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

##表注释（宏定义）
#tableComment("表数据库访问层")
public interface $!{tableInfo.name}Mapper extends BaseMapper<$!{tableInfo.name}> {


}

```

```
package com.healerjean.proj.template.mapper;

import com.healerjean.proj.template.po.UserDemo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * (UserDemo)表数据库访问层
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
public interface UserDemoMapper extends BaseMapper<UserDemo> {


}


```



### 4）`Mapper.xml.vm`

```v
##引入mybatis支持
$!{mybatisSupport.vm}
$!{healerjean.vm}

##设置保存名称与保存位置
$!callback.setFileName($tool.append($!{tableInfo.name}, "Mapper.xml"))
$!callback.setSavePath($tool.append($modulePath, "/src/main/resources/mapper"))

##拿到主键
#if(!$tableInfo.pkColumn.isEmpty())
    #set($pk = $tableInfo.pkColumn.get(0))
#end

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperPath}.$!{tableInfo.name}Mapper">

    <resultMap id="BaseResultMap" type="${poPath}.$!{tableInfo.name}">
#foreach($column in $tableInfo.fullColumn)
        <result column="$!column.obj.name" property="$!column.name"/>
#end
    </resultMap>

        <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        #allSqlColumn()
     </sql>

</mapper>

```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.healerjean.proj.data.mapper.UserDemoMapper">

    <resultMap id="BaseResultMap" type="com.healerjean.proj.data.po.UserDemo">
        <result column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="age" property="age"/>
        <result column="phone" property="phone"/>
        <result column="email" property="email"/>
        <result column="start_time" property="startTime"/>
        <result column="end_time" property="endTime"/>
        <result column="valid_flag" property="validFlag"/>
        <result column="create_time" property="createTime"/>
        <result column="update_time" property="updateTime"/>
    </resultMap>

        <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        id, name, age, phone, email, start_time, end_time, valid_flag, create_time, update_time     </sql>

</mapper>


```



### 5）`Dao.java.vm`

```
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("Dao")

##保存文件（宏定义）
#save("/dao", "Dao.java")

##包路径（宏定义）
#setPackageSuffix(${daoPath})

import com.baomidou.mybatisplus.extension.service.IService;
import ${poPath}.$!tableInfo.name;

##表注释（宏定义）
#tableComment("Dao接口")
public interface $!{tableName} extends IService<$!tableInfo.name> {

}

```

```java
package com.healerjean.proj.template.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.healerjean.proj.template.po.UserDemo;

/**
 * (UserDemo)Dao接口
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
public interface UserDemoDao extends IService<UserDemo> {

}


```



### 6）`DaoImpl.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("DaoImpl")

##保存文件（宏定义）
#save("/dao/impl", "DaoImpl.java")

##包路径（宏定义）
#setPackageSuffix(${daoImplPath})

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ${poPath}.$!{tableInfo.name};
import ${daoPath}.$!{tableInfo.name}Dao;
import ${mapperPath}.$!{tableInfo.name}Mapper;

##表注释（宏定义）
#tableComment("Dao实现类")
@Service
public class $!{tableName} extends ServiceImpl<$!{tableInfo.name}Mapper, $!{tableInfo.name}> implements $!{tableInfo.name}Dao {

}

```

```java
package com.healerjean.proj.template.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.healerjean.proj.template.po.UserDemo;
import com.healerjean.proj.template.dao.UserDemoDao;
import com.healerjean.proj.template.mapper.UserDemoMapper;

/**
 * (UserDemo)Dao实现类
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:15
 */
@Service
public class UserDemoDaoImpl extends ServiceImpl<UserDemoMapper, UserDemo> implements UserDemoDao {

}


```



### 7）`manager.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("Manager")

##保存文件（宏定义）
#save("/manager", "Manager.java")

##包路径（宏定义）
#setPackageSuffix(${managerPath})

import ${poPath}.${tableInfo.name};
import ${boPath}.${tableInfo.name}QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

##表注释（宏定义）
#tableComment("Manager接口")
public interface $!{tableName} {

    /**
     * 保存-$!tableInfo.name
     *
     * @param po po
     * @return boolean
     */    
    boolean save${tableInfo.name}(${tableInfo.name} po);
    
     /**
     * 删除-$!tableInfo.name
     *
     * @param id id
     * @return boolean
     */     
    boolean delete${tableInfo.name}ById(Long id);
    
    /**
     * 更新-$!tableInfo.name
     *
     * @param po po
     * @return boolean
     */    
    boolean update${tableInfo.name}(${tableInfo.name} po);
    
     /**
     * 单条主键查询-$!tableInfo.name
     *
     * @param id id
     * @return ${tableInfo.name}
     */     
    ${tableInfo.name} query${tableInfo.name}ById(Long id);
    
    
     /**
     * 单条查询-$!tableInfo.name
     *
     * @param queryBo queryBo
     * @return ${tableInfo.name}
     */     
    ${tableInfo.name} query${tableInfo.name}Single(${tableInfo.name}QueryBO queryBo);
    
    /**
     * 列表查询-$!tableInfo.name
     *
     * @param queryBo queryBo
     * @return List<${tableInfo.name}>
     */    
    List<${tableInfo.name}> query${tableInfo.name}List(${tableInfo.name}QueryBO queryBo);
   
    /**
     * 分页查询-$!tableInfo.name
     *
     * @param queryBo queryBo
     * @return Page<${tableInfo.name}>
     */    
    Page<${tableInfo.name}> query${tableInfo.name}Paga(${tableInfo.name}QueryBO queryBo);
     
}

```

```java
package com.healerjean.proj.template.manager;

import com.healerjean.proj.template.po.UserDemo;
import com.healerjean.proj.template.bo.UserDemoQueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * (UserDemo)Manager接口
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:16
 */
public interface UserDemoManager {

    /**
     * 保存-UserDemo
     *
     * @param po po
     * @return boolean
     */    
    boolean saveUserDemo(UserDemo po);
    
     /**
     * 删除-UserDemo
     *
     * @param id id
     * @return boolean
     */     
    boolean deleteUserDemoById(Long id);
    
    /**
     * 更新-UserDemo
     *
     * @param po po
     * @return boolean
     */    
    boolean updateUserDemo(UserDemo po);
    
     /**
     * 单条主键查询-UserDemo
     *
     * @param id id
     * @return UserDemo
     */     
    UserDemo queryUserDemoById(Long id);
    
    
     /**
     * 单条查询-UserDemo
     *
     * @param queryBo queryBo
     * @return UserDemo
     */     
    UserDemo queryUserDemoSingle(UserDemoQueryBO queryBo);
    
    /**
     * 列表查询-UserDemo
     *
     * @param queryBo queryBo
     * @return List<UserDemo>
     */    
    List<UserDemo> queryUserDemoList(UserDemoQueryBO queryBo);
   
    /**
     * 分页查询-UserDemo
     *
     * @param queryBo queryBo
     * @return Page<UserDemo>
     */    
    Page<UserDemo> queryUserDemoPaga(UserDemoQueryBO queryBo);
     
}


```



### 8）`managerImpl.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("ManagerImpl")

##保存文件（宏定义）
#save("/manager/impl", "ManagerImpl.java")

##包路径（宏定义）
#setPackageSuffix(${managerImplPath})
##定义服务名
#set($daoName = $!tool.append($!tool.firstLowerCase($!tableInfo.name), "Dao"))

import ${poPath}.${tableInfo.name};
import ${boPath}.${tableInfo.name}QueryBO;
import ${daoPath}.${tableInfo.name}Dao;
import ${managerPath}.${tableInfo.name}Manager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import javax.annotation.Resource;

##表注释（宏定义）
#tableComment("Manager接口")
public class $!{tableName} implements $!{tableInfo.name}Manager {
     
     @Resource
     private $!{tableInfo.name}Dao $daoName;
        
    /**
     * 保存-$!tableInfo.name
     *
     * @param po po
     * @return boolean
     */    
    public boolean save${tableInfo.name}(${tableInfo.name} po){
        return $!{daoName}.save(po);
    }
    
     /**
     * 删除-$!tableInfo.name
     *
     * @param id id
     * @return boolean
     */     
    public boolean delete${tableInfo.name}ById(Long id){
        //todo
        return false;
    }
    
    /**
     * 更新-$!tableInfo.name
     *
     * @param po po
     * @return boolean
     */    
    public boolean update${tableInfo.name}(${tableInfo.name} po){
        LambdaUpdateWrapper<${tableInfo.name}> updateWrapper = Wrappers.lambdaUpdate(${tableInfo.name}.class);
        //todo
        return $!{daoName}.update(updateWrapper);
    }
    
     /**
     * 单条主键查询-$!tableInfo.name
     *
     * @param id id
     * @return ${tableInfo.name}
     */     
    public ${tableInfo.name} query${tableInfo.name}ById(Long id){
        return $!{daoName}.getById(id);
    }
    
    
     /**
     * 单条查询-$!tableInfo.name
     *
     * @param queryBo queryBo
     * @return ${tableInfo.name}
     */     
    public ${tableInfo.name} query${tableInfo.name}Single(${tableInfo.name}QueryBO queryBo){
        LambdaQueryWrapper<${tableInfo.name}> queryWrapper = Wrappers.lambdaQuery(${tableInfo.name}.class);
        //todo
        return $!{daoName}.getOne(queryWrapper);
    }
    
    /**
     * 列表查询-$!tableInfo.name
     *
     * @param queryBo queryBo
     * @return List<${tableInfo.name}>
     */    
    public List<${tableInfo.name}> query${tableInfo.name}List(${tableInfo.name}QueryBO queryBo){
        LambdaQueryWrapper<${tableInfo.name}> queryWrapper = Wrappers.lambdaQuery(${tableInfo.name}.class);
        //todo
        return $!{daoName}.list(queryWrapper);
    }
   
    /**
     * 分页查询-$!tableInfo.name
     *
     * @param queryBo queryBo
     * @return Page<${tableInfo.name}>
     */    
    public Page<${tableInfo.name}> query${tableInfo.name}Paga(${tableInfo.name}QueryBO queryBo){
        LambdaQueryWrapper<${tableInfo.name}> queryWrapper = Wrappers.lambdaQuery(${tableInfo.name}.class);
        //todo
        Page<${tableInfo.name}> pageReq = new Page<>(0, 0, 0);
        return $!{daoName}.page(pageReq, queryWrapper);
    }
     
}

```



```java
package com.healerjean.proj.template.manager.impl;

import com.healerjean.proj.template.po.UserDemo;
import com.healerjean.proj.template.bo.UserDemoQueryBO;
import com.healerjean.proj.template.dao.UserDemoDao;
import com.healerjean.proj.template.manager.UserDemoManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import javax.annotation.Resource;

/**
 * (UserDemo)Manager接口
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:16
 */
public class UserDemoManagerImpl implements UserDemoManager {
     
     @Resource
     private UserDemoDao userDemoDao;
        
    /**
     * 保存-UserDemo
     *
     * @param po po
     * @return boolean
     */    
    public boolean saveUserDemo(UserDemo po){
        return userDemoDao.save(po);
    }
    
     /**
     * 删除-UserDemo
     *
     * @param id id
     * @return boolean
     */     
    public boolean deleteUserDemoById(Long id){
        //todo
        return false;
    }
    
    /**
     * 更新-UserDemo
     *
     * @param po po
     * @return boolean
     */    
    public boolean updateUserDemo(UserDemo po){
        LambdaUpdateWrapper<UserDemo> updateWrapper = Wrappers.lambdaUpdate(UserDemo.class);
        //todo
        return userDemoDao.update(updateWrapper);
    }
    
     /**
     * 单条主键查询-UserDemo
     *
     * @param id id
     * @return UserDemo
     */     
    public UserDemo queryUserDemoById(Long id){
        return userDemoDao.getById(id);
    }
    
    
     /**
     * 单条查询-UserDemo
     *
     * @param queryBo queryBo
     * @return UserDemo
     */     
    public UserDemo queryUserDemoSingle(UserDemoQueryBO queryBo){
        LambdaQueryWrapper<UserDemo> queryWrapper = Wrappers.lambdaQuery(UserDemo.class);
        //todo
        return userDemoDao.getOne(queryWrapper);
    }
    
    /**
     * 列表查询-UserDemo
     *
     * @param queryBo queryBo
     * @return List<UserDemo>
     */    
    public List<UserDemo> queryUserDemoList(UserDemoQueryBO queryBo){
        LambdaQueryWrapper<UserDemo> queryWrapper = Wrappers.lambdaQuery(UserDemo.class);
        //todo
        return userDemoDao.list(queryWrapper);
    }
   
    /**
     * 分页查询-UserDemo
     *
     * @param queryBo queryBo
     * @return Page<UserDemo>
     */    
    public Page<UserDemo> queryUserDemoPaga(UserDemoQueryBO queryBo){
        LambdaQueryWrapper<UserDemo> queryWrapper = Wrappers.lambdaQuery(UserDemo.class);
        //todo
        Page<UserDemo> pageReq = new Page<>(0, 0, 0);
        return userDemoDao.page(pageReq, queryWrapper);
    }
     
}


```



### 9）`Service.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("Service")

##保存文件（宏定义）
#save("/service", "Service.java")

##包路径（宏定义）
#setPackageSuffix(${servicePath})

import ${boPath}.${tableInfo.name}QueryBO;
import ${boPath}.${tableInfo.name}BO;
import java.util.List;

##表注释（宏定义）
#tableComment("Service接口")
public interface $!{tableName} {

    /**
     * 保存-$!tableInfo.name
     *
     * @param bo bo
     * @return boolean
     */   
    boolean save${tableInfo.name}(${tableInfo.name}BO bo);
    
     /**
     * 删除-$!tableInfo.name
     * 
     * @param bo bo
     * @return boolean
     */  
    boolean delete${tableInfo.name}(${tableInfo.name}BO bo);
    
     /**
     * 更新-$!tableInfo.name
     * 
     * @param bo bo
     * @return boolean
     */    
    boolean update${tableInfo.name}(${tableInfo.name}BO bo);
    
     /**
     * 单条查询-$!tableInfo.name
     * 
     * @param queryBo queryBo
     * @return ${tableInfo.name}BO
     */    
    ${tableInfo.name}BO query${tableInfo.name}Single(${tableInfo.name}QueryBO queryBo);
    
     /**
     * 列表查询-$!tableInfo.name
     * 
     * @param queryBo queryBo
     * @return List<${tableInfo.name}BO>
     */     
    List<${tableInfo.name}BO> query${tableInfo.name}List(${tableInfo.name}QueryBO queryBo);
    
}

```

```java
package com.healerjean.proj.template.service;

import com.healerjean.proj.template.bo.UserDemoQueryBO;
import com.healerjean.proj.template.bo.UserDemoBO;
import java.util.List;

/**
 * (UserDemo)Service接口
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:16
 */
public interface UserDemoService {

    /**
     * 保存-UserDemo
     *
     * @param bo bo
     * @return boolean
     */   
    boolean saveUserDemo(UserDemoBO bo);
    
     /**
     * 删除-UserDemo
     * 
     * @param bo bo
     * @return boolean
     */  
    boolean deleteUserDemo(UserDemoBO bo);
    
     /**
     * 更新-UserDemo
     * 
     * @param bo bo
     * @return boolean
     */    
    boolean updateUserDemo(UserDemoBO bo);
    
     /**
     * 单条查询-UserDemo
     * 
     * @param queryBo queryBo
     * @return UserDemoBO
     */    
    UserDemoBO queryUserDemoSingle(UserDemoQueryBO queryBo);
    
     /**
     * 列表查询-UserDemo
     * 
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */     
    List<UserDemoBO> queryUserDemoList(UserDemoQueryBO queryBo);
    
}


```



### 10）`ServiceImpl.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("ServiceImpl")

##保存文件（宏定义）
#save("/service/impl", "ServiceImpl.java")

##包路径（宏定义）
#setPackageSuffix(${serviceImplPath})
##定义服务名
#set($managerName = $!tool.append($!tool.firstLowerCase($!tableInfo.name), "Manager"))

import ${poPath}.${tableInfo.name};
import ${boPath}.${tableInfo.name}QueryBO;
import ${boPath}.${tableInfo.name}BO;
import ${managerPath}.${tableInfo.name}Manager;
import ${servicePath}.${tableInfo.name}Service;
import ${converterPath}.${tableInfo.name}Converter;
import java.util.List;
import javax.annotation.Resource;

##表注释（宏定义）
#tableComment("Service")
public class $!{tableName} implements $!{tableInfo.name}Service {

    /**
     * $managerName
     */ 
    @Resource
    private $!{tableInfo.name}Manager $managerName;

    /**
     * 保存-$!tableInfo.name
     *
     * @param bo bo
     * @return boolean
     */    
    public boolean save${tableInfo.name}($!{tableInfo.name}BO bo){
        $!{tableInfo.name} po = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}BoToPo(bo);
        return $!{managerName}.save${tableInfo.name}(po);
    }
    
     /**
     * 删除-$!tableInfo.name
     * 
     * @param bo bo
     * @return boolean
     */     
    public boolean delete${tableInfo.name}(${tableInfo.name}BO bo){
        return $!{managerName}.delete${tableInfo.name}ById(bo.getId());
    }
    
     /**
     * 更新-$!tableInfo.name
     * 
     * @param bo bo
     * @return boolean
     */     
    public boolean update${tableInfo.name}(${tableInfo.name}BO bo){
        $!{tableInfo.name} po = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}BoToPo(bo);
        return $!{managerName}.update$!{tableInfo.name}(po);
    }
    
     /**
     * 单条查询-$!tableInfo.name
     * 
     * @param queryBo queryBo
     * @return ${tableInfo.name}BO
     */     
    public ${tableInfo.name}BO query${tableInfo.name}Single(${tableInfo.name}QueryBO queryBo){
        $!{tableInfo.name} po =  $!{managerName}.query${tableInfo.name}Single(queryBo);
        return $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}PoToBo(po);
    }
    
    /**
     * 列表查询-$!tableInfo.name
     * 
     * @param queryBo queryBo
     * @return List<${tableInfo.name}BO>
     */     
    public List<${tableInfo.name}BO> query${tableInfo.name}List(${tableInfo.name}QueryBO queryBo){
        List<$!{tableInfo.name}> pos =  $!{managerName}.query${tableInfo.name}List(queryBo);
        return $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}PoToBoList(pos);        
    }
    
}

```



```java
package com.healerjean.proj.template.service.impl;

import com.healerjean.proj.template.po.UserDemo;
import com.healerjean.proj.template.bo.UserDemoQueryBO;
import com.healerjean.proj.template.bo.UserDemoBO;
import com.healerjean.proj.template.manager.UserDemoManager;
import com.healerjean.proj.template.service.UserDemoService;
import com.healerjean.proj.template.converter.UserDemoConverter;
import java.util.List;
import javax.annotation.Resource;

/**
 * (UserDemo)Service
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:16
 */
public class UserDemoServiceImpl implements UserDemoService {

    /**
     * userDemoManager
     */ 
    @Resource
    private UserDemoManager userDemoManager;

    /**
     * 保存-UserDemo
     *
     * @param bo bo
     * @return boolean
     */    
    public boolean saveUserDemo(UserDemoBO bo){
        UserDemo po = UserDemoConverter.INSTANCE.convertUserDemoBoToPo(bo);
        return userDemoManager.saveUserDemo(po);
    }
    
     /**
     * 删除-UserDemo
     * 
     * @param bo bo
     * @return boolean
     */     
    public boolean deleteUserDemo(UserDemoBO bo){
        return userDemoManager.deleteUserDemoById(bo.getId());
    }
    
     /**
     * 更新-UserDemo
     * 
     * @param bo bo
     * @return boolean
     */     
    public boolean updateUserDemo(UserDemoBO bo){
        UserDemo po = UserDemoConverter.INSTANCE.convertUserDemoBoToPo(bo);
        return userDemoManager.updateUserDemo(po);
    }
    
     /**
     * 单条查询-UserDemo
     * 
     * @param queryBo queryBo
     * @return UserDemoBO
     */     
    public UserDemoBO queryUserDemoSingle(UserDemoQueryBO queryBo){
        UserDemo po =  userDemoManager.queryUserDemoSingle(queryBo);
        return UserDemoConverter.INSTANCE.convertUserDemoPoToBo(po);
    }
    
    /**
     * 列表查询-UserDemo
     * 
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */     
    public List<UserDemoBO> queryUserDemoList(UserDemoQueryBO queryBo){
        List<UserDemo> pos =  userDemoManager.queryUserDemoList(queryBo);
        return UserDemoConverter.INSTANCE.convertUserDemoPoToBoList(pos);        
    }
    
}


```



### 11）`Resource.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("Resource")

##保存文件（宏定义）
#save("/resource", "Resource.java")

##包路径（宏定义）
#setPackageSuffix(${resourcePath})

import ${dtoPath}.${tableInfo.name}DTO;
import ${reqPath}.${tableInfo.name}SaveReq;
import ${reqPath}.${tableInfo.name}QueryReq;
import ${reqPath}.${tableInfo.name}DeleteReq;
import java.util.List;

##表注释（宏定义）
#tableComment("Service接口")
public interface $!{tableName} {

    /**
     * 保存-$!tableInfo.name
     *
     * @param req req
     * @return boolean
     */   
    boolean save${tableInfo.name}(${tableInfo.name}SaveReq req);
    
     /**
     * 删除-$!tableInfo.name
     * 
     * @param req req
     * @return boolean
     */  
    boolean delete${tableInfo.name}(${tableInfo.name}DeleteReq req);
    
     /**
     * 更新-$!tableInfo.name
     * 
     * @param req req
     * @return boolean
     */    
    boolean update${tableInfo.name}(${tableInfo.name}SaveReq req);
    
     /**
     * 单条查询-$!tableInfo.name
     * 
     * @param req req
     * @return ${tableInfo.name}DTO
     */    
    ${tableInfo.name}DTO query${tableInfo.name}Single(${tableInfo.name}QueryReq req);
    
     /**
     * 列表查询-$!tableInfo.name
     * 
     * @param req req
     * @return List<${tableInfo.name}DTO>
     */     
    List<${tableInfo.name}DTO> query${tableInfo.name}List(${tableInfo.name}QueryReq req);
    
}

```

```java
package com.healerjean.proj.template.resource;

import com.healerjean.proj.template.dto.UserDemoDTO;
import com.healerjean.proj.template.req.UserDemoSaveReq;
import com.healerjean.proj.template.req.UserDemoQueryReq;
import com.healerjean.proj.template.req.UserDemoDeleteReq;
import java.util.List;

/**
 * (UserDemo)Service接口
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:16
 */
public interface UserDemoResource {

    /**
     * 保存-UserDemo
     *
     * @param req req
     * @return boolean
     */   
    boolean saveUserDemo(UserDemoSaveReq req);
    
     /**
     * 删除-UserDemo
     * 
     * @param req req
     * @return boolean
     */  
    boolean deleteUserDemo(UserDemoDeleteReq req);
    
     /**
     * 更新-UserDemo
     * 
     * @param req req
     * @return boolean
     */    
    boolean updateUserDemo(UserDemoSaveReq req);
    
     /**
     * 单条查询-UserDemo
     * 
     * @param req req
     * @return UserDemoDTO
     */    
    UserDemoDTO queryUserDemoSingle(UserDemoQueryReq req);
    
     /**
     * 列表查询-UserDemo
     * 
     * @param req req
     * @return List<UserDemoDTO>
     */     
    List<UserDemoDTO> queryUserDemoList(UserDemoQueryReq req);
    
}


```



### 12）`ResourceImpl.java.vm`

```v
##导入宏定义
$!{define.vm}
$!{healerjean.vm}

##设置表后缀（宏定义）
#setTableSuffix("ResourceImpl")

##保存文件（宏定义）
#save("/resource/impl", "ResourceImpl.java")

##包路径（宏定义）
#setPackageSuffix(${resourceImplPath})
##定义服务名
#set($serviceName = $!tool.append($!tool.firstLowerCase($!tableInfo.name), "Service"))

import ${boPath}.${tableInfo.name}QueryBO;
import ${boPath}.${tableInfo.name}BO;
import ${dtoPath}.${tableInfo.name}DTO;
import ${reqPath}.${tableInfo.name}SaveReq;
import ${reqPath}.${tableInfo.name}QueryReq;
import ${reqPath}.${tableInfo.name}DeleteReq;
import ${servicePath}.${tableInfo.name}Service;
import ${resourcePath}.${tableInfo.name}Resource;
import ${converterPath}.${tableInfo.name}Converter;
import java.util.List;
import javax.annotation.Resource;

##表注释（宏定义）
#tableComment("Resource接口")
public class $!{tableName} implements $!{tableInfo.name}Resource {

     /**
     * $serviceName
     */ 
    @Resource
    private $!{tableInfo.name}Service $serviceName;


    /**
     * 保存-$!tableInfo.name
     *
     * @param req req
     * @return boolean
     */   
    public boolean save${tableInfo.name}(${tableInfo.name}SaveReq req){
        $!{tableInfo.name}BO bo = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}SaveReqToBo(req);
        return $!{serviceName}.save${tableInfo.name}(bo);  
    }
    
     /**
     * 删除-$!tableInfo.name
     * 
     * @param req req
     * @return boolean
     */  
    public boolean delete${tableInfo.name}(${tableInfo.name}DeleteReq req){
        $!{tableInfo.name}BO bo = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}DeleteReqToBo(req);
        return $!{serviceName}.delete${tableInfo.name}(bo);  
    }
    
     /**
     * 更新-$!tableInfo.name
     * 
     * @param req req
     * @return boolean
     */    
    public boolean update${tableInfo.name}(${tableInfo.name}SaveReq req){
        $!{tableInfo.name}BO bo = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}SaveReqToBo(req);
        return $!{serviceName}.update${tableInfo.name}(bo);  
    }
    
     /**
     * 单条查询-$!tableInfo.name
     * 
     * @param req req
     * @return ${tableInfo.name}DTO
     */    
    public ${tableInfo.name}DTO query${tableInfo.name}Single(${tableInfo.name}QueryReq req){
        $!{tableInfo.name}QueryBO queryBo = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}QueryReqToBo(req);
        $!{tableInfo.name}BO bo = $!{serviceName}.query${tableInfo.name}Single(queryBo);  
        return $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}BoToDto(bo);  
    }
    
     /**
     * 列表查询-$!tableInfo.name
     * 
     * @param req req
     * @return List<${tableInfo.name}DTO>
     */     
    public List<${tableInfo.name}DTO> query${tableInfo.name}List(${tableInfo.name}QueryReq req){
        $!{tableInfo.name}QueryBO queryBo = $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}QueryReqToBo(req);
        List<$!{tableInfo.name}BO> bos = $!{serviceName}.query${tableInfo.name}List(queryBo);  
        return $!{tableInfo.name}Converter.INSTANCE.convert$!{tableInfo.name}BoToDtoList(bos);  
    }
    
}

```

```java
package com.healerjean.proj.template.resource.impl;

import com.healerjean.proj.template.bo.UserDemoQueryBO;
import com.healerjean.proj.template.bo.UserDemoBO;
import com.healerjean.proj.template.dto.UserDemoDTO;
import com.healerjean.proj.template.req.UserDemoSaveReq;
import com.healerjean.proj.template.req.UserDemoQueryReq;
import com.healerjean.proj.template.req.UserDemoDeleteReq;
import com.healerjean.proj.template.service.UserDemoService;
import com.healerjean.proj.template.resource.UserDemoResource;
import com.healerjean.proj.template.converter.UserDemoConverter;
import java.util.List;
import javax.annotation.Resource;

/**
 * (UserDemo)Resource接口
 *
 * @author zhangyujin
 * @date 2023-06-17 22:00:16
 */
public class UserDemoResourceImpl implements UserDemoResource {

     /**
     * userDemoService
     */ 
    @Resource
    private UserDemoService userDemoService;


    /**
     * 保存-UserDemo
     *
     * @param req req
     * @return boolean
     */   
    public boolean saveUserDemo(UserDemoSaveReq req){
        UserDemoBO bo = UserDemoConverter.INSTANCE.convertUserDemoSaveReqToBo(req);
        return userDemoService.saveUserDemo(bo);  
    }
    
     /**
     * 删除-UserDemo
     * 
     * @param req req
     * @return boolean
     */  
    public boolean deleteUserDemo(UserDemoDeleteReq req){
        UserDemoBO bo = UserDemoConverter.INSTANCE.convertUserDemoDeleteReqToBo(req);
        return userDemoService.deleteUserDemo(bo);  
    }
    
     /**
     * 更新-UserDemo
     * 
     * @param req req
     * @return boolean
     */    
    public boolean updateUserDemo(UserDemoSaveReq req){
        UserDemoBO bo = UserDemoConverter.INSTANCE.convertUserDemoSaveReqToBo(req);
        return userDemoService.updateUserDemo(bo);  
    }
    
     /**
     * 单条查询-UserDemo
     * 
     * @param req req
     * @return UserDemoDTO
     */    
    public UserDemoDTO queryUserDemoSingle(UserDemoQueryReq req){
        UserDemoQueryBO queryBo = UserDemoConverter.INSTANCE.convertUserDemoQueryReqToBo(req);
        UserDemoBO bo = userDemoService.queryUserDemoSingle(queryBo);  
        return UserDemoConverter.INSTANCE.convertUserDemoBoToDto(bo);  
    }
    
     /**
     * 列表查询-UserDemo
     * 
     * @param req req
     * @return List<UserDemoDTO>
     */     
    public List<UserDemoDTO> queryUserDemoList(UserDemoQueryReq req){
        UserDemoQueryBO queryBo = UserDemoConverter.INSTANCE.convertUserDemoQueryReqToBo(req);
        List<UserDemoBO> bos = userDemoService.queryUserDemoList(queryBo);  
        return UserDemoConverter.INSTANCE.convertUserDemoBoToDtoList(bos);  
    }
    
}


```



# 三、代码生成

## 1、`mybatis-plus`

### 1）`pom.xml`

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
        <version>${com-alibaba-druid.version}</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.45</version>
    </dependency>

    <!--mybatis-plus-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>${mybatis-plus-boot-starter.version}</version>
    </dependency>

    <!--h2database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.1.214</version>
        <scope>test</scope>
    </dependency>

    <!--mybatis-代码生成-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>3.4.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.velocity</groupId>
        <artifactId>velocity-engine-core</artifactId>
        <version>2.3</version>
    </dependency>
```





### 2）`MybatisPlusGenerator`

```java
package com.healerjean.proj.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.healerjean.proj.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2023/6/17  12:21.
 */
@Slf4j
@Component
public class MybatisPlusGenerator {

    /**
     * 默认项目Key
     */
    private static final String PROJECT_KEY = "hlj-project";
    /**
     * 需要替换的目录
     */
    private static final String REPLACE_DIRECTORY = PROJECT_KEY + "-web";

    /**
     * 生成表
     *
     * @param tableNameArray 表名列表
     * @param projectPath    项目路径
     */
    public void generator(String[] tableNameArray, String projectPath, DataSourceConfig sourceConfig) {
        if (StringUtils.isBlank(projectPath)) {
            projectPath = System.getProperty("user.dir");
        }
        log.info("projectPath:{},tableNameArray:{}", projectPath, JsonUtils.toString(tableNameArray));
        if (projectPath.contains(REPLACE_DIRECTORY)) {
            // 如果是从单元测试执行,则该目录会定位到web层,需要替换掉
            projectPath = projectPath.replace(REPLACE_DIRECTORY, StringUtils.EMPTY);
        }
        for (ProjectModuleEnum projectModule : ProjectModuleEnum.values()) {
            StringBuilder pathBuilder = new StringBuilder(projectPath);
            pathBuilder.append("/").append(PROJECT_KEY).append("-").append(projectModule.getName());
            // 代码生成器
            AutoGenerator autoGenerator = new AutoGenerator();
            // 全局配置
            autoGenerator.setGlobalConfig(createGlobalConfig(pathBuilder.toString()));
            // 数据源配置
            autoGenerator.setDataSource(sourceConfig);
            // 包配置
            PackageConfig packageConfig = new PackageConfig();
            packageConfig.setParent("com")
                    .setService("dao.service")
                    .setServiceImpl("dao.service.impl")
                    .setMapper("dao.mapper")
                    .setEntity("domain.po");
            autoGenerator.setPackageInfo(packageConfig);
            // 自定义配置
            autoGenerator.setCfg(createInjectionConfig(pathBuilder.toString(), projectModule, packageConfig));
            // 模版配置
            autoGenerator.setTemplate(createTemplateConfig(projectModule));
            // 策略配置
            autoGenerator.setStrategy(createStrategyConfig(tableNameArray, packageConfig.getModuleName()));
            autoGenerator.setTemplateEngine(new VelocityTemplateEngine());
            autoGenerator.execute();
        }
    }

    /**
     * 数据源配置
     *
     * @return 数据源配置
     */
    private static DataSourceConfig createDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://[ip]:[port]/[db_name]?characterEncoding=utf-8&useUnicode=true&autoReconnect=true&connectTimeout=3000&initialTimeout=1&socketTimeout=5000&useSSL=false&serverTimezone=CTT");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("");
        dataSourceConfig.setPassword("");
        return dataSourceConfig;
    }

    /**
     * 策略配置
     *
     * @param tableNameArray 表名
     * @param moduleName     模块名
     * @return 策略配置
     */
    private static StrategyConfig createStrategyConfig(String[] tableNameArray, String moduleName) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableNameArray);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setEntityColumnConstant(true);
        strategy.setTablePrefix(moduleName + "_");
        return strategy;
    }

    /**
     * 模版配置
     *
     * @return 模版配置
     */
    private static TemplateConfig createTemplateConfig(ProjectModuleEnum projectModuleEnum) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        if (ProjectModuleEnum.DOMAIN.equals(projectModuleEnum)) {
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
            templateConfig.setMapper(null);
            templateConfig.setXml(null);
        } else if (ProjectModuleEnum.DAO.equals(projectModuleEnum)) {
            templateConfig.setEntity(null);
        }
        return templateConfig;
    }

    /**
     * 自定义配置
     *
     * @return 自定义配置
     */
    private static InjectionConfig createInjectionConfig(String projectPath, ProjectModuleEnum projectModuleEnum, PackageConfig packageConfig) {
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        if (ProjectModuleEnum.DOMAIN.equals(projectModuleEnum)) {
            return injectionConfig;
        }
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String xmlPath = packageConfig.getParent().replace(StringPool.DOT, StringPool.SLASH) + "/" + packageConfig.getMapper().replace(StringPool.DOT, StringPool.SLASH);
                // 自定义输出文件名,如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + xmlPath + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }

    /**
     * 全局配置
     *
     * @param projectPath 项目路径
     * @return 全局配置
     */
    private static GlobalConfig createGlobalConfig(String projectPath) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("example author");
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(true);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setIdType(IdType.AUTO);
        globalConfig.setActiveRecord(true);
        globalConfig.setServiceName("%sDao");
        globalConfig.setServiceImplName("%sDaoImpl");
        return globalConfig;
    }

    /**
     * 项目模块枚举
     */
    @Getter
    @AllArgsConstructor
    public enum ProjectModuleEnum {
        /**
         * Domain
         */
        DOMAIN("domain"),
        /**
         * Dao
         */
        DAO("dao");
        /**
         * 项目模块名称
         */
        private final String name;
    }
}


```



### 3）`MybatisPlusGeneratorTest`

```java
package com.healerjean.proj.db;

/**
 * @author zhangyujin
 * @date 2023/6/17  12:20.
 */

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.healerjean.proj.base.BaseJunit5SpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * MybatisPlusGenerator测试
 *
 * @author zhanghanlin6
 * @date 2023-04-13 15:54
 */
@Slf4j
public class MybatisPlusGeneratorTest extends BaseJunit5SpringTest {
    /**
     * MybatisPlus生成器
     */
    @Resource
    private MybatisPlusGenerator mybatisPlusGenerator;
    /**
     * TABLES
     */
    private final static String[] TABLES = new String[]{"vender_premium_rate_model"};

    /**
     * 该单元测试不能提交线上,会影响单元测试流水线执行
     */
    @Test
    public void generator() {
        // 内存数据库
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:h2:mem:healerjean");
        dataSourceConfig.setDriverName("org.h2.Driver");

        // 本地数据库
        // dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/healerjean?characterEncoding=utf-8&useUnicode=true&autoReconnect=true&connectTimeout=3000&initialTimeout=1&socketTimeout=5000&useSSL=false&serverTimezone=CTT");
        // dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        // dataSourceConfig.setUsername("root");
        // dataSourceConfig.setPassword("12345678");
        mybatisPlusGenerator.generator(TABLES, null, dataSourceConfig);
    }
}
```



### 4）模版修改

#### a、`entity.java.vm`

```
src/test/resources/templates/entity.java.vm
```

```java
package ${package.Entity};

#foreach($pkg in ${table.importPackages})
import ${pkg};
#end
#if(${swagger2})
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
#end
#if(${entityLombokModel})
import lombok.Data;
import lombok.EqualsAndHashCode;
#if(${chainModel})
import lombok.experimental.Accessors;
#end
#end

/**
 * <p>
 * ${table.comment}
 * </p>
 *
 * @author ${author}
 * @date ${date}
 */
#if(${entityLombokModel})
@Data
  #if(${superEntityClass})
@EqualsAndHashCode(callSuper = true)
  #else
@EqualsAndHashCode(callSuper = false)
  #end
  #if(${chainModel})
@Accessors(chain = true)
  #end
#end
#if(${table.convert})
@TableName("${table.name}")
#end
#if(${swagger2})
@ApiModel(value="${entity}对象", description="$!{table.comment}")
#end
#if(${superEntityClass})
public class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {
#elseif(${activeRecord})
public class ${entity} extends Model<${entity}> {
#else
public class ${entity} implements Serializable {
#end

#if(${entitySerialVersionUID})
    private static final long serialVersionUID = 1L;
#end
## ----------  BEGIN 字段循环遍历  ----------
#foreach($field in ${table.fields})

#if(${field.keyFlag})
#set($keyPropertyName=${field.propertyName})
#end
#if("$!field.comment" != "")
  #if(${swagger2})
    @ApiModelProperty(value = "${field.comment}")
  #else
    /**
     * ${field.comment}
     */
  #end
#end
#if(${field.keyFlag})
## 主键
  #if(${field.keyIdentityFlag})
    @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
  #elseif(!$null.isNull(${idType}) && "$!idType" != "")
    @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
  #elseif(${field.convert})
    @TableId("${field.annotationColumnName}")
  #end
## 普通字段
#elseif(${field.fill})
## -----   存在字段填充设置   -----
  #if(${field.convert})
    @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
  #else
    @TableField(fill = FieldFill.${field.fill})
  #end
#elseif(${field.convert})
    @TableField("${field.annotationColumnName}")
#end
## 乐观锁注解
#if(${versionFieldName}==${field.name})
    @Version
#end
## 逻辑删除注解
#if(${logicDeleteFieldName}==${field.name})
    @TableLogic
#end
    private ${field.propertyType} ${field.propertyName};
#end
## ----------  END 字段循环遍历  ----------

#if(!${entityLombokModel})
#foreach($field in ${table.fields})
  #if(${field.propertyType.equals("boolean")})
    #set($getprefix="is")
  #else
    #set($getprefix="get")
  #end

    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

  #if(${chainModel})
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
  #else
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
  #end
        this.${field.propertyName} = ${field.propertyName};
  #if(${chainModel})
        return this;
  #end
    }
#end
## --foreach end---
#end
## --end of #if(!${entityLombokModel})--

#if(${entityColumnConstant})
  #foreach($field in ${table.fields})
    /**
     * ${field.name}
     */
    public static final String ${field.name.toUpperCase()} = "${field.name}";

  #end
#end
#if(${activeRecord})
    @Override
    protected Serializable pkVal() {
  #if(${keyPropertyName})
        return this.${keyPropertyName};
  #else
        return null;
  #end
    }

#end
#if(!${entityLombokModel})
    @Override
    public String toString() {
        return "${entity}{" +
  #foreach($field in ${table.fields})
    #if($!{foreach.index}==0)
        "${field.propertyName}=" + ${field.propertyName} +
    #else
        ", ${field.propertyName}=" + ${field.propertyName} +
    #end
  #end
        "}";
    }
#end
}

```



## 2、`mybatis`

> 代码：`healerjean-code-gen`













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
		id: 'AAAAAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->



