---
title: 阿里云搭建springboot项目容器
date: 2018-08-13 03:33:00
tags: 
- Docker
category: 
- Docker
description: 阿里云搭建springboot项目容器
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


#### 1.2、创建镜像仓库

![WX20180815-144409@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-144409@2x.png)

#### 1.3、创建的仓库名称（不需要创建，在下面上传代码的时候，我们根据sh命令中的信息会进行创建，当然我们也可以自行创建）


### 2、开始打包上传代码

![WX20180815-145050@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180815-145050@2x.png)


#### 2.1、sh命令


```
PREFIX=registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker
PROJECT=com-hlj-springboot-docker
tag=latest



docker login --username=HealerJean registry.cn-qingdao.aliyuncs.com --password=AAAAAAAAA




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


#### 2.5、修改Dockerfile 文件的From ，修改为基础镜像为阿里云仓库镜像


```
FROM registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker:latest
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

