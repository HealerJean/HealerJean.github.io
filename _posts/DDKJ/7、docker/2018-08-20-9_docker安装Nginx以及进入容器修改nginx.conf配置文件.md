---
title: docker安装_进入容器修改nginx.conf配置文件_宿主主机挂载（nginx举例）
date: 2018-08-20 03:33:00
tags: 
- Docker
category: 
- Docker
description: docker安装_进入容器修改nginx.conf配置文件_宿主主机挂载（nginx举例）
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

## 2、进入这个容器

### 2.1、docker exec ：在运行的容器中执行命令

```
		-d :分离模式: 在后台运行
		-i :即使没有附加也保持STDIN（标准输入） 打开,以交互模式运行容器，通常与 -t 同时使用；
		-t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用； 

docker exec -it 9fbe362214a6  /bin/bash 
```


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

## 3、docker cp


#### 3.1、将容器29df10f32d44:/etc/nginx/nginx.conf 目录拷贝到主机/Users/healerjean/Desktop目录下


```
:~ healerjean$  docker cp  29df10f32d44:/etc/nginx/nginx.conf /Users/healerjean/Desktop

```

#### 3.2、将主机/Users/healerjean/Desktop/AAA.md 拷贝到容器29df10f32d44:/etc/nginx/中


```
 docker cp /Users/healerjean/Desktop/AAA.md 29df10f32d44:/etc/nginx/
```


```

:~ healerjean$ docker cp /Users/healerjean/Desktop/AAA.md 29df10f32d44:/etc/nginx/


:~ healerjean$ docker exec -it 29df10f32d44 /bin/bash
root@29df10f32d44:/# cd /etc/nginx/
root@29df10f32d44:/etc/nginx# ls -l
total 44
-rw-r--r-- 1  501 dialout 1519 Jul 27 08:08 AAA.md
drwxr-xr-x 1 root root    4096 Aug 28 07:10 conf.d
-rw-r--r-- 1 root root    1007 Jul 24 13:02 fastcgi_params
-rw-r--r-- 1 root root    2837 Jul 24 13:02 koi-utf
-rw-r--r-- 1 root root    2223 Jul 24 13:02 koi-win
-rw-r--r-- 1 root root    5231 Jul 24 13:02 mime.types
lrwxrwxrwx 1 root root      22 Jul 24 13:02 modules -> /usr/lib/nginx/modules
-rw-r--r-- 1 root root     643 Jul 24 13:02 nginx.conf
-rw-r--r-- 1 root root     636 Jul 24 13:02 scgi_params
-rw-r--r-- 1 root root     664 Jul 24 13:02 uwsgi_params
-rw-r--r-- 1 root root    3610 Jul 24 13:02 win-utf
root@29df10f32d44:/etc/nginx# 



```


## 4、 -v 挂在本地目录到容器中


#### 4.1、建议启动的时候挂载 ：:ro 表示分配给只读权限（这样容器就可以使用宿主主机的目录了）


```
docker run -p 80:80  -v /Users/healerjean/Desktop:/usr/local/mynginx:ro -d nginx
```

```
JeandeMBP:~ healerjean$ docker run -p 80:80 -it -v /Users/healerjean/Desktop:/usr/local/mynginx:ro -d nginx 
f3a39301086b999f4bcd9ccddcd83672007be0280889c3f4f0c1fc6bc45b0db4
JeandeMBP:~ healerjean$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                NAMES
f3a39301086b        nginx               "nginx -g 'daemon of…"   2 seconds ago       Up 7 seconds        0.0.0.0:80->80/tcp   sharp_varahamihira
JeandeMBP:~ healerjean$ docker exec -it f3a39301086b /bin/bash
root@f3a39301086b:/# cd /usr/local/mynginx/
root@f3a39301086b:/usr/local/mynginx# ls -l
total 35824
-rw-r--r--  1 root root    19915 Jan 25  2018 1???.docx
-rw-r--r--  1 root root 18885250 Aug 28 06:58 2018-07.xlsx
-rw-r--r--  1 root root 17711506 Aug 28 06:55 2018-08.xlsx
-rw-r--r--  1 root root     1519 Jul 27 08:08 AAA.md
-rw-r--r--  1 root root        0 Jul 17 09:05 AAA.sql
-rw-r--r--  1 root root        0 Jul 26 09:22 AAAAA.txt
-rw-r--r--  1 root root      972 Mar  8 10:56 DDKJ
-rw-r--r--  1 root root      671 Aug 11 07:00 HttpHelper.java
-rw-r--r--  1 root root      936 Mar  6 06:43 _posts
-rw-r--r--  1 root root     1542 Aug 24 09:01 default
drwxr-xr-x  4 root root      136 Aug 16 07:42 images
drwx------ 20 root root      680 Aug 28 03:21 study
drwxr-xr-x 15 root root      510 Aug 20 11:11 workspace
drwxr-xr-x 23 root root      782 Aug  2 03:59 youhui-h5
root@f3a39301086b:/usr/local/mynginx# 

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

