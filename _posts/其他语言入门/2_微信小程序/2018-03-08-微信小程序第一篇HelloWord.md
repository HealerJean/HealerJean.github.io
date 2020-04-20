---
title: 微信小程序第一篇HelloWord
date: 2018-03-08 13:33:00
tags: 
- WeChat
category: 
- WeChat
description: 微信小程序第一篇HelloWord
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



## **准备工作**
### 1、注册微信公众号（小程序）
### **2、微信开发软件下载地址**[点击下载](https://mp.weixin.qq.com/debug/wxadoc/dev/devtools/download.html)

### **3、注册号信息之后，我们会受到一个`AppID` 和一个`AppSecret`，如下图所示**
![WX20180308-131628@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180308-131628@2x.png)

## **1、打开软件之后，会自动出现如下的代码** 

![WX20180308-131852@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180308-131852@2x.png)


## **2、介绍下主要代码的构成**
![20170206092847476](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/20170206092847476.png)


## **3、修改`HelloWord` 为你好，世界**
修改index.js中的代码如下

```
Page({
  data: {
    motto: '你好，世界',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },

```

## **4、重新编译，上传代码**

![WX20180308-132422@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180308-132422@2x.png)

## **5、打开[网址进行审核](https://mp.weixin.qq.com/wxopen/wacodepage?action=getcodepage&token=959420618&lang=zh_CN)**
选中体验版本，然后扫码，即可预览

![WX20180308-133655@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180308-133655@2x.png)

![WX20180308-133830@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180308-133830@2x.png)


## **6、手机预览**
---
<br/>
![WX20180308-133929@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180308-133929@2x.png)

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
		id: 'lp1tkURFKzI7QONv',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

