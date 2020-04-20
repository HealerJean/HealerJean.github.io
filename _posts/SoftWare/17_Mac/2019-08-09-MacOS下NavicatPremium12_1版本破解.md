---
title: MacOS 下 Navicat Premium 12.1 版本破解
date: 2019-08-03 03:33:00
tags: 
- Mac
category: 
- Mac
description: MacOS 下 Navicat Premium 12.1 版本破解
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          




### 1.安装brew (终端内完成)

```sh
$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

```

### 2.依次安装以下几个库


```shell
$ brew install openssl
$ brew install capstone
$ brew install keystone
$ brew install rapidjson

```


### 3.Clone mac 分支，并编译keygen和patcher

```shell
$ git clone -b mac https://github.com/DoubleLabyrinth/navicat-keygen.git
$ cd navicat-keygen
$ make all

```
 **如果这里报错：报错信息 invalid value 'c++17' in '-std=c++17**    

解决方案：   更新Xcode，更新Xcode一定要先更新Mac，这样才能确保Xcode是新版本的

### 4、进入bin目标查看文件


```shell
$ cd bin/
$ ls

会发现如下两个文件navicat-keygen navicat-patcher

```

### 5、使用navicat-patcher替换掉公钥

有些同学的电脑执行

```shell
$ ./navicat-patcher /Applications/Navicat\ Premium.app/Contents/MacOS/Navicat\ Premium

```

如果出现如下报错信息

```shell
***************************************************
*       Navicat Patcher by @DoubleLabyrinth       *
*                  Version: 4.0                   *
***************************************************
Press Enter to continue or Ctrl + C to abort.
[-] ./navicat-patcher/main.cpp:134 -> 
    open failed.
    Not a directory
    
```

执行下面正确的

```shell
$ ./navicat-patcher /Applications/Navicat\ Premium.app/

***************************************************
*       Navicat Patcher by @DoubleLabyrinth       *
*                  Version: 3.0                   *
***************************************************
             省略中间一堆字母
MESSAGE: PatchSolution0 has been applied.
MESSAGE: PatchSolution2 has been applied.
MESSAGE: Patch has been done successfully. Have fun and enjoy~

```

### 6、生成一份自签名的代码证书
![WX20190807-215931](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-215931.png)

![WX20190807-220005](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220005.png)

![WX20190807-220014](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220014.png)

		![WX20190807-220113](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220113.png)




### 7、.用codesign对Navicat Premium.app重签名。


```shell
$ codesign -f -s "填写刚刚你的证书名字" /Applications/Navicat\ Premium.app/

```

### 8、使用navicat-keygen来生成 序列号 和 激活码。



```shell
$ ./navicat-keygen ./RegPrivateKey.pem

会有以下需要填写的文字
Which is your Navicat Premium language?
0. English
1. Simplified Chinese
2. Traditional Chinese
3. Japanese
4. Polish
5. Spanish
6. French
7. German
8. Korean
9. Russian
10. Portuguese

(Input index)> 1(根据自己的版本填写)
(Input major version number, range: 0 ~ 15, default: 12)> 12(根据自己的版本填写)

Serial number:
xxxx-xxxx-xxxx-xxxx(等会要用到)

Your name:  填写名称.随意
Your organization: 填写组织.随意 

```

回车，然后就别动了，准备复制东西到这里来


### 9、断开网络 并打开Navicat。找到注册窗口，填入注册机给你的序列号。然后点击激活按钮，会出现下面的东西东西，把这部分复制，然后输入到

![WX20190807-220422](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220422.png)

![WX20190807-220459](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220459.png)


### 10、生成请求码

![WX20190807-220511](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220511.png)

![WX20190807-220121](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20190807-220121.png)

