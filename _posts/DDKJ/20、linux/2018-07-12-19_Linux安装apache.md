---
title: Linux安装apache
date: 2018-07-12 03:33:00
tags: 
- Linux
category: 
- Linux
description: Linux安装apache
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


准备：卸载自带的httpd服务

1、检查该环境中是否已经存在httpd服务的配置文件，默认存储路径：/etc/httpd/httpd.conf（这是centos预装的Apache的一个ent版本，一般我们安装源代码版的Apache）。如果已经存在/etc/httpd/httpd.conf，请先卸载或者关闭centos系统自带的web服务，执行命令：chkconfig  httpd off，再或者把centos自带的httpd服务的80端口改为其他端口，Apache服务的端口不能出现冲突情况。


```
寻找文件命令：find / -name httpd.conf 
停止并卸载Linux系统自带的httpd服务：
1、service httpd stop
2、ps -ef | grep httpd
3、kill -9 pid号（逐个删除）
4、rpm -qa |grep httpd
5、rpm -e httpd软件包
```



## 1、下载Apache安装包（httpd-2.4.3.tar.gz）
[下载地址](http://httpd.apache.org/)


## 2、安装

```
	./configure --prefix=/usr/local/apache2 --with-apr=/usr/local/apr --with-apr-util=/usr/local/apr-util/ --with-pcre=/usr/local/pcre （除了指定Apache的安装目录外，还要安装apr、apr-util、pcre，并指定参数）    make && make install

```

## 3、安装出错

错误描述：在编译Apache(在安装httpd-2.4.3时遇到的问题)时分别出现了apr not found、APR-util not found、pcre-config for libpcre not found的问题，


```
http://apr.apache.org/download.cgi  下载apr-1.4.5.tar.gz、apr-util-1.3.12.tar.gz
http://sourceforge.net/projects/pcre/files/latest/download 下载pcre-8.31.zip
```



### 1.解决apr not found问题 

```
tar -zvxf apr-1.4.5.tar.gz 
./configure --prefix=/usr/local/apr 
make && make install

```
   
## 2.解决APR-util not found问题

```
tar -zvxf apr-util-1.3.12.tar.gz 
./configure --prefix=/usr/local/apr-util -with-apr=/usr/local/apr/bin/apr-1-config 

make && make install

```
 
### 3、解决pcre-config for libpcre not found问题


```
unzip pcre-8.31.zip
cd pcre-8.31 ./configure --prefix=/usr/local/pcre 

make && make install

```
 
## 2、开始安装吧

```
./configure --prefix=/usr/local/apache --with-apr=/usr/local/apr --with-apr-util=/usr/local/apr-util --with-pcre=/usr/local/pcre

make && make install

```

## 3、启动停止

```
• 启动Apache：/usr/local/apache2/bin/apachectl start
• 停止Apache：/usr/local/apache2/bin/apachectl stop
• 重启Apache：/usr/local/apache2/bin/apachectl restart
```

## 测试
网站放在/usr/local/apache2/htdocs目录下

[http://localhost:80](http://localhost:80)

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
		id: 'rh9X8ZiGhjIf8NES',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

