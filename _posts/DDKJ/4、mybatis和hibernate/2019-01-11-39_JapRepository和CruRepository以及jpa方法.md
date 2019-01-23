---
title: JapRepository和CruRepository以及jpa方法
date: 2019-01-11 03:33:00
tags: 
- database
category: 
- database
description: JapRepository和CruRepository以及jpa方法
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

用了这么久springboot springdata jpa了，是时候简单总结一下下了


## 1、CrudRepository与JpaRepository的不同

其实用起来一模一样

### 1.1、继承关系

PagingAndSortingRepository 继承 CrudRepository
JpaRepository 继承 PagingAndSortingRepository
<font  color="red" size="4">所以，我们以后一般用JpaRepository   </font>



### 12、使用关系

CrudRepository 提供基本的增删改查；<br/>
PagingAndSortingRepository 提供分页和排序方法；<br/>
JpaRepository 提供JPA需要的方法。<br/>


## 2、方法集锦

<table border="1" cellspacing="1" cellpadding="1" ><tbody><tr><td><span style="font-size:18px;">关键字</span></td>
<td><span style="font-size:18px;">方法命名</span></td>
<td><span style="font-size:18px;">sql where字句</span></td>
</tr><tr><td><span style="font-size:18px;">And</span></td>
<td><span style="font-size:18px;">findByNameAndPwd</span></td>
<td><span style="font-size:18px;">where name= ? and pwd =?</span></td>
</tr><tr><td><span style="font-size:18px;">Or</span></td>
<td><span style="font-size:18px;">findByNameOrSex</span></td>
<td><span style="font-size:18px;">where name= ? or sex=?</span></td>
</tr><tr><td><span style="font-size:18px;">Is,Equals</span></td>
<td><span style="font-size:18px;">findById,findByIdEquals</span></td>
<td><span style="font-size:18px;">where id= ?</span></td>
</tr><tr><td><span style="font-size:18px;">Between</span></td>
<td><span style="font-size:18px;">findByIdBetween</span></td>
<td><span style="font-size:18px;">where id between ? and ?</span></td>
</tr><tr><td><span style="font-size:18px;">LessThan</span></td>
<td><span style="font-size:18px;">findByIdLessThan</span></td>
<td><span style="font-size:18px;">where id &lt; ?</span></td>
</tr><tr><td><span style="font-size:18px;">LessThanEquals</span></td>
<td><span style="font-size:18px;">findByIdLessThanEquals</span></td>
<td><span style="font-size:18px;">where id &lt;= ?</span></td>
</tr><tr><td><span style="font-size:18px;">GreaterThan</span></td>
<td><span style="font-size:18px;">findByIdGreaterThan</span></td>
<td><span style="font-size:18px;">where id &gt; ?</span></td>
</tr><tr><td><span style="font-size:18px;">GreaterThanEquals</span></td>
<td><span style="font-size:18px;">findByIdGreaterThanEquals</span></td>
<td><span style="font-size:18px;">where id &gt; = ?</span></td>
</tr><tr><td><span style="font-size:18px;">After</span></td>
<td><span style="font-size:18px;">findByIdAfter</span></td>
<td><span style="font-size:18px;">where id &gt; ?</span></td>
</tr><tr><td><span style="font-size:18px;">Before</span></td>
<td><span style="font-size:18px;">findByIdBefore</span></td>
<td><span style="font-size:18px;">where id &lt; ?</span></td>
</tr><tr><td><span style="font-size:18px;">IsNull</span></td>
<td><span style="font-size:18px;">findByNameIsNull</span></td>
<td><span style="font-size:18px;">where name is null</span></td>
</tr><tr><td><span style="font-size:18px;">isNotNull,NotNull</span></td>
<td><span style="font-size:18px;">findByNameNotNull</span></td>
<td><span style="font-size:18px;">where name is not null</span></td>
</tr><tr><td><span style="font-size:18px;">Like</span></td>
<td><span style="font-size:18px;">findByNameLike</span></td>
<td><span style="font-size:18px;">where name like ?</span></td>
</tr><tr><td><span style="font-size:18px;">NotLike</span></td>
<td><span style="font-size:18px;">findByNameNotLike</span></td>
<td><span style="font-size:18px;">where name not like ?</span></td>
</tr><tr><td>
<p><span style="font-size:18px;">StartingWith</span></p>
</td>
<td><span style="font-size:18px;">findByNameStartingWith</span></td>
<td><span style="font-size:18px;">where name like '?%'</span></td>
</tr><tr><td><span style="font-size:18px;">EndingWith</span></td>
<td><span style="font-size:18px;">findByNameEndingWith</span></td>
<td><span style="font-size:18px;">where name like '%?'</span></td>
</tr><tr><td><span style="font-size:18px;">Containing</span></td>
<td><span style="font-size:18px;">findByNameContaining</span></td>
<td><span style="font-size:18px;">where name like '%?%'</span></td>
</tr><tr><td><span style="font-size:18px;">OrderBy</span></td>
<td><span style="font-size:18px;">findByIdOrderByXDesc</span></td>
<td><span style="font-size:18px;">where id=? order by x desc</span></td>
</tr><tr><td><span style="font-size:18px;">Not</span></td>
<td><span style="font-size:18px;">findByNameNot</span></td>
<td><span style="font-size:18px;">where name &lt;&gt; ?</span></td>
</tr><tr><td><span style="font-size:18px;">In</span></td>
<td><span style="font-size:18px;">findByIdIn(Collection&lt;?&gt; c)</span></td>
<td><span style="font-size:18px;">where id in (?)</span></td>
</tr><tr><td><span style="font-size:18px;">NotIn</span></td>
<td><span style="font-size:18px;">findByIdNotIn(Collection&lt;?&gt; c)</span></td>
<td><span style="font-size:18px;">where id not &nbsp;in (?)</span></td>
</tr><tr><td><span style="font-size:18px;">True</span></td>
<td>
<p><span style="font-size:18px;">findByAaaTue</span></p>
</td>
<td><span style="font-size:18px;">where aaa = true</span></td>
</tr><tr><td><span style="font-size:18px;">False</span></td>
<td><span style="font-size:18px;">findByAaaFalse</span></td>
<td><span style="font-size:18px;">where aaa = false</span></td>
</tr><tr><td><span style="font-size:18px;">IgnoreCase</span></td>
<td><span style="font-size:18px;">findByNameIgnoreCase</span></td>
<td><span style="font-size:18px;">where UPPER(name)=UPPER(?)</span></td>
</tr></tbody></table>



