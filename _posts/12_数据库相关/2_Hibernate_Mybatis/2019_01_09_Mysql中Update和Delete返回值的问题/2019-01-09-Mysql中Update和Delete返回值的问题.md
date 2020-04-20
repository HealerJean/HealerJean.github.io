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

