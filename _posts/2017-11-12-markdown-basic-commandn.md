---
title: markdown基本命令
date: 2017-11-12 00:15:20
tags: 
- MarkDown
category: MarkDown
description: markdown基本命令，标题，文本样式，链接，代码块，表格，列表，图片
---

# 1、标题

```
#一级标题
##这是二级标题
###还有三级标题
```

# 2、文本样式

**粗体**

```
（1）**粗体**
```

*斜体*

```
（2）*斜体*效果：
```

***粗体加斜体***

```
（3）***粗体加斜体***
```

~~删除线~~

```
（5）~~删除线~~
```

# 3、链接

[链接文字](www.baidu.com)

```
[链接文字](www.baidu.com)
```

# 4、代码块

## 1、引用

> 引用1
>
> > 应用2
> >
> > > 引用3

```
> 引用1
>
> > 应用2
> >
> > > 引用3
```

## 2、灰色代码块（可以选择语言html或者java）



```java
List<`StudentInformation`> queryStuInfo(
    String name,
    String stuNo,
    LocalDate endDateMin ,
    LocalDate endDateMax ,
    String tutorName,
    String status,
    String eduDegree);
```
```
​```java(选择语言)
List<StudentInformation> queryStuInfo(
    String name,
    String stuNo,
    LocalDate endDateMin ,
    LocalDate endDateMax ,
    String tutorName,
    String status,
    String eduDegree);
​```
```



## 5、表格

| 属性           |  数据类型   |   长度 |  必填   |    备注说明    |
|:-----------|-----:|:---|:---:|--------:|
| id           |  uuid   |    / | true  | 专场预约唯一识别ID |
| audit_person | varchar |   32 | false |    审核人     |
| audit_state  | varchar |   10 | false |    审核状态    |

```
| 属性           |  数据类型   |   长度 |  必填   |    备注说明    |
|:-----------|-----:|:---|:---:|--------:|
| id           |  uuid   |    / | true  | 专场预约唯一识别ID |
| audit_person | varchar |   32 | false |    审核人     |
| audit_state  | varchar |   10 | false |    审核状态    |

解释： 冒号 表示向那边偏移表格，它在哪边，就会向哪边靠近
```

#6、有序无语列表

## 1、黑点 + 或者 - 、*

- 插入链接 `Ctrl + L` 
- 插入代码 `Ctrl + K` 
- 插入图片 `Ctrl + G` 

```
- 插入链接 `Ctrl + L` 
- 插入代码 `Ctrl + K` 
- 插入图片 `Ctrl + G` 

技巧： 打两个+ 中间按回车，就可以直接很快打出来 eg：- 内容 回车-
注意：*后要空格
```

# 2、有序列表

1.  插入链接 `Ctrl + L` 
2.  插入代码 `Ctrl + K` 
3.  插入图片 `Ctrl + G` 

```
1.  插入链接 `Ctrl + L` 
2.  插入代码 `Ctrl + K` 
3.  插入图片 `Ctrl + G` 
```


# 3、插入图片

## 1、图片

![ImageName](http://p0.ifengimg.com/pmop/2017/1104/979F612A2B62228AE66DD9E67271B01D8CDE9932_size75_w1280_h1280.jpeg)



```

![ImageName](http://p0.ifengimg.com/pmop/2017/1104/979F612A2B62228AE66DD9E67271B01D8CDE9932_size75_w1280_h1280.jpeg)
```

## 2、图片链接

[![ImageName](http://p0.ifengimg.com/pmop/2017/1104/979F612A2B62228AE66DD9E67271B01D8CDE9932_size75_w1280_h1280.jpeg)](https://twww.baidu.com)

```

[![ImageName](http://p0.ifengimg.com/pmop/2017/1104/979F612A2B62228AE66DD9E67271B01D8CDE9932_size75_w1280_h1280.jpeg)](https://twww.baidu.com)
```



# 7、横线

---

```
---或者***再或者+++ 都可以
```


<font color="red"> 感兴趣的，欢迎添加博主微信， </font>哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `d0dabbf6e5925b11ec9c`,
		clientSecret: `d0dabbf6e5925b11ec9c`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'qWLZYhNj9UnO1svG',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

