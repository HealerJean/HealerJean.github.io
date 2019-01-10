---
title: Mysql中Update和Delete返回值的问题
date: 2018-01-09 03:33:00
tags: 
- database
category: 
- database
description: Mysql中Update和Delete返回值的问题
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

我们都知道update和delete语句返回的是我们修改的行数，那么在jpa和mybatis中是如果实现的呢，请看下面

#### 当然我们也可以设置为void，这是无所谓的，因为有了返回值（int ,Integer都可以），我不要也可以啊，这与我们自己的业务逻辑有关系


## 1、jpa 


```java


public interface DemoEntityRepository extends CrudRepository<DemoEntity,Long> {
    
    

    /**
     * 更新字段，判断更新了几条数据 ,没有 resultType
     */
    @Modifying
    @Query(value = "update demo_entity d set d.age = d.age +1 where d.name =?1" ,nativeQuery = true)
    Integer jpaUpdateByName( String name) ;



    /**
     * 删除字段,如果是对象删除则不能使用我们 d.*
     * 这和mysql有很大的不一样，mysql 如果使用了变量 d，delete 不使用 d.*是错误的
     * @param name
     * @return
     */
    @Modifying
    @Query(value = "delete d.*  from demo_entity d where d.name = :name",nativeQuery = true)
    Integer jpaDeletByName(@Param("name") String name) ;
//
//    @Modifying 对象映射
//    @Query(value = "delete  from DemoEntity d where d.name = :name")
//    int jpaDeletByName(@Param("name") String name) ;

}


```


## 2、mybatis 

#### 会发现不能写resultType，如果加上就错了，即使这样，我们还可以让他返回int类型的数据

### 3.1、mapper

```java


    /**
     * 更新字段，判断更新了几条数据 ,没有 resultType
     */
    Integer mybatisUpdateByName(String name) ;

    Integer mybatisDeletByName(String name) ;


```


### 3.2、xml


```jsva


    <update id="mybatisUpdateByName">
        update demo_entity d set d.age = d.age +1 where d.name = #{name}
    </update>


    <delete id="mybatisDeletByName">
        delete d.* from demo_entity d where d.name = #{name}
    </delete>


```


### 3、代码

### com.hlj.sql



<br/><br/><br/>
<font color="red"> 感兴趣的，欢迎添加博主微信， </font><br/>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
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
		id: 'ZCGRKNA0JL4gXoQ2',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

