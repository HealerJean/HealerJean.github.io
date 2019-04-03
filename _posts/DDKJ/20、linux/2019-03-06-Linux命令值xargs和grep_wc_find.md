---
title: Linux命令值xargs和grep_wc_find
date: 2019-03-06 03:33:00
tags: 
- Linux
category: 
- Linux
description: Linux命令值xargs和grep
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进


<font  clalss="healerColor" color="red" size="5" >     

</font>

<font  clalss="healerSize"  size="5" >     </font>

-->

## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    

​     

## 1、xargs

### 1.1、文本内容相关


```shell
healerjean$ cat -n text.txt 
1	a b c d e f g
2	h i j k l m n
3	o p q
4	r s t
     
```


#### 1.1.1、| xargs 单行输出文本所有内容 


```shell
healerjean$ cat text.txt | xargs
a b c d e f g h i j k l m n o p q r s t
```

#### 1.1.2、| xargs -n4 选择每行打印多少个（按照空格和换行来进行分组）

##### -n多行输出：后面直接加数字

```
healerjean$ cat text.txt | xargs -n4
a b c d
e f g h
i j k l
m n o p
q r s t

```

#### 1.1.3、 | xargs -dX 分隔一串字符串

##### -d 选项可以自定义一个定界符：

```shell

healerjean$ echo "nameXnameXnameXname" | xargs -dX
 
name name name name

```


#### 1.1.4、 -d -n 联合使用


```shell

echo "nameXnameXnameXname" | xargs -dX -n2
 
name name
name name

```


### 1.2、组合多个命令


#### 1.2.1、（查询->删除）

```

删除vedio开头的键
keys vedio* | xargs redis-cli del

查询文件名为.svn的，然后删除
find . -name ".svn"| xargs rm -Rf 

删除镜像
docker images | grep registry.cn-qingdao.aliyuncs.com/duodianyouhui/dev-server | xargs docker rmi



```




## 2、grep

### 2.1、文本内容相关

#### 2.1.1、查看某个字符串所在行的内容


```

healerjean$ grep a text.txt 
a b c d e f g

```

### 2.2、查询


```
healerjean$ find . -name '*.txt' |grep test
./study/cant-breathe/13、redis/7/1、spring redis/spring-redis-test.txt
./study/HealerJean.github.io/_posts/DDKJ/3、AngularJS_Vue/Vue/模板/element-starter/node_modules/hoek/test/modules/ignore.txt
```


## 3、find

### 3.1、-name 通过名字查询

#### -iname 不区分大小写



```
healerjean$ find . -name '*.txt' |grep test
./study/cant-breathe/13、redis/7/1、spring redis/spring-redis-test.txt
./study/HealerJean.github.io/_posts/DDKJ/3、AngularJS_Vue/Vue/模板/element-starter/node_modules/hoek/test/modules/ignore.txt
```

### 3.2、-type 根据文件类型查找(f文件,d目录,l软链接文件)



```
healerjean$ find . -type f -name "text.txt"
./text.txt
./workspace/duodianyouhui/youhui-admin/node_modules/vue-resource/test/data/text.txt

```

### 3.3  -print -print0

-print 在每一个输出后会添加一个回车换行符（默认），而-print0则不会直接连起来变成一行。


```
root@AaronWong shell_test]# find /ABC/ -type l -print
/home/AaronWong/ABC/libcvaux.so
/home/AaronWong/ABC/libgomp.so.1
/home/AaronWong/ABC/libcvaux.so.4


[root@AaronWong shell_test]# find /ABC/ -type l -print0
/home/AaronWong/ABC/libcvaux.so/home/AaronWong/ABC/libgomp.so.1/home/AaronWong/ABC/libcvaux.so.4/hom

```


## 4、wc 统计文件中的字节数、字数、行数


```
cat text.txt 
a b c d e f g
h i j k l m n
o p q
r s t

```

### 4.1、-l 统计行数


```
healerjean$ wc -l text.txt 
4 text.txt
       
  
healerjean$ cat text.txt| wc -l
4

```

### 4.2、-w 统计单词数量(不包含空格和换行等)


```
healerjean$ wc -w text.txt 
20 text.txt
```

### 4.3、-c 统计字符数（只显示Bytes数；）


```
healerjean$ wc -c text.txt 
40 text.txt
```

### 4.3、 wc 直接打印

分别打印行数，单词书，字符数

```
healerjean$ wc text.txt 
       4      20      40 text.txt
```

## 5、cat

### 5.1、直接查看文件内容


```
healerjean$ cat text.txt 
a b c d e f g
h i j k l m n
o p q
r s t
```

### 5.2、-n 显示内容与行数     


```
healerjean$ cat -n text.txt 
1	a b c d e f g
2	h i j k l m n
3	o p q
4	r s t

```


<br><br>    
<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>
<br>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'QgipZPDtUL7aBGbS',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

