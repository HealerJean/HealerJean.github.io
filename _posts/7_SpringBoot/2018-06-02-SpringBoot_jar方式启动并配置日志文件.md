---
title: 1、SpringBoot_jar方式启动并配置日志文件
date: 2018-06-02 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot_jar方式启动并配置日志文件
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            



正常启动 ,下面会选择`application.properties` 中配置默认的启动文件进行启动，下面这种情况不能根据实际情况进行启动项目

```shell
java jar admin-1.0-SNAPSHOT.jar 

```

解压


```java
java -xvf admin-1.1-SNAPSHOT.jar

```


## 1、测试环境和生产环境启动


```shell
主要观察priperties中配置文件的名字

测试环境：java -jar my-spring-boot.jar --spring.profiles.active=test  

生产环境：java -jar my-spring-boot.jar --spring.profiles.active=prod  

```

## 2、利用shell启动并配置log日志


#### 2.1、测试库启动

```shell
#!/bin/bash
   echo starting
   java -jar admin-1.0-SNAPSHOT.jar --spring.profiles.active=test  > log.file 2>log.error &

```

#### 2.2、正式库启动


```shell
#!/bin/bash
   echo starting
   java -jar admin-1.0-SNAPSHOT.jar --spring.profiles.active=prod > log.file 2>&1 &
     
```



#### 2.3、停止正在运行的项目


```shell
#!/bin/bash

check_results=`ps -aux | grep "admin-1.0-SNAPSHOT.jar" | grep "spring"   |awk '{print $2}'`


if [ ! -n "$check_results"   ]; then

  echo "the web never find"
else

  echo "command(ps -aux) results are: $check_results"
  kill -9  $check_results  
  echo "the web have been stop : $check_results"

fi

```


#### 2.4、重启项目


```shell
#!/bin/bash

echo stop application

source stop.sh

echo start application

source start.sh

```


## 3、很简答吧





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
		id: 't6TT8dlSAcO5WxXH',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

