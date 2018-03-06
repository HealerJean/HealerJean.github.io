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

```
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
​```
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

