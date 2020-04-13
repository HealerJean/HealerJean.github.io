---
title: Phabricator进行CodeReview
date: 2020-02-27 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Phabricator进行CodeReview
---



<!--
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/ 
　　首行缩进
-->






**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)    



# 1、软件安装

## 1.1、安装Git  



## 1.2、安装`php`

### 1.2.1、`window`安装 

> 查看我的另一篇文章windows 安装php文章 



## 1.3、安装`Arcanist`   



### 1.3.1、下载  

 

```shell
git clone https://github.com/phacility/libphutil.git #克隆工具库
git clone https://github.com/phacility/arcanist.git #克隆Arcanist
```





![1582789692324](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789692324.png)



### 1.3.2、环境变量配置  

![1582789774962](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789774962.png)



![1582789794391](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582789794391.png)



### 1.3.3、配置编辑器    

> 若显示“... was null”，则再执行一次命令：

**将编辑器配置为Notepad++**

```shell
arc set-config editor "\"C:\Program Files (x86)\Notepad++\notepad++.exe\" -multiInst -nosession"
```

**将编辑器配置为Sublime Text**

```shell
arc set-config editor "\"C:\Program Files\Sublime Text 3\sublime_text.exe\" -w -n"
```



### 1.3.4、 设置`phabricator`全局参数  

**配置全局参数，若显示“... was null”，则再执行一次命令：**

```shel
arc set-config default https://phabricator.d.healerjean.net/
```



**运行下面的命令，根据提示访问 [ https://phabricator.d.xiaomi.net/conduit/login/]( https://phabricator.d.xiaomi.net/conduit/login/)，把页面上的token复制/粘贴下来**

```shell
arc install-certificate
# 以上设置存储于 ~/.arcrc 中
```



**在~/.bashrc里添加下面两行** 

```shell
export EDITOR=vim
alias arc='LC_ALL=C arc'

```



配置生效

```shell
source ~/.bashrc
```





# 2、项目管理工具  





## 2.1、安装arcanist前的准备工作

> 在安装arcanist之前，确保以下，这样才能有权限的clone基于ssh协议的远程仓库：
>
> 1、将你的ssh-key上传到了phabricator：    
>
> 2、将$HOME/.ssh/id_rsa.pub文件的内容粘贴到phabricator的Setting>Personal Settings>Authentication>SSH public key中，



```
git clone ssh://git@phabricator.youdomain.com/diffusion/P/youproject.git
```



**//有机会再写一些phabricator配置性的东西** 









# 3、arcanist的使用



## 3.1、arcanist的作用  

> arcanist是phabricator创建的代码审核辅助工具，其作用参见图[1](http://softlab.sdut.edu.cn/blog/subaochen/2016/09/arcanist的用法简介/#fig_arcanist___)：



![1582790565276](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582790565276.png)

## 3.2、详细使用命令    

### 3.3.1、`arc diff origin/healerjean`

>   **review前开发一定要使用自己的分支，比如healerjean，因为最后合并到主分支后会删除当前分支(如果不创建会怎么办)**   

```
git checkout -b healerjean
```



> 对项目做了一些修改后，首先git add/git commit将修改放进暂存区（stage area），然后执行arc diff命令，会弹出编辑器，我的是Sublime Text，   会提示填写下面的信息：  



**`title：`**              

**`Summary`：默认给的是commit的信息**     

**`Test Plan` – 必填，详细说明你的测试计划，没有就随便填个no或者skip；**       

**`Reviewers`**  **必填   ，执行代码审核人的账户，多个使用”,”隔开；（比如 healerjean），如果指定了多个 reviewers，则其中任何一个 review 通过就可以，不用全部 review 通过**

**`Subscribers` – 非必填，订阅人，能够收到代码变更的邮件通知，多个使用”,”隔开**      



![1582797454590](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1582797454590.png)





### 3.3.2、`arc diff –preview:`代码审核前，可以自己预览自己提交的代码，并且可以再网页重新发起review请求

> 输入以下命令，会给一个预览地址，代码审核前，可以自己预览自己提交的代码。并且可以再网页重新发起review请求



```shell
arc diff –preview
```



### 3.3.3、 如果审核未通过，或者自己想继续追加提交       

```shell
arc diff origin/healerhean   
arc diff #继续执行这个命令，arc会自动知道是更新操作

arc diff　origin/healerjean –update D66666  更新操作（如果被通过了，还可以用哦）

arc diff origin/healerjean  --create  创建一个新的review
```



### 3.3.4、`arc list：`查看review的状态 

```shell
arc list
* Needs Review D202484: 用户登录
```



### 3.3.4、`arc land:`审核通过后，合并分支    

> 如果审核通过，在本地执行arc land即可将代码push到中央仓库，并删除当前分支。自动回到push的分支 。

```shell
arc land --onto healerjean --revision D202484
```


















  **<font  color="red">感兴趣的，欢迎添加博主微信 </font>**       

​    

哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦   



|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'AvkEwfGpsLcSoizd',
    });
    gitalk.render('gitalk-container');
</script> 

