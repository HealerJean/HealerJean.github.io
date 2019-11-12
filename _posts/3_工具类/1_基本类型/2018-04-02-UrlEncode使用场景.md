---
title: UrlEncode使用场景
date: 2018-03-06 03:33:00
tags: 
- Url
category: 
- Url
description: UrlEncode使用场景
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言



## 1.url请求中有回调地址

> 比如，一个接口，在传送的时候，我们要参数中包含一个url回调地址，也就是提供给对方进行回调的。那么如果是http 使用get请求的时候，不可以url+url ，以为回调地址中也可能包含参数连接符&等。这样就会混淆两个url。这个时候，就需要使用urlencode将回调地址编码。如果是post的话就不用考虑喽。


```java
　　1.字符"a"-"z"，"A"-"Z"，"0"-"9"，"."，"-"，"*"，和"_" 都不会被编码;
　　2.将空格转换为加号 (+) ;
　　3.将非文本内容转换成"%xy"的形式,xy是两位16进制的数值;
　　4.在每个 name=value 对之间放置 & 符号。
　　
　　URLEncoder类包含将字符串转换为application/x-www-form-urlencoded MIME 格式的静态方法。



String name=java.net.URLEncoder.encode("测试", "UTF-8");
System.out.println(name);
name=java.net.URLEncoder.encode(name,"UTF-8");
System.out.println(name);
name=java.net.URLDecoder.decode(name, "UTF-8");
System.out.println(name);
System.out.println(java.net.URLDecoder.decode(name, "UTF-8"));


```



<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'aNVkyOXWQlWvlOF7',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

