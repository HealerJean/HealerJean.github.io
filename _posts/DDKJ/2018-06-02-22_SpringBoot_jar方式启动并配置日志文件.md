---
title: 1、SpringBoot_jar方式启动并配置日志文件
date: 2018-06-02 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot_jar方式启动并配置日志文件
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

正常启动 ,下面会选择`application.properties` 中配置默认的启动文件进行启动，下面这种情况不能根据实际情况进行启动项目

```
java jar admin-1.0-SNAPSHOT.jar 

```


## 1、测试环境和生产环境启动


```
主要观察priperties中配置文件的名字

测试环境：java -jar my-spring-boot.jar --spring.profiles.active=test  

生产环境：java -jar my-spring-boot.jar --spring.profiles.active=prod  

```

## 2、利用shell启动并配置log日志


#### 2.1、测试库启动

```
#!/bin/bash
   echo starting
   java -jar admin-1.0-SNAPSHOT.jar --spring.profiles.active=test  > log.file 2>log.error &

```

#### 2.2、正式库启动


```
#!/bin/bash
   echo starting
   java -jar admin-1.0-SNAPSHOT.jar --spring.profiles.active=prod > log.file 2>&1 &
     
```



#### 2.3、停止正在运行的项目


```
#!/bin/bash
   PID=$(ps -ef | grep admin-1.0-SNAPSHOT.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then

    echo Application is already stopped
else

    echo kill $PID

    kill $PID
fi


```


#### 2.4、重启项目


```
#!/bin/bash

echo stop application

source stop.sh

echo start application

source start.sh

```


## 3、很简答吧


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
		id: 't6TT8dlSAcO5WxXH',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

