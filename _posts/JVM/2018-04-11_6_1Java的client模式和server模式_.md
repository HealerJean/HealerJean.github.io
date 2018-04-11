---
title: 6.1、Java的client模式和server模式
date: 2018-03-06 03:33:00
tags: 
- JVM
category: 
- JVM
description: Java的client模式和server模式
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


## 1、解释和比较

hotspot包括server和client两种模式的实现：<br/>
Java HotSpot Client VM(-client)，为在客户端环境中减少启动时间而优化；<br/>
Java HotSpot Server VM(-server)，为在服务器环境中最大化程序执行速度而设计。<br/>
比较：<br/>

两种模式的区别在于，Client模式启动速度较快，Server模式启动较慢；但是启动进入稳定期长期运行之后Server模式的程序运行速度比Client要快很多。

因为因为Server模式启动的JVM采用的是重量级的虚拟机，对程序采用了更多的优化，server模式会尝试收集更多的系统性能信息，使用更复杂的优化算法对程序进行优化。而Client模式启动的JVM采用的是轻量级的虚拟机

因此当系统完全启动并进入运行稳定期后，server模式的执行速度会远远快于client模式，所以在对于后台长期运行的系统，使用server模式启动对系统的整体性能可以有不小的帮助，但对于用户界面程序，运行时间不长，又追求启动速度建议使用client模式启动


## 2、模式根据

看了一下hotspot的安装的模式，32位的hotspot都是client模式；64位的都是server模式的。

1、查看当前虚拟机处于那种模式，如下图是64位的虚拟机

Java -version

![WX20180411-144901@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180411-144901@2x.png)

下图是32位的虚拟机

![WX20180411-144945@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180411-144945@2x.png)

2、 Client与Server切换

首先要确认JDK支持哪一种或两种模式。查看JAVA_HOME/jre/bin目录下是否存在client或server目录。32位的JDK一般都支持server和client两种模式。64位的虚拟机好像只支持server模式(不能修改模式)，没有client目录。如下为32位JDK模式支持目录： 

![WX20180411-145050@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180411-145050@2x.png)



在32位JDK中，jvm.cfg位置为：JAVA_HOME/jre/lib/i386/jvm.cfg； <br/> 
切换模式只需要将client和server的声明语句互换位置即可。如下图所示
![WX20180411-145255@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180411-145255@2x.png)




<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

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
		id: 'DmWG8wRpcKRBY3Wt',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

