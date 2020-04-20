---
title: SpringBoot项目创建Resource文件夹
date: 2019-05-13 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot项目创建Resource文件夹
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            



针对于刚刚新创建的maven项目中没有resource文件夹，当我在设计一个service接口层的时候，没有考虑过放入资源文件，所以一开始没有创建resource文件夹，但是后来需要做一个freemarker模板项目，我需要一个文件夹来存放静态资源html模板文件



#### 1、设置-> Project Structer

![1557727708927](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727708927.png)

#### 2、Modules-->Sources-->main右键-->New Folder, 输入resources

![1557727726716](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727726716.png)



![1557727740146](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727740146.png)



#### 3、选择resources右键，点击Resources（选择文件资源类型），然后Apply，OK即可（添加test也一样）

![1557727765572](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727765572.png)



#### 4、成功

![1557727779133](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1557727779133.png)



#### 5、pom引入resource

```xml

</build>
	<resources>
		<resource>
			<directory>src/main/i18n</directory>
		</resource>
		<resource>
			<directory>src/main/resources</directory>
		</resource>
		<resource>
			<directory>src/main/java</directory>
			<includes>
				<include>**/*.properties</include>
				<include>**/*.xml</include>
			</includes>
			<filtering>false</filtering>
		</resource>
	</resources>
</build>

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
		id: 'ngNBMJuIE8SZTmCj',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

