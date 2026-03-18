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

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)        

# **一、责任链模式（Chain of Responsibility Pattern）**

## **1、模式概述**

> **责任链模式** 是一种 **行为型设计模式**，它允许多个对象 **有机会处理请求**，从而避免请求的发送者与接收者之间的 **耦合**。
> 将这些对象连成一条链，并沿着这条链传递请求，直到有一个对象处理它为止。

**核心思想**：**“一个接一个地试，谁合适谁处理 —— 责任层层传递。”**    

**责任链模式 = 请求传递 + 动态处理**

- 它不是“踢皮球”，而是“智能路由”——让系统具备 **自适应处理能力**。

类比理解：

- 你去公司领资料：
  - 前台 → 营业窗口 → 售后部门 → 资料中心……
  - **每人都说“不归我管”，把你推给下一个人**，直到找到真正负责人。
  - 这看似“推卸责任”，实则是 **解耦请求与处理者** 的优雅设计！



## **2、使用场景**

责任链模式适用于以下情况：

- 多个对象 **均可处理同一请求**，但具体由谁处理需在运行时动态决定；
- 请求有 **多个处理层级**（如审批流：组长 → 经理 → 总监）；
- 需要 **按顺序尝试处理**，且可能提前终止（如过滤器链、拦截器）。

**典型应用**：

- Java Servlet 中的 `FilterChain`；
- Spring Security 的认证/授权过滤器链；
- 工作流引擎（如你提到的供应链 FlowNode）；
- 日志系统（INFO / DEBUG / ERROR 分级处理）；
- 权限校验、异常处理、表单验证等中间件场景。



## 3、**示例程序：分级日志系统**



### **1）抽象处理器** `AbstractLogger`

```java
/**
 * 抽象处理器（Handler）
 * 定义日志级别和责任链基础行为
 */
public abstract class AbstractLogger {
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int ERROR = 3;

    protected int level; // 当前处理器能处理的最低级别
    protected AbstractLogger nextLogger; // 下一个处理器

    public void setNextLogger(AbstractLogger nextLogger) {
        this.nextLogger = nextLogger;
    }

    /**
     * 处理日志请求
     * @param level 请求的日志级别
     * @param message 日志内容
     */
    public void logMessage(int level, String message) {
        // 如果当前处理器能处理（请求级别 >= 自身级别），则处理
        if (this.level <= level) {
            write(message);
        }
        // 如果还有下一个处理器，继续传递（无论是否已处理）
        if (nextLogger != null) {
            nextLogger.logMessage(level, message);
        }
    }

    protected abstract void write(String message);
}
```



### **2）具体处理器（ConcreteHandler）**

#### **a、控制台信息日志**

```java
public class ConsoleInfoLogger extends AbstractLogger {
    public ConsoleInfoLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("INFO: " + message);
    }
}
```

#### **b、控制台调试日志**

```java
public class ConsoleDebugLogger extends AbstractLogger {
    public ConsoleDebugLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("DEBUG: " + message);
    }
}
```

#### **c、控制台错误日志**

```java
public class ConsoleErrorLogger extends AbstractLogger {
    public ConsoleErrorLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("ERROR: " + message);
    }
}
```



### **3）客户端构建责任链** `Main`

```java
/**
 * 客户端（Client）
 * 构建责任链：INFO → DEBUG → ERROR
 * （注意：实际中常按优先级反向构建，此处为演示传递效果）
 */
public class Main {
    private static AbstractLogger buildLoggerChain() {
        // 创建各个日志处理器
        AbstractLogger infoLogger = new ConsoleInfoLogger(AbstractLogger.INFO);     // 1
        AbstractLogger debugLogger = new ConsoleDebugLogger(AbstractLogger.DEBUG);  // 2
        AbstractLogger errorLogger = new ConsoleErrorLogger(AbstractLogger.ERROR);  // 3

        // 构建链：INFO → DEBUG → ERROR
        infoLogger.setNextLogger(debugLogger);
        debugLogger.setNextLogger(errorLogger);

        return infoLogger; // 从最低级别开始
    }

    public static void main(String[] args) {
        AbstractLogger loggerChain = buildLoggerChain();

        System.out.println("=== 发送 ERROR 日志 ===");
        loggerChain.logMessage(AbstractLogger.ERROR, "系统崩溃！"); 
        // 输出：
        // INFO: 系统崩溃！
        // DEBUG: 系统崩溃！
        // ERROR: 系统崩溃！

        System.out.println("\n=== 发送 DEBUG 日志 ===");
        loggerChain.logMessage(AbstractLogger.DEBUG, "调试信息");
        // 输出：
        // INFO: 调试信息
        // DEBUG: 调试信息

        System.out.println("\n=== 发送 INFO 日志 ===");
        loggerChain.logMessage(AbstractLogger.INFO, "普通信息");
        // 输出：
        // INFO: 普通信息
    }
}
```



## **4）**模式角色****

| 角色                          | 职责                                         | 示例                        |
| ----------------------------- | -------------------------------------------- | --------------------------- |
| Handler（处理器抽象类/接口）  | 定义处理请求的接口，并持有下一个处理器的引用 | `AbstractLogger`            |
| ConcreteHandler（具体处理器） | 实现处理逻辑，决定自己处理 or 转发给下一个   | `ErrorLogger`, `InfoLogger` |
| Client（客户端）              | 构建责任链，并发起请求                       | `Main`                      |

```
                ┌──────────────────────┐
                │   AbstractLogger     │
                │----------------------│
                │ + level: int         │
                │ + nextLogger         │
                │----------------------│
                │ + logMessage(level, msg) │
                │ + write(msg): abstract   │
                └──────────▲───────────┘
                           │
     ┌─────────────────────┴─────────────────────┐
     │                                           │
┌────▼────────────┐   ┌──────────▼────────────┐   ┌─────────▼───────────┐
│ ConsoleInfoLogger│   │ ConsoleDebugLogger   │   │ ConsoleErrorLogger  │
└─────────────────┘   └──────────────────────┘   └─────────────────────┘
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

