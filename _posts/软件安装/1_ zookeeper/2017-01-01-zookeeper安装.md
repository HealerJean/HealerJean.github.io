---
title: zookeeper安装
date: 2017-01-01 03:33:00
tags: 
- InstallSoftWare
category: 
- InstallSoftWare
description: zookeeper安装
---



# 1、Mac安装zookeeper



## 1.1、下载地址

> [https://downloads.apache.org/zookeeper/zookeeper-3.6.2/](https://downloads.apache.org/zookeeper/zookeeper-3.6.2/)

![image-20210131175743203](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210131175743203.png)

## 1.2、启动前配置

1、修改目录下conf配置文件`zoo_sample.cfg`为`zoo.cfg`名字



![image-20210131180038412](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210131180038412.png)



2、编辑`zoo.cfg`，自定义`dataDir`目录

![image-20210131180224287](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210131180224287.png)





## 1.3、启动

```
./zkServer.sh start
```

```
healerjean@MAC bin % pwd
/Users/healerjean/Desktop/software/apache-zookeeper-3.6.2-bin/bin
healerjean@MAC bin % ./zkServer.sh start
/usr/bin/java
ZooKeeper JMX enabled by default
Using config: /Users/healerjean/Desktop/software/apache-zookeeper-3.6.2-bin/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```





## 1.4、停止

```
./zkServer.sh stop
```

```
healerjean@MAC bin % ./zkServer.sh stop
/usr/bin/java
ZooKeeper JMX enabled by default
Using config: /Users/healerjean/Desktop/software/apache-zookeeper-3.6.2-bin/bin/../conf/zoo.cfg
Stopping zookeeper ... STOPPED
```













