---
title: spirngboot关于Maven项目代码jar上传到linux服务
date: 2018-07-19 03:33:00
tags: 
- SpirngBoot
category: 
- SpirngBoot
description: spirngboot关于Maven项目代码jar上传到linux服务
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言



```shell
#! /bin/bash


echo "################------清理项目----######################################"
mvn clean

echo "################------开始打包----######################################"
mvn package

echo "################-----测试环境-开始上传,请输入密码----######################################"

scp admin/target/admin-1.0-SNAPSHOT.jar root@15.1.1.1:/usr/local/webproject

```



```shell
本地运行
./build.sh
```

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
		id: 'lZ1uLRU8A99RLrYw',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

