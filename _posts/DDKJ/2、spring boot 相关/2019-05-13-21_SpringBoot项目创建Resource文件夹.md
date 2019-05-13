---
title: SpringBoot项目创建Resource文件夹
date: 2019-05-13 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot项目创建Resource文件夹
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



针对于刚刚新创建的maven项目中没有resource文件夹，当我在设计一个service接口层的时候，没有考虑过放入资源文件，所以一开始没有创建resource文件夹，但是后来需要做一个freemarker模板项目，我需要一个文件夹来存放静态资源html模板文件



#### 1、设置-> Project Structer

![1557727708927](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727708927.png)

#### 2、Modules-->Sources-->main右键-->New Folder, 输入resources

![1557727726716](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727726716.png)



![1557727740146](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727740146.png)



#### 3、选择resources右键，点击Resources（选择文件资源类型），然后Apply，OK即可（添加test也一样）

![1557727765572](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727765572.png)



#### 4、成功

![1557727779133](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727779133.png)



















<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



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
		id: 'ngNBMJuIE8SZTmCj',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

