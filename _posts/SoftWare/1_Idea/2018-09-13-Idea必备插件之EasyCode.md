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

### 1）`PO.java`

```java
##导入宏定义
$!{define.vm}

##保存文件（宏定义）
#save("/po", ".java")

##包路径（宏定义）
#setPackageSuffix("com.healerjean.proj.template.po")

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


```java
package com.healerjean.proj.template.po;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (UserDemo)表实体类
 *
 * @author zhangyujin
 * @date 2023-06-17 15:57:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDemo extends Model<UserDemo> {
   
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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


    
    /**
     * id
     */
    public static final String ID = "id";
    
    /**
     * name
     */
    public static final String NAME = "name";
    
    /**
     * age
     */
    public static final String AGE = "age";
    
    /**
     * phone
     */
    public static final String PHONE = "phone";
    
    /**
     * email
     */
    public static final String EMAIL = "email";
    
    /**
     * start_time
     */
    public static final String START_TIME = "start_time";
    
    /**
     * end_time
     */
    public static final String END_TIME = "end_time";
    
    /**
     * valid_flag
     */
    public static final String VALID_FLAG = "valid_flag";
    
    /**
     * create_time
     */
    public static final String CREATE_TIME = "create_time";
    
    /**
     * update_time
     */
    public static final String UPDATE_TIME = "update_time";

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



### 2）`Mapper.java`

```java
##导入宏定义
$!{define.vm}

##设置表后缀（宏定义）
#setTableSuffix("Mapper")

##保存文件（宏定义）
#save("/mapper", "Mapper.java")

##包路径（宏定义）
#setPackageSuffix("com.healerjean.proj.template.mapper")

import com.healerjean.proj.template.po.$!tableInfo.name;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

##表注释（宏定义）
#tableComment("表数据库访问层")
public interface $!{tableInfo.name}Mapper extends BaseMapper<$!{tableInfo.name}> {


}

```



```java
package com.healerjean.proj.template.mapper;

import com.healerjean.proj.template.po.UserDemo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * (UserDemo)表数据库访问层
 *
 * @author zhangyujin
 * @date 2023-06-17 15:57:33
 */
public interface UserDemoMapper extends BaseMapper<UserDemo> {


}


```



### 3）`Mapper.xml`

```java
##引入mybatis支持
$!{mybatisSupport.vm}

##设置保存名称与保存位置
$!callback.setFileName($tool.append($!{tableInfo.name}, "Mapper.xml"))
$!callback.setSavePath($tool.append($modulePath, "/src/main/resources/mapper"))

##拿到主键
#if(!$tableInfo.pkColumn.isEmpty())
    #set($pk = $tableInfo.pkColumn.get(0))
#end

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.healerjean.proj.template.mapper.$!{tableInfo.name}Mapper">

    <resultMap id="BaseResultMap" type="com.healerjean.proj.template.po.$!{tableInfo.name}">
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
<mapper namespace="com.healerjean.proj.template.mapper.UserDemoMapper">

    <resultMap id="BaseResultMap" type="com.healerjean.proj.template.po.UserDemo">
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



### 4）`Dao.java`

```java
##导入宏定义
$!{define.vm}

##设置表后缀（宏定义）
#setTableSuffix("Dao")

##保存文件（宏定义）
#save("/dao", "Dao.java")

##包路径（宏定义）
#setPackageSuffix("com.healerjean.proj.template.dao")

import com.baomidou.mybatisplus.extension.service.IService;
import com.healerjean.proj.template.po.$!tableInfo.name;

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
 * @date 2023-06-17 15:57:33
 */
public interface UserDemoDao extends IService<UserDemo> {

}


```


### 5）`DaoImpl.java`

```java
##导入宏定义
$!{define.vm}

##设置表后缀（宏定义）
#setTableSuffix("DaoImpl")

##保存文件（宏定义）
#save("/dao/impl", "DaoImpl.java")

##包路径（宏定义）
#setPackageSuffix("com.healerjean.proj.template.dao.impl")

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healerjean.proj.template.po.$!{tableInfo.name};
import com.healerjean.proj.template.dao.$!{tableInfo.name}Dao;
import com.healerjean.proj.template.mapper.$!{tableInfo.name}Mapper;
import org.springframework.stereotype.Service;

##表注释（宏定义）
#tableComment("Dao实现类")
@Service
public class $!{tableName} extends ServiceImpl<$!{tableInfo.name}Mapper, $!{tableInfo.name}> implements $!{tableInfo.name}Dao {

}

```

```java
package com.healerjean.proj.template.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healerjean.proj.template.po.UserDemo;
import com.healerjean.proj.template.dao.UserDemoDao;
import com.healerjean.proj.template.mapper.UserDemoMapper;
import org.springframework.stereotype.Service;

/**
 * (UserDemo)Dao实现类
 *
 * @author zhangyujin
 * @date 2023-06-17 15:57:33
 */
@Service
public class UserDemoDaoImpl extends ServiceImpl<UserDemoMapper, UserDemo> implements UserDemoDao {

}


```



### 6）`Controller.java`


```java
##定义初始变量
#set($tableName = $tool.append($tableInfo.name, "Controller"))
##设置回调
$!callback.setFileName($tool.append($tableName, ".java"))
$!callback.setSavePath($tool.append($tableInfo.savePath, "/controller"))
##拿到主键
#if(!$tableInfo.pkColumn.isEmpty())
    #set($pk = $tableInfo.pkColumn.get(0))
#end

#if($tableInfo.savePackageName)package $!{tableInfo.savePackageName}.#{end}controller;

import $!{tableInfo.savePackageName}.service.$!{tableInfo.name}Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HealerJean
 * @date 2020-04-03 11:40:02
 * @Description
 */
@RestController
@RequestMapping("api/$!tool.firstLowerCase($tableInfo.name)")
public class $!{tableName} {
    
    @Autowired
    private $!{tableInfo.name}Service $!tool.firstLowerCase($tableInfo.name)Service;

}
```


```java
package com.controller;

import com.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HealerJean
 * @date 2020-04-03 11:40:02
 * @Description
 */
@RestController
@RequestMapping("api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

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



