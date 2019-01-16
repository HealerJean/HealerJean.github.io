---
title: Throwable_Exception_Error
date: 2010-01-17 03:33:00
tags: 
- Java
category: 
- Java
description: Throwable_Exception_Error
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

Throwable是java.lang包中一个专门用来处理异常的类。它有两个子类，即Error 和Exception，它们分别用来处理两组异常。 


## 1、Error

用来处理程序运行环境方面的异常，比如，虚拟机错误、装载错误和连接错误，这类异常主要是和硬件有关的，而不是由程序本身抛出的。 <br/>

Error无法预期的错误因此，这是不可捕捉的，无法采取任何恢复的操作，一般只能显示错误的信息。<br/>

比如 OutOfMemoryError，试多少次很大概率出错的。


## 2、Exception

va提供了两类主要的异常:运行时异常runtime exception和一般异常checked exception。checked 异常。

### 2.1、运行时异常

Java程序运行时常常遇到的各种异常的处理，其中包括隐式异常。比如，程序中除数为0引起的错误、数组下标越界错误等，这类异常也称为运行时异常，,因为它们虽然是由程序本身引起的异常，但不是程序主动抛出的，而是在程序运行中产生的。<br/>

运行时异常我们可以不处理。这样的异常由虚拟机接管。出现运行时异常后，系统会把异常一直往上层抛，一直遇到处理代码。如果不对运行时异常进行处理，那么出现运行时异常之后，要么是线程中止，要么是主程序终

### 2.2、一般异常

这些异常也称为显式异常。它们都是在程序中用语句抛出、并且也是用语句进行捕获的，比如，文件没找到引起的异常、类没找到引起的异常等。 <br/>

JAVA要求程序员对其进行catch。所以，面对这种异常不管我们是否愿意，只能 catch捕获，要么用throws字句声明抛出，交给它的父类处理，否则编译不会通过。   



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
		id: 'UbQ31gNiwspmn0cj',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

