---
title: Cookie和Session小结
date: 2018-08-05 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Cookie和Session小结
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


## 1、跨域问题

<table class="border" style="margin-left:2em;border:1px solid #C0C0C0;border-collapse:collapse;"><tbody><tr><th style="border:1px solid #C0C0C0;border-collapse:collapse;">URL</th>
<th style="border:1px solid #C0C0C0;border-collapse:collapse;">说明</th>
<th style="border:1px solid #C0C0C0;border-collapse:collapse;">是否允许通信</th>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com/a.js&nbsp;<br>
http://www.a.com/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">同一域名下</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">允许</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com/lab/a.js&nbsp;<br>
http://www.a.com/script/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">同一域名下不同文件夹</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">允许</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com:8000/a.js&nbsp;<br>
http://www.a.com/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">同一域名，不同端口</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不允许</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com/a.js&nbsp;<br>
https://www.a.com/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">同一域名，不同协议</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不允许</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com/a.js&nbsp;<br>
http://70.32.92.74/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">域名和域名对应ip</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不允许</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com/a.js&nbsp;<br>
http://script.a.com/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">主域相同，子域不同</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不允许</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.a.com/a.js&nbsp;<br>
http://a.com/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">同一域名，不同二级域名（同上）</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不允许（cookie这种情况下也不允许访问）</td>
</tr><tr><td style="border:1px solid #C0C0C0;border-collapse:collapse;">http://www.cnblogs.com/a.js&nbsp;<br>
http://www.a.com/b.js</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不同域名</td>
<td style="border:1px solid #C0C0C0;border-collapse:collapse;">不允许</td>
</tr></tbody></table>


## 2、Session的小结

2、之前做过一个Nginx，Tomcat，session共享，当时用了的是一个域名，也就是常见的localhost，当时没有考虑过一个域名下的session是不是同一个，但是按照教程做完了。通过配置tomcat和项目，最后得到是一个sessionId。其实现在我才知道，如果没有配置共享session，本来一个localhost，不同端口，如果开启项目的话，每次刷新不同的端口项目，session变了。那是因为当前的端口将之前的端口的sessionId被替换掉了。但是之前的端口的sessionId再刷新的话，就又会变了。

---

4、接着说sso，我还是用了同一个localhost，发现，单点是可以登录，但是只能登录一个客户端。这就偶是3中强调的一个域名session是同一个，如果登录另外一个。通过sso就会将之前的session给干掉。只留下当前登录的那个客户端。我这里用的是sso使用的是ticket验证，不可以共用一个sessionId的。所以请看下面的第一个


### 1、一个域名下两个不同端口的项目，session不能同时存在

以cas客户端为例，由于两个项目登录信息都是存在session中的，但是当我登录一个项目之后，再去登录另一个时，前一个的session就会清空。

### 2、得到不同的sesion

基于cookie区分路径、域名、名称，有三个解决方案。
1、设置域名不同，比如 
http://projectA.com:7777/ 
http://projectB.com:8080/

2、设置路径不同，设置工程名字，不再映射根路径，比如 
 http://ip1:7777/projectA  http://ip1:8080/projectB

3、设置key不同  在Tomcat的server.xml中配置sessionCookieName，只要两个不相同就可以  Tomcat server.xml context配置



## 3、反思

1、单点登录中 两个客户端不同域名，自然分配的是不同的sessionId，因为代表不同的逻辑代码。
2、如果是负载均衡项目，那么久需要保证我们的Cookie中的 sessionId是同一个，这样才能代表是同一个逻辑代码。

### 3.1、session

存储在服务器的内存中，tomcat的StandardManager类将session存储在内存中，也可以持久化到file，数据库，memcache，redis等。客户端只保存sessionid到cookie中，而不会保存session，session销毁只能通过invalidate或超时，关掉浏览器并不会关闭session。


```
HttpSession session = request.getSession();
 System.out.println(session.getId());
```

<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'CsEcgEt8Jg8Kz5yI',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

