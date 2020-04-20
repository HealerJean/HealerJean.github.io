---
title: SpringBoot启动加载数据CommandLineRunner
date: 2018-03-29 18:44:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot启动加载数据CommandLineRunner
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             



　　实际应用中，我们会有在项目服务启动的时候就去加载一些数据或做一些事情这样的需求。  为了解决这样的问题，Spring Boot 为我们提供了一个方法，通过实现接口 CommandLineRunner 来实现。


order越小越先执行

## 1、order为1

```java
package com.hlj.commandlinerunner.comhljcommandlinerunner.orlder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午6:18.
 */
@Component
@Order(value=1)
public class MyStartupRunner1 implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作 11111111 <<<<<<<<<<<<<");
    }

}


```

## 2、order为2

```java
package com.hlj.commandlinerunner.comhljcommandlinerunner.orlder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午6:18.
 */
@Component
@Order(value=2)
public class MyStartupRunner2 implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作 22222222 <<<<<<<<<<<<<");
    }

}
```


## 3、启动SpringBoot

![WX20180329-182220](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180329-182220.png)





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
		id: 'synrCiOxeZMrB4Qr',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

