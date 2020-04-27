---
title: 1、多线程队列之BlockingQueueTest
date: 2018-03-28 18:33:00
tags: 
- Thread
category: 
- Thread
description: 多线程队列之BlockingQueueTest
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             



BlockingQueueTest    

<font color="red"> 
　　LinkedBlockingQueue是一个线程安全的阻塞队列(`使用了lock锁机制`)，实现了先进先出等特性，是作为生产者消费者的首选，可以指定容量，也可以不指定，不指定的话默认最大是Integer.MAX_VALUE </font>     

　　其中主要用到put和take方法，put方法将一个对象放到队列尾部，在队列满的时候会阻塞直到有队列成员被消费，take方法从head取一个对象，在队列为空的时候会阻塞，直到有队列成员被放进来。    

1、add(anObject)： 能放就放不能放就生气了     

　　把anObject添加到BlockingQueue里，添加成功返回true，如果BlockingQueue空间已满则抛出异常。

#### 2、offer(anObject)： 能放就放，不能放拉倒，返回false
 　　表示如果可能的话，将anObject加到BlockingQueue里，即如果BlockingQueue可以容纳，则返回true，否则返回false。     

　　

#### <font color="red"> 3、put(anObject) </font>： 我脾气好，我等还不行
 　　把anObject加到BlockingQueue里，如果BlockingQueue没有空间，则调用此方法的线程被阻断直到BlockingQueue里有空间再继续。     

　

#### 4、poll(time)： 按照一段时间取出队头
 　　获取并移除此队列的头，若不能立即取出，则可以等time参数规定的时间，取不到时返回null。

#### <font color="red">5、 take()：</font> 我脾气好，我等还不行 　　
　　获取BlockingQueue里排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到BlockingQueue有新的对象被加入为止。<br/> 
#### 6、clear()： 清除整个队列 
从队列彻底移除所有元素。<br/> 
#### 7、remove()  把头砍掉

方法直接删除队头的元素<br/> 
#### 8、peek() 取出对头，但是不删除，也是醉了

方法直接取出队头的元素，并不删除<br/>

## 1、线程池测试

泛型可以指定队列中存放的内容，可以存放对象哦

```
    public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(4);

```

```java
package com.hlj.thread.comhljthread.start;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/28
 */

public class BlockingQueueTest extends Thread {

    public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(4);

    private int index;

    public BlockingQueueTest(int i) {
        this.index = i;
    }

    public void run() {
        try {
            //把anObject加到BlockingQueue里，如果BlockingQueue没有空间，则调用此方法的线程被阻断直到BlockingQueue里有空间再继续。
            queue.put(String.valueOf(this.index));
            System.out.println(this.index + ":" + Thread.currentThread().getName());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String args[]) {
        ExecutorService service = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++) {
            service.submit(new BlockingQueueTest(i));
        }
        Thread thread = new Thread() {

            public void run() {
                try {
                    while(true) {
                        Thread.sleep((int)(Math.random() * 1000));
                        if(BlockingQueueTest.queue.isEmpty()){
                            break;
                        }
                        //获取BlockingQueue里排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到BlockingQueue有新的对象被加入为止。
                        String str = BlockingQueueTest.queue.take();
                        System.out.println("take {" + str + "} out of queue!");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        service.submit(thread);
        service.shutdown();
    }

}

```

![WX20180328-175518](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180328-175518.png)


## [代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_28_1_%E5%A4%9A%E7%BA%BF%E7%A8%8B%E9%98%9F%E5%88%97%E4%B9%8BBlockingQueueTest/com-hlj-thread.zip)



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
		id: '1fk9yXknLkdU4c7I',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

