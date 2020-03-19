---
title: Ubuntu安装nginx_php7以及配置index.php启动
date: 2018-02-23 03:33:00
tags: 
- Linux
category: 
- Linux
description: Ubuntu安装nginx_php7以及配置index.php启动
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

上午接到一个活，要安装一个index.php，关于php，基本语法在大学的时候学习了2个礼拜。觉得php很简单的，当时本地环境用的额是apache，所以本来也想在公司的服务器上安装apache来配置启动，后来想想还是算了，已经有了nginx就用nginx吧。那么下面就是Ubuntu安装的过程了，很简答的哦


### 1、安装nginx

自己安装吧，不会的可以查找我之前的博客。


### 2、安装php



```shell

apt-get install php7.0 php7.0-fpm 

```

### 3、nginx中配置php文件的目录


```java

server {
    listen 80;
    server_name  test.healerjean.cn;
    location ~\.php$ {
            fastcgi_pass   127.0.0.1:9000;
            fastcgi_index  index.php;
            fastcgi_param  SCRIPT_FILENAME /usr/local/dataoke$fastcgi_script_name;
            include        fastcgi_params;
    }
    location / {
        root   /usr/local/dataoke;
        index  index.php;
    }
}


```

### 4、修改php默认监听

nginx将端口转发给php默认的9000端口。而通过apt-get安装的php默认使用本地sock文件通信，需要对php的配置进行修改。要修改的文件位于：/etc/php/7.0/fpm/pool.d/www.conf ，大概在36行。将


```
将 listen = /run/php/php7.0-fpm.sock
改成
listen = 127.0.0.1:9000

```

### 5、启动php

```shell
/etc/init.d/php7.0-fpm start

```

#### 5.1、观察是否启动成功,发现没有内容，说明是没有启动，对不起，我也不知道为什么，请往后看



```shell

netstat -ntpl | grep 9000

```


#### 5.2、php 5.3.3 以后的php-fpm 不再支持 php-fpm 以前具有的 php-fpm (start|stop|reload)等命令，所以不要再看这种老掉牙的命令了，需要使用信号控制：


```shell

master进程可以理解以下信号

INT, TERM 立刻终止
QUIT 平滑终止
USR1 重新打开日志文件
USR2 平滑重载所有worker进程并重新载入配置和二进制模块

root@-node4:/etc/php/7.0/fpm# ps -ef | grep php-fpm
www-data  1667 22344  0 16:16 ?        00:00:00 php-fpm: pool www
www-data  1668 22344  0 16:16 ?        00:00:00 php-fpm: pool www
root     15521  1556  0 16:31 pts/0    00:00:00 grep --color=auto php-fpm
root     22344     1  0 16:03 ?        00:00:00 php-fpm: master process (/etc/php/7.0/fpm/php-fpm.conf)

```
重启


```shell

重启php-fpm:

kill -USR2 22344

再次观察，成功显示
netstat -ntpl | grep 9000
tcp        0      0 127.0.0.1:9000          0.0.0.0:*               LISTEN      1667/php-fpm: pool 

```

## 6、浏览器中访问吧

关于出现下面问题的解决方法

![https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages20190328165623.png](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages20190328165623.png)

cms 版本：11
php 版本：7.0.33-0ubuntu0.16.04.2
curl 未开启,请先开启curl扩展，否则无法运行,请联系您的空间或者服务器提供商
mbstring 未开启,请先开启mbstring扩展，否则无法运行
cache 无效,请先设置目录读写权限,请联系您的空间或者服务器提供商


apt-cache search curl，等具体看版本

apt-get install php7.0-curl
apt-get install php7.0-mbstring
apt-get install php7.0-cache




<br/><br/><br/>
<font color="red" size="5"> 感兴趣的，欢迎添加博主微信， </font><br/>
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
		id: '06L9ze4F7CdWtkvR',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

