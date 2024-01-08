---

title: JVM之_4_Java的client模式和server模式
date: 2018-03-06 03:33:00
tags: 
- JVM
category:  
- JVM
description: JVM之_4_Java的client模式和server模式
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            



# 一、说明

> `hotspot` 包括`server` 和 `client` 两种模式的实现：    

`Java` `HotSpot` `Client` `VM` ( `-client` )，为在客户端环境中减少启动时间而优化；           

`Java` `HotSpot` `Server` `VM` ( `-server` )，为在服务器环境中最大化程序执行速度而设计。   



# 二、比较

> 因为 `Server` 模式启动的`JVM` 采用的是重量级的虚拟机，对程序采用了更多的优化，`server` 模式会尝试收集更多的系统性能信息，使用更复杂的优化算法对程序进行优化。而 `Client` 模式启动的 `JVM` 采用的是轻量级的虚拟机      
>



两种模式的区别在于，`Client` 模式启动速度较快，`Server` 模式启动较慢；但是启动进入稳定期长期运行之后`Server` 模式的程序运行速度比 `Client` 要快很多。      



因此当系统完全启动并进入运行稳定期后，`server` 模式的执行速度会远远快于 `client`模式，所以在对于后台长期运行的系统，使用 `server` 模式启动对系统的整体性能可以有不小的帮助，但对于用户界面程序，运行时间不长，又追求启动速度建议使用 `client`模式启动  






# 三、模式查看

> **看了一下 `hotspot` 的安装的模式，32位的 `hotspot` 都是 `client 模式；64位的都是 `server`模式的。**     

## 1、查看当前虚拟机处于那种模式

```shell
└─[$]  java -version                                                 [17:04:
java version "1.8.0_271"
Java(TM) SE Runtime Environment (build 1.8.0_271-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.271-b09, mixed mode)
┌─[healerjean@HealerJn2023MAC] - [~] - [2734]
└─[$]           
```





+ **下图是32位的虚拟机**

![WX20180411-144945@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180411-144945@2x.png)



## 2、 `Client` 与 `Server` 切换  

> 首先要确认 `JDK` 支持哪一种或两种模式。查看`JAVA_HOME/jre/bin`目录下是否存在 `client` 或 `server`目录。`32` 位的JDK一般都支持`server` 和 `client` 两种模式。`64` 位的虚拟机好像只支持 `server`模式(不能修改模式)，没有 `client` 目录。如下为 `32` 位 `JDK`模式支持目录：    
>



![WX20180411-145050@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180411-145050@2x.png)

![WX20180411-145255@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180411-145255@2x.png)

​    

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
		id: 'DmWG8wRpcKRBY3Wt',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