### 2.1、count

|count|sql|解释|
|---|----|---|
|1| Long countByName(String name); |通过名字查询|
|2| Long countAllByName(String name);|通过名字查询|
|3|  Long countBy();|查询库中所有的|
|4|  long count();|查询库中所有的,注意这里为long|




####  2.1.2、测试


```java

public interface DemoEntityJapCruRepository extends JpaRepository<DemoEntity,Long> {

  
    Long countByName(String name);
    Long countAllByName(String name);
    Long countBy();
    long count();
    
}
```

### 2.2、exists 检测主键Id存在不存在


```java
    boolean exists(Long id);

```

### 2.3、返回结果为对象

#### 2.3.1、返回Date类型


```java

    @Query("select d.cdate from  DemoEntity d  where d.id = :id")
    Date findDate(@Param("id") Long id);
    
```

#### 2.3.2、返回实体对象

下面这种方式不可以使用原生的sql语句，必须使用Hibernate 对象语法。所以尽量使用mybatis 
****
```java

@Query("select  new com.hlj.data.res.RspDemoModel(d.id,d.name,d.age)  from DemoEntity  d where d.id  = :id")
RspDemoModel findDtoModel(@Param("id") Long id) ;


```



## 3、getOne和上面的findOne区别

使用中get和上面的find在Jpa方法中没什么区别的，比如：getByNameContaining也就是说可以用下吗的get去替代上面的find<br/>

但是如果是getOne和findOne就会有一些问题

<font  color="red" size="4">  
findOne()是返回的是一个实体对象，查不到的时候会返回null。
getOne()是返回的一个对象的引用，也是是代理对象，查不到会抛异常。
 </font>



SpringBoot版本1.5.4

```xml

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>


```

### 3.1、测试Repository


```java
public interface DemoEntityJapRepository extends JpaRepository<DemoEntity,Long> {

}


public interface DemoEntityCruRepository extends CrudRepository<DemoEntity,Long> {
}
```

### 3.2、service


