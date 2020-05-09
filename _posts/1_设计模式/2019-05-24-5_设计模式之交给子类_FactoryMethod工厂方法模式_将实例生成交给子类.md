---
title: 设计模式之交给子类_FactoryMethod模式_将实例生成交给子类
date: 2019-05-24 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之交给子类_FactoryMethod模式_将实例生成交给子类
---



# 1、FactoryMethod模式

> 父类决定实例的生成方式，但并不决定所要生成的具体的类，将实例的生成交给子类，通过工厂具体使用哪个类



1、定义一个创建产品的接口(内部可能有多个产品)         

2、将产品实例生成的交给子类             

3、我们通过工厂选择需要的产品，小米用到     






## 1.1、普通工厂模式：示例程序

### 1.1.1、创建一个接口`Sender`


```java
public interface Sender {  
    public void Send();  
}  

```



### 1.1.2、两个实现类


#### 1.1.2.1、发送实现类1`MailSender`


```java
public class MailSender implements Sender {  
    @Override  
    public void Send() {  
        System.out.println("this is mailsender!");  
    }  
} 


```



#### 1.1.2.2、发送实现类2`SmsSender`


```java
public class SmsSender implements Sender {  
	  
    @Override  
    public void Send() {  
        System.out.println("this is sms sender!");  
    }  
}

```



### 1.1.3、创建工厂类`SendFactory`

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

### 1.1.4、测试使用


```java
public class FactoryTest {  
	  
    public static void main(String[] args) {  
        SendFactory factory = new SendFactory();  
        Sender sender = factory.produce("sms");  
        sender.Send();  
    }  
}

```



## 1.2、多个工厂方法模式：示例程序


### 1.2.1、改变上面的工厂类

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

### 12.2、测试使用


```java

public class FactoryTest {  
	  
    public static void main(String[] args) {  
        SendFactory factory = new SendFactory();  
        Sender sender = factory.produceMail();  
        sender.Send();  
    }  
}

```



## 1.3、静态工厂方法


### 1.3.1、修改静态工厂方法


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

### 1.3.2、测试使用


```java

public class FactoryTest {  
	  
    public static void main(String[] args) {      
        Sender sender = SendFactory.produceMail();  
        sender.Send();  
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
		id: '1BqS6AMNpv2dUJuC',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

