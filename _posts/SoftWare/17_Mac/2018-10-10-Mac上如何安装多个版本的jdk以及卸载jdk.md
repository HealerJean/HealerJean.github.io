---
title: mac上如何安装多个版本的jdk以及卸载jdk
date: 2018-10-10 03:33:00
tags: 
- Mac
category: 
- Mac
description: mac上如何安装多个版本的jdk以及卸载jdk
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



# 一、安装

## 1、直接安装
> `dmp`包直接安装两个版本的jdk，比如我这里安装的`1.8.0_181` 、`10.0.2`。

![WX20181009-112351@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181009-112351@2x.png)

## 2、配置环境

> 打开环境变量配置文件`bash_profile `
>


```sh
vim ~/.bash_profile 


# java
export JAVA_8_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home"
export JAVA_10_HOME="/Library/Java/JavaVirtualMachines/jdk-10.0.2.jdk/Contents/Home"

alias jdk8="export JAVA_HOME=$JAVA_8_HOME"
alias jdk10="export JAVA_HOME=$JAVA_10_HOME"

export JAVA_HOME=$JAVA_8_HOME
export PATH="$JAVA_HOME:$PATH"


source ~/.bash_profile

```

## 3、任意切换 `java` 环境
> 控制台输入 `jdk8` 或者 `jdk10` 就会自动切换


```sh
JeandeMBP:~ healerjean$ jdk8
JeandeMBP:~ healerjean$ java -version
java version "1.8.0_181"
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)



JeandeMBP:~ healerjean$ jdk10
JeandeMBP:~ healerjean$ java -version
java version "10.0.2" 2018-07-17
Java(TM) SE Runtime Environment 18.3 (build 10.0.2+13)
Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10.0.2+13, mixed mode)

```


## 4、删除 `jdk`


```sh
#输入 
sudo rm -fr /Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin  sudo rm -fr /Library/PreferencesPanes/JavaControlPanel.prefpane


#查找当前版本 输入：ls /Library/Java/JavaVirtualMachines/ 
#输出：jdk1.8.0_181.jdk

sudo rm -rf /Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk
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
		id: '3iLbrBRsYwpG4yUg',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

