---
title: SpringCloudConfig配置中心原理
date: 2024-03-28 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringCloudConfig配置中心原理
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、简介

> `Spring` `Cloud` `Config` 分为 `Server` 端和 `Client` 端。其中 `Spring` `Cloud` `Config` `Server` 是 `Spring` `Cloud` 为指定应用中所有服务提供集中式配置的一个服务，借助 `Spring` `Cloud` `Config` `Server` 可以实现集中管理所有应用的配置，避免重复配置。



# 二、架构

![image-20240328154322046](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20240328154322046.png)



![image-20240328154524992](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20240328154524992.png)





# 三、原理



## 1、手动刷新

> 因为 `springcloud`- `config` 的服务端第一次读取远程 `git` 服务器的配置之后是会缓存一份配置在本地的,即使远程 `git` 修改了配置,`config` 客户端也依然是读取缓存中的配置,而使用手动刷新可以强制刷新缓存,让 `config` 端读取最新配置文件.    
>
> > 调用 `/bus/refresh`接口





## 2、自动刷新

> `Git` 本身没有事件通知机制所以需要依赖 `mq` 来通知各个客户端(而且也只是通知说有配置更新,还需要一次网络交互才能获取到新配置) 当更新配置之后 `git` 会通过 `webhook` 调用 `/bus/refresh`接口。 这是自动刷新配置的入口

![image-20240328154835655](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20240328154835655.png)



![image-20240328155226872](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20240328155226872.png)









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
		id: 'oir4JUmKsC3nkONp',
    });
    gitalk.render('gitalk-container');
</script> 






<!-- Gitalk end -->



