---
title: 服务器_之_IOPS和IO Thruput
date: 2022-01-01 00:00:00
tags: 
- HardWare
category: 
- HardWare
description: 服务器_之_IOPS和IO Thruput
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

# 1、IOPS

> `IOPS` (`Input` / `Output ` `Per` `Second`) 即每秒的读写次数，是衡量磁盘性能的主要指标之一。
>
> `IOPS`是指单位时间内系统能处理的I/O请求数量，一般以每秒处理的 `I/O` 请求数量为单位，`I/O`请求通常为读或写数据操作请求。



1.2、`IOPS` 和 `IO Thruput` 的取舍

> 关注 `IOPS`：随机读写频繁的应用，如小文件存储(图片)、OLTP数据库、邮件服务器，关注随机读写性能，是关键衡量指标。    
>
> 关注  `IO Thruput` ：顺序读写频繁的应用，传输大量连续数据，如电视台的视频编辑，视频点播 `VOD(Video On Demand)`，关注连续读写性能。数据吞吐量是关键衡量指标。
>
> > `IOPS` 和数据吞吐量适用于不同的场合：    
> >
> > 追求 `IOP` ：读取 `10000` 个 `1KB` 文件，用时 `10` 秒 `Throught` (吞吐量) = `1MB/s` ，`IOPS` = `1000`  
> >
> > 追求吞吐量：读取1个`10MB`文件，用时`0.2`秒 `Throught`(吞吐量) = `50MB/s`,` IOPS` = `5`  







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
		id: 'TPJ9BS7tMFedY6Ix',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



