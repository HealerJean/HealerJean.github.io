---
title: 阿里云容器服务springboot版本回退_以及平滑更新蓝绿发布
date: 2018-08-15 03:33:00
tags: 
- Docker
category: 
- Docker
description: 阿里云容器服务springboot版本回退_以及平滑更新蓝绿发布
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


# 1、版本回退以及蓝绿发布

## 2、查看容器镜像版本

![WX20180824-150642@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-150642@2x.png)



## 3、比如我们想回退到版本`08241301`


### 3.1、点击应用 -> 变更配置

![WX20180824-150744@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-150744@2x.png)


### 3.2、之前的版本

```
dev-08241327:
  restart: always
  ports:
    - '8082:8082/tcp'
  environment:
    - LANG=C.UTF-8
    - JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
    - spring.profiles.active=dev
    - server.port=8082
  memswap_limit: 0
  labels:
    aliyun.scale: '1'
    aliyun.routing.port_8082: test.dangqugame.cn
  shm_size: 0
  image: 'registry-vpc.cn-qingdao.aliyuncs.com/healerjean/dev-server:08241327'
  memswap_reservation: 0
  kernel_memory: 0
  mem_limit: 0
```

### 3.3、需改之后的版本

#### 3.3.1、修改这个版本的名字 `dev-08241327` 为`dev-08241301`名字任意，只要不一样就可以

#### 3.3.2、修改端口号：即使我们项目中的端口号和我们这里要配置的不一样也可以，容器会自动伴我们将项目中的端口号改变的，如果不修改端口号就会造成端口冲突

```
1、服务器和容器暴露端口号
  ports:
    - '8081:8081/tcp'
2、容器中项目的端口
  environment:
    - server.port=8081
   labels:
3、http协议端口号暴露   
    aliyun.routing.port_8081: test.dangqugame.cn

```
#### 3.3.3、修改镜像的版本


```
 image: 'registry-vpc.cn-qingdao.aliyuncs.com/healerjean/dev-server:08241301'
```

#### 3.3.4、容器的个数（等于或者小于节点的个数：因为一个节点就是一个服务器，会开放一个相同端口） 下面这个表示2个容器，

```
    aliyun.scale: '2'

```


```
dev-08241301:
  restart: always
  ports:
    - '8081:8081/tcp'
  environment:
    - LANG=C.UTF-8
    - JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
    - spring.profiles.active=dev
    - server.port=8081
  memswap_limit: 0
  labels:
    aliyun.scale: '2'
    aliyun.routing.port_8081: test.dangqugame.cn
  shm_size: 0
  image: 'registry-vpc.cn-qingdao.aliyuncs.com/healerjean/dev-server:08241301'
  memswap_reservation: 0
  kernel_memory: 0
  mem_limit: 0
```


### 3.4、点击保存（忽略出现的警告）

#### 3.4.1、观察容器和日志，查看启动是否完成（下面之所以是因为我使用了2个容器）

![WX20180824-152139@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-152139@2x.png)


![WX20180824-152105@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-152105@2x.png)

#### 3.4.2、启动完成修改域名的权重比例
![WX20180824-152220@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-152220@2x.png)

#### 3.4.3、将旧版本的权重比例和新版本的权重比例进行对调

![WX20180824-152251@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-152251@2x.png)


#### 3.4.4、蓝绿发布完成或者撤销

1、当蓝绿发布完成并且显示正常的时候，点击确认发布完成，系统会自动将旧版本删除

2、当发现新版本有问题的时候，点击回滚，就会将我们刚刚发布的版本进行删除

![WX20180824-152519@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180824-152519@2x.png)




<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'yQ4cnDTuazekOY0I',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

