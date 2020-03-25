---
title: Windows安装PHP
date: 2019-02-27 03:33:00
tags: 
- Windows
category: 
- Windows
description: Windows安装PHP
---



<!--
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/ 
　　首行缩进
-->






**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)    



# 1、下载 

打开php官网下载页面 [https://windows.php.net/download](https://windows.php.net/download)  ，点击”Windows downloads”（下图）

> 其中每个版本有“Non Thread Safe”和“Thread Safe”可选，我们一般选“Non Thread Safe”非线程安全，点击“Zip”下载。





![1582789284247](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789284247.png)





# 2、安装 



> **安装目录 ：D:\programFiles\php-7.4.3-nts-Win32-vc15-x64**  



## 2.1、设置环境变量

![1582789366440](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789366440.png)





![1582789435301](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789435301.png) 



## 2.2、修改配置文件 



>  将`php.ini-development`复制一份到同目录下，并命名为`php.ini`。    
>
> 编辑php.ini，找到`; extension_dir = "ext"`，在该行下面添加如下两行：



```
extension_dir = "D:\programFiles\php-7.4.3-nts-Win32-vc15-x64\ext"
extension=php_curl.dll
```



## 2.3、查看是否安装成功 



![1582789577708](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789577708.png)







  **<font  color="red">感兴趣的，欢迎添加博主微信 </font>**       

​    

哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦   



|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'YuLQmOB3UCAk6DMn',
    });
    gitalk.render('gitalk-container');
</script> 

