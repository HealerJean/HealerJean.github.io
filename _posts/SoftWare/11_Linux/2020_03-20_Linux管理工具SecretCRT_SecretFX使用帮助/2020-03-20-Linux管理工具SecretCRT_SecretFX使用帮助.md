---
title: Linux管理工具SecretCRT_SecretFX使用帮助
date: 2020-03-20 03:33:00
tags: 
- Linux
- SoftWare
category: 
- Linux
- SoftWare
description: Linux管理工具SecretCRT_SecretFX使用帮助
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、如果出现不显示二维码的情况



![1584329665749](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584329665749.png)



# 2、调整颜色 

![1584329707148](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584329707148.png)



# 3、不保存用户密码

> 虽然在第一次通过SSH连接时，输入密码后，已经选择Accept&Save去保存了密码： 



**“options-global options-general-mac options”去掉“use keychain”的勾选项**



# 4、设置secureCRT不掉线的方法

Options—>Global Options—>General—>Default Session—>Edit Default Settings—>Terminal—>Anti-idle

选中Send protocol NO-OP every 120 seconds



# 5、添加快捷命令 

![1584675872177](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584675872177.png)





![1584675894076](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584675894076.png)





![1584675936380](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584675936380.png)



## 5.1、`new Button Bar`



![1584675977928](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584675977928.png)



![1584676005994](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584676005994.png)



## 5.2、`new Button`





![1584676099918](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584676099918.png)







![1584676125598](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1584676125598.png)



# 6、备份



![image-20200512163146407](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200512163146407.png)











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
