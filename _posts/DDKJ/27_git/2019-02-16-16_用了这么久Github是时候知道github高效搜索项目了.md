---
title: 用了这么久Github是时候知道github高效搜索项目了
date: 2019-02-16 03:33:00
tags: 
- GitHub
category: 
- GitHub
description: 用了这么久Github是时候知道github高效搜索项目了
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言


2019年的github的一声巨雷，<font  color="red" size="4">  个人可以免费创建私有仓库了 </font>，哎，为了省点银子，博主还专门去码云上建立私有仓库来放自己的每天的日记。两边操作，现在终于可以都放到github上来的。<br/>

但是，这种权限是有限制的哦<br/>
<font color="red"> 
GitHub Free now includes unlimited private repositories. For the first time, developers can use GitHub for their private projects with up to three collaborators per repository for free. Many developers want to use private repos to apply for a job, work on a side project, or try something out in private before releasing it publicly. Starting today, those scenarios, and many more, are possible on GitHub at no cost. Public repositories are still free (of course—no changes there) and include unlimited collaborators.<br/>

个人可以免费创建私有仓库，数量无限制，不过有一个限制就是，免费的私有仓库同时最多只能有三个协作者<br/>
</font>

## 回归正题

相信我们很多人或多或少都接触过github，更有些人经常使用自己的github，同样也使用过github的搜索功能，但是获取觉得搜索功能不够友好，每次都得一页一页翻，实不相瞒，博主也是…………，那么这里就简单介绍下如果高效搜索github项目吧

## 1、平常我们所使用的搜索

直接输入关键词 比如博主正在学习的SpringCloud 


#### 1、直接输入关键词

#### 2、输入排序的方式


![WX20190216-121207@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-121207@2x.png)




### 1、in:name 关键词  ，仓库标题搜索 


>in:name SpringCloud


![WX20190216-121707@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-121707@2x.png)


### 2、in:descripton 关键词 ，仓库描述搜索

>in:descripton SpringCloud


![WX20190216-132418@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-132418@2x.png)

### 3、in:readme 关键词 ，readme文件搜索

>in:readme SpringCloud


### 4、stars: > 数字  关键字  ，star/fork 数量搜索

> stars:>3000 spring cloud
> forks:>1000 spring cloud


![WX20190216-132614@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-132614@2x.png)


![WX20190216-132630@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-132630@2x.png)


### 5、size:>= 数字 ，指定仓库的大小搜索

>size:>=5000 Spring Cloud   
>这里注意下，这个数字代表 K, 5000 代表着5M。
 
 ![WX20190216-133245@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-133245@2x.png)


### 6、pushed:>2019-01-03 关键词 ，发布时间搜索

>pushed:>2019-02-12 springcloud

![WX20190216-133659@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-133659@2x.png)



### 7、user:用户名  ，用户名称搜索

>user:healerjean

![WX20190216-133737@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-133737@2x.png)

## 组合命令使用

在用户下面搜索

>user:healerjean in:name springcloud

![WX20190216-133953@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190216-133953@2x.png)



<br/><br/><br/>
<font color="red"> 感兴趣的，欢迎添加博主微信， </font><br/>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

