---
title: Hibernate和Mybatis区别
date: 2018-08-17 03:33:00
tags: 
- Database
category: 
- Database
description: Hibernate和Mybatis区别
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进


<font  clalss="healerColor" color="red" size="5" >     

</font>

<font  clalss="healerSize"  size="5" >     </font>

-->

## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    
     
     

#### 1、hibernate完全可以通过对象关系模型实现对数据库的操作，拥有完整的JavaBean对象与数据库的映射结构来自动生成sql。而mybatis仅有基本的字段映射，对象数据以及对象实际关系仍然需要通过手写sql来实现和管理。

#### 2、hibernate移植性好，数据库切换方便

前提是是否写了通用sql


#### 3、学习成本方面

hibernate学习成本高，mybatis开发方面与传统的额jdbc相差不大，纯sql语句能够加快开发，hibernate虽然也能写sql语句，但是看起来不是那么方便，因为再java类中，我的项目中二者都使用了。


   

    
     
     
     
     
     
     
     
     
<br><br>    
<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>
<br>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

