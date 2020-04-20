<!---->---
title: 设计模式之交给子类_FactoryMethod模式_将实例生成交给子类
date: 2019-05-24 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之交给子类_FactoryMethod模式_将实例生成交给子类
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font size="4">   </font>
-->

## 1、解释
<font color="red"> 

### 1、将实例的生成交给子类 

### 2、父类决定实例的生成方式，但并不决定所要生成的具体的类

 </font>


工厂方法(Factory Method)模式的意义是      

1、定义一个创建产品的工厂接口(内部可能有多个产品)      

2、将产品实例生成的交给工厂子类         

3、我们通过子类工厂选择需要的产品，小米用到

好处：使得工厂方法模式可以使系统在不修改具体工厂角色的情况下引进新的产品。


**工厂方法模式是简单工厂模式的衍生，解决了许多简单工厂模式的问题。首先完全实现‘开－闭原则’，实现了可扩展。其次更复杂的层次结构，可以应用于产品结果复杂的场合**。


## 1、普通工厂模式

### 1.1、创建一个接口`Sender`


```java
public interface Sender {  
    public void Send();  
}  

```

### 1.2、两个实现类


#### 1.2.1、发送实现类1`MailSender`


```java
public class MailSender implements Sender {  
    @Override  
    public void Send() {  
        System.out.println("this is mailsender!");  
    }  
} 




```

#### 1.2.2、发送实现类2`SmsSender`


```java
public class SmsSender implements Sender {  
	  
    @Override  
    public void Send() {  
        System.out.println("this is sms sender!");  
    }  
}

```



### 1.3、创建工厂类`SendFactory`


#### <font color="red">通过传入的字符串常亮进行匹配  </font>

 

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

### 1.4、测试使用


```java
public class FactoryTest {  
	  
    public static void main(String[] args) {  
        SendFactory factory = new SendFactory();  
        Sender sender = factory.produce("sms");  
        sender.Send();  
    }  
}

```


## 2、多个工厂方法模式


### 2.1、改变上面的工厂类



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

### 2.2、测试使用


```java

public class FactoryTest {  
	  
    public static void main(String[] args) {  
        SendFactory factory = new SendFactory();  
        Sender sender = factory.produceMail();  
        sender.Send();  
    }  
}

```


## 3、静态工厂方法


### 3.1、修改静态工厂方法


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

### 3.2、测试使用


```java

public class FactoryTest {  
	  
    public static void main(String[] args) {      
        Sender sender = SendFactory.produceMail();  
        sender.Send();  
    }  
}  


```

## 总结：

总体来说，工厂模式适合：凡是出现了大量的产品需要创建，并且具有共同的接口时，可以通过工厂方法模式进行创建。     

第一种：如果传入的字符串有误，不能正确创建对象，     

第三种相对于第二种，不需要实例化工厂类，所以，大多数情况下，我们会选用第三种——静态工厂方法模式。

## 缺点：


**每增加一个接口实现类，都需要在工厂类中添加对应的获取接口实现类的方法。这样就会改变代码的结构**。


   
       
          
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

