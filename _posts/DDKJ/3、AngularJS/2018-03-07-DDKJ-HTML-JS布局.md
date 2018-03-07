---
title: DDKJ-HTML-JS布局
date: 2018-03-07 21:33:00
tags: 
- HTML 
- AngularJs
category: 
- HTML
- AngularJs
description: HTML中提示控件不能正常在需要提示的控件之下。
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
-->

# 1、input下面的标签提示控件不能对齐<br>
## 效果展示

### 之前

![WechatIMG234](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WechatIMG234.jpeg)


### 之后
![WX20180307-103852@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180307-103852%402x.png)

## **之前代码** <br/>
---

```
<div class="form-item form-remark">
    <div class="item-title"><span class="adError">*</span> 投放产品</div>
    <input name="app" ng-model="chance.appName" type="text" class="item-select" placeholder="请填写产品名称">
</div>
<div ng-if="ishaveName==true" class="margin-top-1 adError">
    该应用已被商务"{{haveName}}"录入国，可保持沟通
</div>

```
## 修改以后


```
<div  class="form-item flex-col-center flex-baseline">
    <div class="item-title"><span class="adError">*</span> 投放产品</div>
    <div>
        <input name="app" ng-model="chance.appName" type="text" class="item-select" placeholder="请填写产品名称">
        <div ng-if="ishaveName==true" class="adError margin-top-1">该应用已被商务"{{haveName}}"录入过，可保持沟通</div>
    </div>
</div>

```

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
		id: 'GitHub评论Gitalk插件',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