```java
接口

    DemoEntity findOrGet (String type , String which , Long id);
实现类



    @Override
    public DemoEntity findOrGet(String type , String which , Long id) {
        DemoEntity  demoEntitie = null ;
        if(StringUtils.equals("jpa", type)){
            if(StringUtils.equals("find",which )){
                demoEntitie = demoEntityJapRepository.findOne(id) ;
            }else if(StringUtils.equals("get",which )){
                demoEntitie = demoEntityJapRepository.getOne(id) ;
            }
        }else if(StringUtils.equals("cru",type )){
            if(StringUtils.equals("find",which )){
                demoEntitie = ( demoEntityCruRepository.findOne(id));
            }else if(StringUtils.equals("get",which )){
                //下面这种不存在的
//                demoEntitie =  demoEntityCruRepository.getOne(id);
            }
        }
        System.out.println(demoEntitie);
        return  demoEntitie ; //jpa getOne(代理对象能够获取结果但是不能传递到前台)

    }


```

### 3.3、controller

```java

    //http://localhost:8080/demo/jpa/findOrGet?type=jpa&which=get&id=17
    @ApiOperation(notes = "所有Demo实体类",
            value = "所有Demo实体类",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "jpa cru",required = true,dataType = "string",paramType = "query")
    })
    @GetMapping("findOrGet")
    @ResponseBody
    public ResponseBean  findOrGet(String type,String which, Long id){
        try {
            return ResponseBean.buildSuccess(demo02JapMethodService.findOrGet(type,which,id));
        }catch (AppException e){
            ExceptionLogUtils.log(e,this.getClass() );
            return  ResponseBean.buildFailure(e.getCode(),e.getMessage());
        }catch (Exception e){
            ExceptionLogUtils.log(e,this.getClass() );
            return  ResponseBean.buildFailure(e.getMessage());
        }
    }
    

```


### 3.3、测试

#### 3.3.1、测试1 getOne查询一个不存在的数据

[http://localhost:8080/demo/jpa/findOrGet?type=cru&which=get&id=100](http://localhost:8080/demo/jpa/findOrGet?type=cru&which=get&id=100)

```java

报错信息
报错的文件是：EntityManagerFactoryBuilderImpl.java报错方法是：handleEntityNotFound报错的行是：144报错的信息是：Unable to find com.hlj.entity.db.demo.DemoEntity with id 100


```

#### 3.3.2、findOne:查询一个不存在的id数据时，返回的值是null.

type分别为jpa和cur

[http://localhost:8080/demo/jpa/findOrGet?type=cru&which=find&id=100](http://localhost:8080/demo/jpa/findOrGet?type=cru&which=find&id=100)

[http://localhost:8080/demo/jpa/findOrGet?type=jpa&which=find&id=100](http://localhost:8080/demo/jpa/findOrGet?type=jpa&which=find&id=100)


```json
{
    "success": true,
    "result": null,
    "message": "",
    "code": "200",
    "date": "1547197480777"
}
```


#### 3.3.3、getOne返回一个存在的数据，也会出现问题


```java

方法中可以使用，再包装之后传递给前端会报错。错误如下

打印日志：DemoEntity(id=17, name=HealerJean, age=2, cdate=2019-01-10 15:15:10.0, udate=2019-01-10 01:15:11.0)


报错信息:
2019-01-11 17:06:47.898 [http-nio-8080-exec-7] ERROR c.h.config.ControllerExceptionConfig - 报错的文件是：AbstractJackson2HttpMessageConverter.java报错方法是：writeInternal报错的行是：299报错的信息是：Could not write JSON: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS); nested exception is com.fasterxml.jackson.databind.JsonMappingException: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.hlj.data.general.ResponseBean["result"]->com.hlj.entity.db.demo.DemoEntity_$$_jvstcc7_0["handler"])



``` 

原因：
Jason转换失败（直接返回前端会造成）,不是没有 implements Serializable 的原因

解决方法:
//实体类上忽略下面的字段，在Json传递的时候
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})


### 3.4、总结：
总之以后我们使用的时候，就用findOne，不要使用getOne,查询语句get,find不受影响，随意使用





#### getOne:查询一个不存在的id数据时，直接抛出异常，因为它返回的是一个引用，简单点说就是一个代理对象。

这样一看想起来hibernate中get和load区别

![WX20190111-155653@2x](MarkDownImage/WX20190111-155653@2x.png)








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
		id: 'VncLBPDd5fG3gbU7',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

