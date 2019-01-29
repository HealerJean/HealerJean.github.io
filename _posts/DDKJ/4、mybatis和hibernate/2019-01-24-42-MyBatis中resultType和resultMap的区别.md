---
title: MyBatis中resultType和resultMap的区别
date: 2019-02-24 03:33:00
tags: 
- database
category: 
- database
description: MyBatis中resultType和resultMap的区别
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

MyBatis中在查询进行select映射的时候，返回类型可以用resultType，也可以用resultMap<br/>

resultType是直接表示返回类型的，<br/>
①当提供的返回类型属性是resultType时，MyBatis会将Map里面的键值对取出赋给resultType所指定的对象对应的属性。所以其实MyBatis的每一个查询映射的返回类型都是ResultMap，只是当提供的返回类型属性是resultType的时候，MyBatis对自动的给把对应的值赋给resultType所指定对象的属性。<br/><br/>
resultMap则是对外部ResultMap的引用<br/>
②当提供的返回类型是resultMap时，因为Map不能很好表示领域模型，就需要自己再进一步的把它转化为对应的对象，这常常在复杂查询中很有作用。


```xml
<resultMap id="BaseResultMap" type="com.cachee.ilabor.att.clientmodel.User"> 
<result column="ID" property="id" jdbcType="INTEGER" /> 
<result column="SN" property="SN" jdbcType="VARCHAR" /> 
<result column="companyId" property="companyId" jdbcType="VARCHAR" /> 
<result column="tb_isDelete" property="tb_isDelete" jdbcType="VARCHAR" />
<result column="tb_createTime" property="tb_createTime" jdbcType="VARCHAR" /> 
</resultMap>

```

### 一直在用

只传入id和resultType（update和delete没有resultType，但是还能返回值）

```xml
   <select id="countFindCouponTaoKeDataByParam" resultType="java.lang.Long">
   </select>
```





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
		id: 'rqc0ICfQVxRFlUSZ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

