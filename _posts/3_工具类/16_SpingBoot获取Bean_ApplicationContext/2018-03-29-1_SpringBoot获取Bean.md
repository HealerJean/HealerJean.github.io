---
title: 1、SpringBoot获取Bean
date: 2018-03-29 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot获取Bean
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)    

对于没有熟悉springBoot的小白来说，可能还在局限于spring上下文获取bean。其实SpringBoot也一样的

## 1、第一种通过工具类获取


获取方式

```java
APIAdminUserService userService = SpringHelper.getBean(APIAdminUserService.class);

```

工具类代码


```java
package com.sankuai.windmill.interact.support.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/30  下午12:13.
 */
@Service
public class SpringHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public SpringHelper() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class clazz) {
        return applicationContext.getBean((Class<T>) clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return  applicationContext.getBean(name, clazz);
    }

    public static Object getBeanByName(String beanName) throws BeansException {
        return applicationContext.getBean(beanName);
    }
}

```

## 2、直接通过注入获取


```java
@Component
public class WebSocketServerInitializer extends ChannelInitializer {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    protected void initChannel() throws Exception {
     pipeline.addLast(applicationContext.getBean(WebSocketFrameHandler.class));
    }
}

```



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
		id: 'iabPIXdOOF5DUlAG',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

