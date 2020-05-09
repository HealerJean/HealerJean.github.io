---
title: 设计模式之访问数据结构_ChainOfReponsibility责任链模式_推卸责任
date: 2019-05-27 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之访问数据结构_ChainOfReponsibility责任链模式_推卸责任
---




**前言**     

[博主github](https://github.com/HealerJean)      

[博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)        



# 1、`ChainOfReponsibility`责任链模式




## 1.1、解释

场景：假设现在我们去公司领取资料。    

1、首先我们向公司前台打听要去哪里领取资料，她告诉我们应该去“营业窗口”。     

2、然后等我们到了“营业窗口”后，又被告知应该去“售后部门”。    

3、等我们好不容易赶到了“售后部门”，    

4、又被告知应该去“资料中心”，        

5、因此最后我们又不得不赶到“资料中心”。     

**像这样，在找到合适的办事人之前，我们被不断地赐给一个有一个人，这就是“推卸责任”。**



真实项目中，其实我也有使用过，在小米金融科技，接触的供应链项目，FlowNode流程，用到过，我们是讲Json挨个传递，直到每个节点处理完成。具体可以到的博客中翻找案例代码，绝对是好项目



## 1.2、示例代码



### 1.2.1、抽象父类节点`AbstractLogger`

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



#### 1.2.1.1、Error级别

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



#### 1.2.1.2、Info级别

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



#### 1.2.1.3、Debug级别

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



### 1.2.2、测试

```java
package com.hlj.moudle.design.D06访问数据结构.D14ChanOfResponsibility责任链模式;


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








![](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)



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

