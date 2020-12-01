---
title: MyISAM与InnoDB两者之间区别
date: 2019-03-06 03:33:00
tags: 
- Database
category: 
- Database
description: MyISAM与InnoDB两者之间区别
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            




## 1、`MyISAM`与`InnoDB`两者之间区别

**`MyISAM`**

> 1、不支持事物 
>
> 2、myisam只支持表级锁       
>
>  a、对MyISAM表的读操作（加读锁）：MyISAM在执行查询语句（SELECT）前，会自动给涉及的所有表加读锁，**不会阻塞其他进程对同一表的读请求**，但会阻塞对同一表的写请求。只有当读锁释放后，才会执行其它进程的写操作。
>
> b、对MyISAM表的写操作（加写锁）  ：在执行更新操作（UPDATE、DELETE、INSERT等）前，会自动给涉及的表加写锁。会阻塞其他进程对同一表的读和写操作，只有当写锁释放后，才会执行其它进程的读写操作。
>
> 
>
> 3、MyISAM的索引和数据是分开的。
>
> 4、支持全文索引
>
> 3、不支持事物



**`InnoDB`**

> 1 、支持事物    
>
> 2、支持行锁    
>
> 3.、InnoDB的数据文件本身就是索引文件     
>
> 4、不支持全文索引





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
		id: 'w2AJZ3efDyxpgWoM',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

