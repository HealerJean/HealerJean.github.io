---
title: 1、VScode读取vue前端代码主题配置
date: 2018-04-24 03:33:00
tags: 
- SoftWare
category: 
- SoftWare
description: 1、VScode读取vue前端代码主题配置
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

## 1、字体太小

首选项->设置->用户设置，添加如下


```json
{
    "editor.fontSize":15
}

```
![WX20180424-101356](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180424-101356.png)


## 2、主题设置

太简单了自己弄吧

## 3、vue代码高亮

尽管设置了主题，但是vue代码缺不是高亮显示的，这个时候我们需要安装一款插件: Ctrl + P 然后输入 ext install vetur 然后回车点安装即可。安装完成重新载入即可

![WX20180424-101541](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180424-101541.png)






<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'aaVAGzXxIdbPu0pS',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

