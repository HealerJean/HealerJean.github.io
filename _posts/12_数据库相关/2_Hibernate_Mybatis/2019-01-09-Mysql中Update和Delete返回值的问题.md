---
title: Mysql中Update和Delete返回值的问题
date: 2018-01-09 03:33:00
tags: 
- Database
category: 
- Database
description: Mysql中Update和Delete返回值的问题
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            




# 1、`mybatis `

> 会发现不能写 `resultType` ，如果加上就错了，即使这样，我们还可以让他返回int类型的数据

### 3.1、`mapper`

```java
/**
 * 更新字段，判断更新了几条数据 ,没有 resultType
 */
Integer mybatisUpdateByName(String name) ;

Integer mybatisDeletByName(String name) ;

```


### 3.2、`xml`


```xml
<update id="mybatisUpdateByName">
  update demo_entity d set d.age = d.age +1 where d.name = #{name}
</update>

<delete id="mybatisDeletByName">
  delete d.* from demo_entity d where d.name = #{name}
</delete>

```



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
		id: 'ZCGRKNA0JL4gXoQ2',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

