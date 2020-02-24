---
title: BO_DTO_VO_AO_POJO_Query
date: 2019-02-20 03:33:00
tags: 
- Experience
category: 
- Experience
description: BO_DTO_VO_AO_POJO_Query
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



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





<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>       

   



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
		id: 'Xx93Na1HrUYbLOf8',
    });
    gitalk.render('gitalk-container');
</script> 


