---
title: SpringBoot监听启动耗时类
date: 2021-08-01 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot监听启动耗时类
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



```java


import com.google.common.collect.Sets;
import com.jd.merchant.export.common.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SpringbeanAnalyse
 *
 * @author zhangyujin
 * @date 2023/6/10  12:53.
 */
@Slf4j
@Component
public class SpringBeanCostMetricUtils implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {


    /**
     * PACKET_PATHS
     */
    private static final Set<String> PACKET_PATHS = Sets.newHashSet(
            "com.healejean.project.service",
            "com.healejean.project.web"
    );

    /**
     * beanTimeMap
     */
    private TreeMap<String, Long> beanTimeMap = new TreeMap<>();

    /**
     * started
     */
    private static volatile AtomicBoolean started = new AtomicBoolean(false);


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws
            BeansException {
        String className = bean.getClass().getName();
        boolean collectFlag = PACKET_PATHS.stream().anyMatch(path -> className.startsWith(path));
        if (collectFlag) {
            beanTimeMap.put(beanName, System.currentTimeMillis());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws
            BeansException {
        Long begin = beanTimeMap.get(beanName);
        if (begin != null) {
            beanTimeMap.put(beanName, System.currentTimeMillis() - begin);
        }
        return bean;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (started.compareAndSet(false, true)) {
            for (Map.Entry<String, Long> entry : beanTimeMap.entrySet()) {
                if (entry.getValue().compareTo(NumberConstant.ONE_HUNDRED.longValue()) >= NumberConstant.ZERO)
                    log.info("[SpringBeanCostMetric] beanName:{}, cost:{}ms", entry.getKey(), entry.getValue());
            }
            beanTimeMap.clear();
        }
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
		id: '7ozQLvxEOiRyq0gD',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->



