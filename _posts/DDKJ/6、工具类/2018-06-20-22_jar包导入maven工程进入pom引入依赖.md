---
title: 22、jar包导入maven工程进入pom引入依赖
date: 2018-03-06 03:33:00
tags: 
- Maven
category: 
- Maven
description: jar包导入maven工程进入pom引入依赖
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

这篇其实在我最开始接触maven的时候，就该写了，后来一直没怎么用到，所以就没写，现在好了，正好项目中使用到了这个功能，好吧，我就将它写到了这里。<br/>

如果一边使用maven一边又导入线程的jar包，当我们再maven package的时间，这个jar不能够帮我们导入进来。所以就需要一种这样活着那样的方式，当然方式很多，但是我选择我下面这种，因为简单方便，快



## 1、现有jar包的名字
> 如下，发现很不规则


![WX20180606-175209@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180606-175209@2x.png)

 
### 1.1、第一步修改名字，

>主要是为了方便我们区分，其实名字可以随意指定的，但是最好加个版本号，也不要像下面这样版本号太大

![WX20180606-175342@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180606-175342@2x.png)


### 1.2、执行maven命令


```
Dfile=taobao-sdk-java-source-5.2.1.jar //jar名字
DgroupId=taobao-sdk-java-source //groupId
DartifactId=taobao-sdk-java-source -Dversion=5.2.1 //artifaceId 
Dversion=5.2.1 //版本号，可以随意指定的

mvn install:install-file -Dfile=taobao-sdk-java-source-5.2.1.jar -DgroupId=taobao-sdk-java-source -DartifactId=taobao-sdk-java-source -Dversion=5.2.1 -Dpackaging=jar  
mvn install:install-file -Dfile=taobao-sdk-java-5.2.1.jar -DgroupId=taobao-sdk-java -DartifactId=taobao-sdk-java -Dversion=5.2.1 -Dpackaging=jar  


```


### 1.3、pom.xml引入依赖


```
<!--淘客官网下载的jar，导入了maven库中，具体观察readme.md-->
<dependency>
    <groupId>taobao-sdk-java-source</groupId>
    <artifactId>taobao-sdk-java-source</artifactId>
    <version>5.2.1</version>
</dependency>

<dependency>
    <groupId>taobao-sdk-java</groupId>
    <artifactId>taobao-sdk-java</artifactId>
    <version>5.2.1</version>
</dependency>

```

### 1.4、ok、大功告成




<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'XNVfMfDTjesCZoKm',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

