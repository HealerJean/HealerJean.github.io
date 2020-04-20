---
title: swagger生成pdf文档
date: 2019-02-20 03:33:00
tags: 
- Swagger
category: 
- Swagger
description: swagger生成pdf文档
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



## swagger.json

```http
http://localhost:8888/develop/swagger
```



![1569757062072](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569757062072.png)




### 1、项目1（生成pdf）




#### 1.1、生成

![1569756076132](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569756076132.png)



#### 1.2、结果目录



![1569756179075](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569756179075.png)



#### 1.3、文件内容



##### 1..3.1、html



![1569756151823](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569756151823.png)



##### 1.3.2、pdf

![1569756236955](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569756236955.png)





### 2、项目2（生成html和pdf）



#### 2.1、生成



```
mvn test
```

![1569755943508](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569755943508.png)



#### 2.2、结果目录

![1569755991946](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569755991946.png)



#### 2.3、文件内容

##### 2.3.1、html

![1569756409477](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569756409477.png)



##### 2.3.2、pdf

![1569756436682](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569756436682.png)



## [代码下载](https://github.com/HealerJean/HealerJean.github.io/tree/master/_posts/DDKJ/15%E3%80%81swagger/2019_09_29_2_swagger%E7%94%9F%E6%88%90pdf%E6%96%87%E6%A1%A3)





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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 
