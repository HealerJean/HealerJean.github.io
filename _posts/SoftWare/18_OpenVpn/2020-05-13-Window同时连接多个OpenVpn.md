---
title: Window同时连接多个OpenVpn
date: 2020-05-13 03:33:00
tags: 
- SoftWare
category: 
- SoftWare
description: Window同时连接多个OpenVpn
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、windows 同时连接多个 openvpn

## 1.1、安装openVpn



## 1.2、配置多个config



![image-20200513093813246](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200513093813246.png)

## 1.3、管理员运行`addtap.bat`

> 双击以下文件，会自动添加一块虚拟网卡，如果自己的用户不是 administrator，需要以管理员身份去运行，否则会添加失败。

```shell
C:\Program Files\TAP-Windows\bin
```





![image-20200513093859670](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200513093859670.png)

































![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





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
