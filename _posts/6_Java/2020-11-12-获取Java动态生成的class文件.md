---
title: 获取Java动态生成的class文件
date: 2020-11-12 03:33:00
tags: 
- Java
category: 
- Java
description: 获取Java动态生成的class文件
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、获取java进程的id

```shell
netstat -aon|findstr "端口号" 
```



# 2、查看JVM运行数据

 

```java
java -classpath "%JAVA_HOME%/lib/sa-jdi.jar" sun.jvm.hotspot.HSDB
```



1、点击file下面的Attach to hotSpot process

![image-20201112143643638](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201112143643638.png)



2、输入进程id：11944



![image-20201112143636315](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201112143636315.png)

3、点击Tools下的Class Browser,输入动态代理的class名称$Proxy77，查询后点击Create .class for this class，动态类的字节码文件成功创建

![image-20201112143622228](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201112143622228.png)



4、然后到当前cmd运行的目录找到$Proxy77.class，反编译得到Class内容



























![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





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
		id: '3pStufUOeXAHZ9qz',
    });
    gitalk.render('gitalk-container');
</script> 

