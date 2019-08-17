---
title: 设计模式之访问数据结构_ChainOfReponsibility责任链模式_推卸责任
date: 2019-05-27 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之访问数据结构_ChainOfReponsibility责任链模式_推卸责任
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    





## 1、解释

场景：假设现在我们去公司领取资料。    

1、首先我们向公司前台打听要去哪里领取资料，她告诉我们应该去“营业窗口”。     

2、然后等我们到了“营业窗口”后，又被告知应该去“售后部门”。    

3、等我们好不容易赶到了“售后部门”，    

4、又被告知应该去“资料中心”，        

5、因此最后我们又不得不赶到“资料中心”。     

**像这样，在找到合适的办事人之前，我们被不断地赐给一个有一个人，这就是“推卸责任”。**



真实项目中，其实我也有使用过，在小米金融科技，接触的供应链项目，FlowNode流程，用到过，我们是讲Json挨个传递，直到每个节点处理完成。具体可以到的博客中翻找案例代码，绝对是好项目



## 2、实例代码



本案例已打印日志说明



### 2.1、抽象父类节点`AbstractLogger`

```java
package com.hlj.moudle.design.D06访问数据结构.D14ChanOfResponsibility责任链模式;

/**
 * @author HealerJean
 * @ClassName AbstractLogger
 * @date 2019/8/16  15:54.
 * @Description 创建抽象的记录器类。
 */
public abstract  class AbstractLogger {

    /**
     * 级别 error 是最高级别3
     */
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    protected int level;

    /**
     * 责任链中的下一个元素
     */
    protected AbstractLogger nextLogger;


    public void setNextLogger(AbstractLogger nextLogger){
        this.nextLogger = nextLogger;
    }

    /**
     * 输入的基本级别如果跟当前级别比较，如果比当前级别大，则肯定打印，
     * 傻瓜 error肯定会打印 info
     */
    public void log(int level, String message){
        if(this.level <= level){
            write(message);
        }
        if(nextLogger !=null){
            nextLogger.log(level, message);
        }
    }

    abstract protected void write(String message);

}

```



### 2.2、Error级别



```java

public class ErrorLogger extends AbstractLogger {

    public ErrorLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Error : " + message);
    }
}

```



### 2.3、Info级别

```java
public class InfoLogger extends AbstractLogger {

    public InfoLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("info : " + message);
    }
}
```



### 2.4、Debug级别

```java
public class DebugLogger extends AbstractLogger {

    public DebugLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Debug : " + message);
    }
}

```



### 2.5、测试

```java
package com.hlj.moudle.design.D06访问数据结构.D14ChanOfResponsibility责任链模式;

/**
 * @author HealerJean
 * @ClassName Main
 * @date 2019/8/16  15:57.
 * @Description
 */
public class Main {

    private static AbstractLogger getChainOfLoggers(){
        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger infoLogger =  new InfoLogger(AbstractLogger.INFO);
        errorLogger.setNextLogger(infoLogger);

        AbstractLogger debugLogger = new DebugLogger(AbstractLogger.DEBUG);
        infoLogger.setNextLogger(debugLogger);

        return errorLogger;
    }

    public static void main(String[] args) {
        AbstractLogger loggerChain = getChainOfLoggers();

        loggerChain.log(AbstractLogger.ERROR, " error message");
        // Error :  error message
        // info :  error message
        // Debug :  error message


        loggerChain.log(AbstractLogger.INFO, "info message");
        // info : info message
        // info :  debug message

        loggerChain.log(AbstractLogger.DEBUG, " debug message ");
        // Debug :  debug message


    }
}

```



### 3、总结

上面就是讲message消息挨个到各自的节点去处理，一直到处理完成，这既是责任链模式



### 3.1、优化地方

#### 3.1.1、可以讲上面抽象类的level 放到各自的子类中

#### 3.1.2、nextLogger 时，在初始化时就指定。





<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



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
		id: 'DuOpsrALa7TUXty8',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

