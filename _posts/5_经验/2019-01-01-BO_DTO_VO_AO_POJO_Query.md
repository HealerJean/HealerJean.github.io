---
title: BO_DTO_VO_AO_POJO_Query
date: 2019-02-20 03:33:00
tags: 
- Experience
category: 
- Experience
description: BO_DTO_VO_AO_POJO_Query
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





```
• DO（Data Object）：此对象与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。
• DTO（Data Transfer Object）：数据传输对象，Service 或 Manager 向外传输的对象。
• BO（Business Object）：业务对象，由 Service 层输出的封装业务逻辑的对象。
• AO（Application Object）：应用对象，在 Web 层与 Service 层之间抽象的复用对象模型，极为贴
近展示层，复用度不高。
• VO（View Object）：显示层对象，通常是 Web 向模板渲染引擎层传输的对象。
• Query：数据查询对象，各层接收上层的查询请求。注意超过 2 个参数的查询封装，禁止使用 Map 类
来传输。




- 数据对象：xxxDO，xxx即为数据表名。
- 数据传输对象：xxxDTO，xxx为业务领域相关的名称。
- 展示对象：xxxVO，xxx一般为网页名称。
- POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。

```



#### 2.1.3、 BO、DTO、VO、POJO、Query  



- **VO（ View Object）：显示层对象，通常是Web向模板渲染引擎层传输的对象。**

  

```java
public List<UserVO> getUserVOs(UserQuery userQuery){
    
   //DTO转VO
    BeanUils.dtoToUserVO(userservice.getUserDTOs());
}

```

- **DTO（ Data Transfer Object）：数据传输对象，Service或Manager向外传输的对象。**

  

```java
 List<UserDTO> getUserDTOs(UserQuery userQuery){
     
      //BO转DTO,也可能DO转DTO
    BeanUils.boToUserDTO(userservice.getUserBOs());
 }

```

- **BO（ Business Object）：业务对象。 由Service层输出的封装业务逻辑的对象。**

  

```java
Service内部的私有代码 
List<UserBO> getUserBOs(UserQuery userQuery){

   //DO转VO
    BeanUils.doToUserBO(userdao.getUserDOs());
}
```



- **DO（ Data Object）：与数据库表结构一一对应，通过DAO层向上传输数据源对象。**

  

```java
List<UserDO> getUsers(UserQuery userQuery);
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
		id: 'Xx93Na1HrUYbLOf8',
    });
    gitalk.render('gitalk-container');
</script> 


