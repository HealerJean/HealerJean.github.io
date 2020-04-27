---
title: 4、线程安全的AtomicLong不需要synchronize
date: 2019-04-27 03:33:00
tags: 
- Thread
category: 
- Thread
description: 线程安全的AtomicLong不需要synchronize
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             




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
		id: 'pN3cRrCmHP1KStDx',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

