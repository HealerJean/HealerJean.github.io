---
title: SpringBoot_之_自定义Enable注解
date: 2022-12-12 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot_之_自定义Enable注解
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

# 1、`@Import`注解再次说明

> spring boot 的@Import注解可以配置三种不同的class，根据不同的场景来选择不同的注入方式
>
> 1、普通的`bean` 或者 带有 `@Configuration的bean` 直接注入     
>
> 2、实现`ImportSelector`接口注入     
>
> 3、实现` ImportBeanDefinitionRegistrar`接口注入



## 1.1、普通的`bean` 注入 

```java
@Slf4j
public class LoggerEnableService {
    public void saveLog(String logMsg){
        log.info("[CounterEnableService#saveLog] logMsg:{}", logMsg);
    }
}

```











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
		id: 'xsh42Xq7BQ0bJCEl',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



