---
title: 4、线程安全的AtomicLong不需要synchronize
date: 2018-03-06 03:33:00
tags: 
- Thread
category: 
- Thread
description: 线程安全的AtomicLong不需要synchronize
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


## 如果不上synchronize的情况

```java
package com.hlj.thread.AtomicLong;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/2  下午4:28.
 */
 public class OriginMain  {
    public static void main(String[] args) {
        for(int i=0;i<1000;i++){
            Thread thread = new Thread(){
                @Override
                public void run() {
                        if(Counter.addOne() == 1000){
                            System.out.println("counter = 1000");
                        }

                }
            };
            thread.start();
        }
    }

}

 class Counter {
    private static long counter = 0;
    public static  long addOne(){
        return ++counter;
    }
}

```

### 解释：
1、控制台上不一定每次都能出现 counter = 1000
2、在static上添加synchronize，则可以每次都能出现。
3、但是我们不想用synchronize，所以就出现了下面的内容

## 2、线程安全的AtomicLong

new初始为0

```java
 class Counter {
    private static AtomicLong counter = new AtomicLong(0);


    public static long addOne() {
        return counter.incrementAndGet();
    }
}

```
![WX20180402-165444](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180402-165444.png)


<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

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
		id: 'pN3cRrCmHP1KStDx',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

