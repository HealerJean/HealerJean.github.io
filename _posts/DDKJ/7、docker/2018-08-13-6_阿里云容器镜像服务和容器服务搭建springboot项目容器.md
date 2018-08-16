---
title: 阿里云容器镜像服务和容器服务搭建springboot项目容器
date: 2018-08-13 03:33:00
tags: 
- Docker
category: 
- Docker
description: 阿里云容器镜像服务和容器服务搭建springboot项目容器
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


### 1、开通阿里云容器镜像服务

#### 1.1、创建命名空间 

![WX20180815-144234@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-144234@2x.png)


#### 1.2、创建镜像仓库(这里不需要执行，以为我使用代码登录的时候就会自动创建)

创建的仓库名称（不需要创建，在下面上传代码的时候，我们根据sh命令中的信息会进行创建，当然我们也可以自行创建）

![WX20180815-144409@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-144409@2x.png)



![WX20180815-145050@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145050@2x.png)


#### 2.1、sh命令


```
PREFIX=registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker
PROJECT=com-hlj-springboot-docker
tag=1


docker login --username=HealerJean registry.cn-qingdao.aliyuncs.com --password=123456789


echo "清理项目..."
mvn clean install
#cd  ${PROJECT}/
echo "开始打包${PROJECT}..."
mvn package

echo "删除之前保留在电脑中的版本..."
docker images | grep registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker | xargs docker rmi

echo "开始构建..."
docker build -t ${PREFIX}:${tag} .

echo "${PROJECT}构建成功，开始上传至阿里云"
docker push ${PREFIX}:${tag}

echo "镜像${PROJECT}构建并上传至阿里云成功"



```


#### 2.2、Dockerfile 文件（这里需要注意一下）

<font color="red">第一次上传From写java:8，，因为我们的镜像仓库中是空的，没有基础镜像， 表示使用 Jdk8 环境 为基础镜像，如果镜像不是本地的会从 DockerHub 进行下载，</font>

```
FROM java:8
MAINTAINER HealerJean
ADD target/com-hlj-springboot-docker-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","/app.jar"]

```

#### 2.3、执行命令


```
删除之前版本...
Error: No such image: registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker:latest
开始构建...
Sending build context to Docker daemon  16.41MB
Step 1/4 : FROM java:8
 ---> d23bdf5b1b1b
Step 2/4 : MAINTAINER HealerJean
 ---> Running in e526e7154026
Removing intermediate container e526e7154026
 ---> 8c8985e663a9
Step 3/4 : ADD target/com-hlj-springboot-docker-0.0.1-SNAPSHOT.jar app.jar
 ---> ab8d3e75b4d8
Step 4/4 : CMD ["java","-jar","/app.jar"]
 ---> Running in 252ce7fac08b
Removing intermediate container 252ce7fac08b
 ---> c9b3b1f00fa9
Successfully built c9b3b1f00fa9
Successfully tagged registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker:latest
com-hlj-springboot-docker构建成功，开始上传至阿里云
The push refers to repository [registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker]
955c377870ab: Pushed 
35c20f26d188: Layer already exists 
c3fe59dd9556: Layer already exists 
6ed1a81ba5b6: Layer already exists 
a3483ce177ce: Layer already exists 
ce6c8756685b: Layer already exists 
30339f20ced0: Layer already exists 
0eb22bfb707d: Layer already exists 
a2ae92ffcd29: Layer already exists 
latest: digest: sha256:218917c3411a807842ca66f6bb8baccd29b45746d49524611584271d3605c465 size: 2212
镜像com-hlj-springboot-docker构建并上传至阿里云成功

```


#### 2.4、观察浏览器发现多出来一个镜像仓库，观察仓库的版本信息


![WX20180815-145708@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145708@2x.png)


![WX20180815-145748@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145748@2x.png)


#### 2.5、修改Dockerfile 文件的From ，修改为基础镜像为阿里云仓库镜像,设置为标签为 1 ，这个镜像我们在阿里云上不要删除，否则执行命令的时候，会报错招不到该镜像


```
FROM registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker:1
MAINTAINER HealerJean
ADD target/com-hlj-springboot-docker-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","/app.jar"]

```



