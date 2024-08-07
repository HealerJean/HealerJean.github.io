---
title: Ubuntu安装Nginx以及上传限制
date: 2018-07-05 03:33:00
tags: 
- Nginx
category: 
- Nginx
description: Ubuntu安装Nginx以及上传限制
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           




## 1、安装

Ubuntu安装Nginx很简单的

```
sudo apt-get install nginx
```

## 2、配置文件位置

需要注意的是Ubuntu安装的Nginx配置文件其实对我们来来说是有两个的

一个是`nginx.conf ` 配置上传的限制大小等
一个是`/etc/nginx/sites-available/default` 配置正常的属性server



![WX20180717-175813@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180717-175813@2x.png)


## 3、启动

```
 nginx -c /etc/nginx/nginx.conf
```

centos 启动是 

```
./nginx -c conf/nginx.conf

```


## 4、上传限制

上传文件的大小超出了 Nginx 允许的最大值，如果没有配置的话，默认是1M；


```xml
	client_max_body_size 100m; 上传限制为 100m


http {	
	sendfile on;
	tcp_nopush on;
	tcp_nodelay on;
	keepalive_timeout 65;
	types_hash_max_size 2048;
	
	include /etc/nginx/mime.types;
	default_type application/octet-stream;
	
	client_max_body_size 100m; 上传限制为 100m

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
		id: 'dw8Om763GOjyZvsT',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

