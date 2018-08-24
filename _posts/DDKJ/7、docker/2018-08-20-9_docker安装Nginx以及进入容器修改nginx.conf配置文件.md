---
title: docker安装Nginx以及进入容器修改nginx.conf配置文件
date: 2018-08-14 03:33:00
tags: 
- Docker
category: 
- Docker
description: docker安装Nginx以及进入容器修改nginx.conf配置文件
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


## 1、docker安装Nginx

### 1.1、观察nginx是否可下载

```
healerjean$ docker search nginx

```

### 1.2、开始安装


```
healerjean$ docker pull nginx
```
## 2、先运行这个nginx



```
healerjean$ docker run -it -p 80:80 nginx

```

### 3.1、查看这个nginx的容器id号


```
docker ps

healerjean$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED                  STATUS              PORTS                NAMES
9fbe362214a6        nginx               "nginx -g 'daemon of…"   Less than a second ago   Up 2 seconds        0.0.0.0:80->80/tcp   objective_chandrasekhar
healerjean$ 

```

### 3.2、进入这个容器


```
docker exec -it 9fbe362214a6  /bin/bash 



healerjean$ docker exec -it 9fbe362214a6  /bin/bash 
root@9fbe362214a6:/# ls -l
total 64
drwxr-xr-x   2 root root 4096 Jul 16 00:00 bin
drwxr-xr-x   2 root root 4096 Jun 26 12:03 boot
drwxr-xr-x   5 root root  360 Aug 15 10:12 dev
drwxr-xr-x   1 root root 4096 Aug 15 10:12 etc
drwxr-xr-x   2 root root 4096 Jun 26 12:03 home
drwxr-xr-x   1 root root 4096 Jul 16 00:00 lib
drwxr-xr-x   2 root root 4096 Jul 16 00:00 lib64
drwxr-xr-x   2 root root 4096 Jul 16 00:00 media
drwxr-xr-x   2 root root 4096 Jul 16 00:00 mnt
drwxr-xr-x   2 root root 4096 Jul 16 00:00 opt
dr-xr-xr-x 185 root root    0 Aug 15 10:12 proc
drwx------   2 root root 4096 Jul 16 00:00 root
drwxr-xr-x   1 root root 4096 Aug 15 10:12 run
drwxr-xr-x   2 root root 4096 Jul 16 00:00 sbin
drwxr-xr-x   2 root root 4096 Jul 16 00:00 srv
dr-xr-xr-x  13 root root    0 Aug 15 10:12 sys
drwxrwxrwt   1 root root 4096 Jul 24 17:21 tmp
drwxr-xr-x   1 root root 4096 Jul 16 00:00 usr
drwxr-xr-x   1 root root 4096 Jul 16 00:00 var
   
root@9fbe362214a6:/# cd /etc/nginx/conf.d/
root@9fbe362214a6:/etc/nginx/conf.d# ls -l
total 4
-rw-r--r-- 1 root root 1093 Jul 24 13:02 default.conf

```







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
		id: 'mHbBiN7OQRZLFj2P',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

