---
title: session问题和cas单点登录ticket
date: 2018-03-09 04:33:00
tags: 
- CAS
category: 
- CAS
description: session问题和cas单点登录ticket
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
-->

## 前言

1、博主这个礼拜很难受，公司没什么活干，礼拜三说要做一个微信小程序，所以马上看了下微信小程序怎么做。学了点皮毛，但是够用了。

---

2、关于session，其实之前也不是很了解，但是学了单点登录，突然觉得必须稍微了解一下。

---

3、之前做过一个Nginx，Tomcat，session共享，当时用了的是一个域名，也就是常见的localhost，当时没有考虑过一个域名下的session是不是同一个，但是按照教程做完了。通过配置tomcat和项目，最后得到是一个sessionId。其实现在我才知道，如果没有配置共享session，本来一个localhost，不同端口，如果开启项目的话，每次刷新不同的端口项目，session变了。那是因为当前的端口将之前的端口的sessionId被替换掉了。但是之前的端口的sessionId再刷新的话，就又会变了。

---

4、接着说sso，我还是用了同一个localhost，发现，单点是可以登录，但是只能登录一个客户端。这就偶是3中强调的一个域名session是同一个，如果登录另外一个。通过sso就会将之前的session给干掉。只留下当前登录的那个客户端。我这里用的是sso使用的是ticket验证，不可以共用一个sessionId的。


## 1、一个域名下两个不同端口的项目，session不能同时存在

以cas客户端为例，由于两个项目登录信息都是存在session中的，但是当我登录一个项目之后，再去登录另一个时，前一个的session就会清空。

## 2、得到不同的sesion

基于cookie区分路径、域名、名称，有三个解决方案。
1、设置域名不同，比如 
http://projectA.com:7777/ 
http://projectB.com:8080/

2、设置路径不同，设置工程名字，不再映射根路径，比如 
 http://ip1:7777/projectA  http://ip1:8080/projectB

3、设置key不同  在Tomcat的server.xml中配置sessionCookieName，只要两个不相同就可以  Tomcat server.xml context配置

```
<Service name="Catalina">
  
		<Connector connectionTimeout="20000" port="8081" protocol="HTTP/1.1" redirectPort="8443" URIEncoding="UTF-8"/>
		
		<Connector port="8009" protocol="AJP/1.3" redirectPort="8443"/>

		<Engine defaultHost="localhost" name="Catalina">

		<Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"/>

		<Host appBase="webapps" autoDeploy="true" name="localhost" unpackWARs="true" xmlNamespaceAware="false" xmlValidation="false">

		<Context path="" docBase="Test1" reloadable="true" sessionCookieName="Test1"/>
      </Host>
    </Engine>
  </Service>
```

---
---

如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

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
		id: 'fghjkl687',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

