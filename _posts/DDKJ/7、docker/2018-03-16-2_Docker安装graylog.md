---
title: Docker安装graylog
date: 2018-03-16 14:33:00
tags: 
- Docker
- GrayLog
category: 
- Docker
- GrayLog
description: Docker安装graylog
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
-->

## 前言

graylog我就不想介绍了，如果你看到graylog就知道它是一个日志存储服务器web

## 准备
 安装docker
 安装docker compose
 

## 1、安装graylog

### 1、创建目录<br/>

`/usr/local/graylog `

该目录下新建文件   `graylog.yml` 或者 `compose.yml` 

写入内容：

```
version: '2'
services:
  # MongoDB: https://hub.docker.com/_/mongo/
  mongodb:
    image: mongo:3
  # Elasticsearch: https://www.elastic.co/guide/en/elasticsearch/reference/5.6/docker.html
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:5.6.3
    environment:
      - http.host=0.0.0.0
      - transport.host=localhost
      - network.host=0.0.0.0
      # Disable X-Pack security: https://www.elastic.co/guide/en/elasticsearch/reference/5.6/security-settings.html#general-security-settings
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    mem_limit: 1g
  # Graylog: https://hub.docker.com/r/graylog/graylog/
  graylog:
    image: graylog/graylog:2.4.0-1
    environment:
      # CHANGE ME!
      - GRAYLOG_PASSWORD_SECRET=somepasswordpepper
      # Password: admin
      - GRAYLOG_ROOT_PASSWORD_SHA2=8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
      - GRAYLOG_WEB_ENDPOINT_URI=http://127.0.0.1:9000/api
    links:
      - mongodb:mongo
      - elasticsearch
    depends_on:
      - mongodb
      - elasticsearch
    ports:
      # Graylog web interface and REST API
      - 9000:9000
      # Syslog TCP
      - 514:514
      # Syslog UDP
      - 514:514/udp
      # GELF TCP
      - 12201:12201
      # GELF UDP
      - 12201:12201/udp

```

### 2、开始安装

1、如果创建的文件名字是`graylog.yml` 使用命令 （-d表示后台运行）

```
sudo docker-compose -f graylog.yml up -d
```
2、如此创建的文件是 `compose.yml` 

```
docker-compose up -d 
```


## 2、浏览器中打开,密码admin/admin

[ http://localhost:9000/]( http://localhost:9000/)

![WX20180316-141255@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-141255@2x.png)



## 3、测试

### 3.1、查看运行的镜像 协议观察，可以看到12201 udp这样才算成功

`docker ps`

![WX20180316-141745@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-141745@2x.png)

### 3.2、使用http测试是否成功

#### 3.2.1、新建input http input

![WX20180316-141909@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-141909@2x.png)


#### 3.2.2、打开一个终端，输入下面的命令

```
curl -XPOST http://localhost:12201/gelf -p0 -d '{"message":"hello这是一条消息", "host":"127.0.0.1", "facility":"test", "topic": "meme"}'
```
打开浏览器观察信息,点击http-input之后


![WX20180316-142346@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180316-142346@2x.png)




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
		id: 'AAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

