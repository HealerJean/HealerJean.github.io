---
title: 1、SpringBoot获取Bean
date: 2018-03-15 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot获取Bean
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

对于没有熟悉springBoot的小白来说，可能还在局限于spring上下文获取bean。其实SpringBoot也一样的

## 1、第一种通过工具类获取


获取方式

```
APIAdminUserService userService = SpringHelper.getBean(APIAdminUserService.class);

```
工具类代码
```
package com.hlj.netty.websocket.helper;

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
    private static ApplicationContext context = null;

    public SpringHelper() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class clazz) throws BeansException {
        return context.getBean((Class<T>) clazz);
    }

    public static <T> T getBean(String clazz) throws BeansException {
        return (T) context.getBean(clazz);
    }

    public static Object getBeanByName(String beanName) throws BeansException {
        return context.getBean(beanName);
    }
}


```

## 2、直接通过注入获取


```
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
		id: 'iabPIXdOOF5DUlAG',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

