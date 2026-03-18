---
title: 设计模式之交给子类_FactoryMethod模式_将实例生成交给子类
date: 2019-05-24 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之交给子类_FactoryMethod模式_将实例生成交给子类

---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)       



# 1、**`Factory Method`（工厂方法）设计模式**

## **1、模式概述**

 **核心思想**：定义创建接口，实现交给子类。

- **工厂方法模式** 是一种 **创建型设计模式**，它定义一个用于创建对象的接口，但 **将具体实例化的责任延迟到子类中**。   

- 父类决定“**何时创建**”和“**创建什么类型的产品**”，但不决定“**具体是哪个类**”；子类负责提供具体的实现。

**工厂方法 = 创建接口 + 子类实现**：它不是直接“造东西”，而是“定义谁来造、怎么造”，把创建权交给更合适的子类。



## 2、**使用场景**

工厂方法模式适用于以下情况：

- 一个类不知道它所必须创建的对象的类；
- 一个类希望由其子类来指定它所创建的对象；
- 需要将对象的创建逻辑集中管理，并支持未来扩展新类型而**不修改现有代码**（符合开闭原则）。




## 4、普通工厂模式：示例程序

### 1） **产品接口** `Sender`


```java
public interface Sender {  
    public void Send();  
}  

```



### 2）**具体产品类**


#### a、发送实现类1 `MailSender`


```java
public class MailSender implements Sender {  
    @Override  
    public void Send() {  
        System.out.println("this is mailsender!");  
    }  
} 


```



#### b、发送实现类2 `SmsSender`


```java
public class SmsSender implements Sender {  
	  
    @Override  
    public void Send() {  
        System.out.println("this is sms sender!");  
    }  
}

```



### 3）创建工厂类 `SendFactory`

> 通过传入的字符串常亮进行匹配选需要的工厂类

```java
public class SendFactory {  
	  
    public Sender produce(String type) {  
        if ("mail".equals(type)) {  
            return new MailSender();  
        } else if ("sms".equals(type)) {  
            return new SmsSender();  
        } else {  
            System.out.println("请输入正确的类型!");  
           return null;  
        }  
    }  
}

```

### 4）**客户端测试** `Main`


```java
public class FactoryTest {  
	  
    public static void main(String[] args) {  
        SendFactory factory = new SendFactory();  
        Sender sender = factory.produce("sms");  
        sender.Send();  
    }  
}

```



## 5、多个工厂方法模式：示例程序


### 1）创建工厂类  `SendFactory`

```java
public class SendFactory {  
   public Sender produceMail(){  
        return new MailSender();  
    }  

    public Sender produceSms(){  
        return new SmsSender();  
    }  
	} 

```



### 2）**客户端测试** `Main`


```java
public class FactoryTest {  
	  
    public static void main(String[] args) {  
        SendFactory factory = new SendFactory();  
        Sender sender = factory.produceMail();  
        sender.Send();  
    }  
}

```



## 6、静态工厂方法


### 1）修改静态工厂方法


```java

public class SendFactory {  
    
    public static Sender produceMail(){  
        return new MailSender();  
    }  
      
    public static Sender produceSms(){  
        return new SmsSender();  
    }  
}  

```



### 2）**客户端测试** `Main`


```java
public class FactoryTest {  
	  
    public static void main(String[] args) {      
        Sender sender = SendFactory.produceMail();  
        sender.Send();  
    }  
}  


```



## 7、`FQA`

### 1）**模式优点**

- **解耦**：客户端与具体产品类解耦；
- **扩展性强**：新增产品无需修改现有代码；
- **符合开闭原则**：对扩展开放，对修改关闭；
- **支持多态创建**：不同工厂可创建不同产品族。



### 2）**注意事项**

- 类数量增加（每个产品需配一个工厂）；
- 若产品结构简单，可能过度设计；
- 可结合 **抽象工厂模式** 支持多系列产品（如同时创建 Button + TextField）。




















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
		id: '1BqS6AMNpv2dUJuC',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