```
删除之前版本...
Untagged: registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker:latest
Untagged: registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker@sha256:4e44ea57cd18e70bce93ba8609dd5dee9721f62b02cdb34fdeacc0b2049c21ea
Deleted: sha256:8627ddcbf358330568a54d786dc27e2d6948759a2e55257620af761bec30650d
Deleted: sha256:b4c461e3c679783a8015e04b36ad00daf592c590b17ceac84597dd12b818bf9e
Deleted: sha256:3642a8cf34b64fc809ba03a381626d61e35dbced37429f8e288f42827049a220
Deleted: sha256:45d1e620ebd673688433d8dc1b14fc100a5650c702224b288d520fe8d71a7d7f
开始构建...
Sending build context to Docker daemon  16.41MB
Step 1/4 : FROM registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker:latest
latest: Pulling from duodianyouhui/com-hlj-springboot-docker

```


## 3、观察阿里云镜像信息

![WX20180815-145708@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145708@2x-1.png)
![WX20180815-145748@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145748@2x-1.png)

![WX20180815-145050@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145050@2x-1.png)

### 3.2、修改仓库类型为公开，否则后面容器服务不能够搭建web成功


![WX20180816-111017@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-111017@2x.png)


![WX20180816-111049@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-111049@2x.png)


## 4、容器服务启动web应用尽心访问

### 4.1、创建应用

![WX20180816-111237@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-111237@2x.png)

### 4.2、不勾选，检测最新，防止我们发布版本的时候，出现错误的版本（通过上面我们可以看到版本是一模一样的，都是latest，当然这个我们可以自己选择版本是最好的，这里是为了方便，最好是版本我们可以自己进行输入，第二章节会讲）

![WX20180816-111324@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-111324@2x.png)


### 4.3、使用镜像进行创建


#### 4.3.1、选择镜像

![WX20180816-111552@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-111552@2x.png)


#### 4.3.2、端口映射


[简单路由-域名配置](https://help.aliyun.com/document_detail/25986.html?spm=5176.2020520152.220.1.419116ddS5MyNX)


主机端口为空，表示随机暴露一个主机的端口（暴露 HTTP/HTTPS 服务时，您可以不需要知道主机暴露的具体端口是什么，可以使用 overlay 网络或者 VPC 网络来直接访问容器的端口），容器端口为 8080。您使用 wordpress-web 服务的 8080 端口来提供 HTTP 服务，使用的协议是 TCP 协议。，但是这里我们还是最好制定一个端口吧，主机端口也是8080 ,<font color="red">最好不要用80端口，小心出问题吧</font>


![WX20180816-120927@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-120927@2x.png)



#### 4.3.3、简单路由配置（配置域名）（写不写http://都可以）

[简单路由（支持 HTTP/HTTPS)](https://help.aliyun.com/document_detail/25984.html?spm=5176.2020520152.220.2.419116ddS5MyNX)

##### 1、配置阿里云提供的子域名


```
springboot;http://springboothttp;http://rongqi.dangqugame.cn;httprongqi.dangqugame.cn

观察有没有http;//会不会报错

阿里云提供的子域名服务
springboot;
http://springboothttp;

我们自己的域名
http://rongqi.dangqugame.cn; 
httprongqi.dangqugame.cn
```


![WX20180816-121207@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-121207@2x.png)



##### 2、配置自己提供的域名

1、集群，找到这个集群容器

![WX20180816-113417@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-113417@2x.png)

2、查看这个容器的负载均衡id


```
lb-m5eezyl4kqkux3

```

![WX20180816-113800@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-113800@2x.png)


3、打开负载均衡控制台，通过这个id查看ip地址，然后域名这个ip分配过去


![WX20180816-114028@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-114028@2x.png)



#### 4.3.4、创建成功

![WX20180816-115503@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-115503@2x.png)


##### 1、观察该应用所有域名（可以观察到有我们自己设置的，也有阿里云给我们提供的）

![WX20180816-120040@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-120040@2x.png)



##### 2、重新配置

1、 服务->变更配置

![WX20180816-121324@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-121324@2x.png)


![WX20180816-121351@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-121351@2x.png)


## 5、更新版本

1、如果所有的使用现在的tag:1版本，则每次从本地发布到阿里云镜像文件之后，点击重新部署即可自动完成发布新版本，但是回退版本是不可能的事情了，

![WX20180816-122813@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180816-122813@2x.png)

2、如果想每次的旧版本不删除，则我们在打包命令的时候，taq就要发生变化，tag变成日期的格式


```
tag=1
#tag=`date +%m%d%H%M`
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
		id: 'EhMnaN2Uz1KXupF3',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

