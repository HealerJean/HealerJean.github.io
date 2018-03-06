---
title: GitHub评论Gitalk插件
date: 2018-03-06 03:33:00
tags: 
- GitHub
category: 
- GitHub
description: GitHub评论Gitalk插件
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
-->
GitHub评论Gitalk插件

![WX20180306-155426@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180306-155426@2x.png)

## 1、Gitalk 需要一个 Github Application，
---
[点击这里申请](https://github.com/settings/applications/new)。
---

![WX20180306-153911@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180306-153911@2x.png)

## 2、将下面的代码复制到每个页面
---

```
<!-- Gitalk 评论 start  -->

<!-- Link Gitalk 的支持文件  -->
<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>     <script type="text/javascript">
    var gitalk = new Gitalk({

    // gitalk的主要参数
		clientID: `Github Application clientID`,
		clientSecret: `Github Application clientSecret`,
		repo: `Github 仓库名`,
		owner: 'Github 用户名',
		admin: ['Github 用户名'],
		id: 'indow.location.pathname', 注意id一定不要重复，这里是举个例子，可以写中文，如果重复了，就会把其他地方的评论显示过来
    
    });
    gitalk.render('gitalk-container');
</script> 
<!-- Gitalk end -->
```

## 3、打开上面我们注册的网址，复制 `clientID` 和 `秘clientSecret` 到上面
----
![WX20180306-154420@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180306-154420@2x.png)

## 4、评论插件预览，评论查看的位置 在GitHub的`issue`中
----
![WX20180306-155426@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180306-155426@2x.png)


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
		id: 'GitHub评论Gitalk插件',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

