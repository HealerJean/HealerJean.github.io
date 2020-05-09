---
title: 设计模式之交给子类_TemplateMethod模模式_讲具体处理交给子类.
date: 2019-05-24 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之交给子类_TemplateMethod模模式_讲具体处理交给子类.
---





**前言**     

[博主github](https://github.com/HealerJean)     

[博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)        



# 1、模板方法

> 父类中定义处理流程的框架，在子类中实现具体处理的模式 ，这种模式我们经常会遇到，项目中太多了   ,**就是说一个父类中有一些方法有不同的实现，需要多个子类来处理。** 



## 1.1、使用场景  



> 也就是说多个业务有重合的地方，重合的地方交给父类，子类继承，也可重写



## 1.2、角色

### 1.2.1、AbstractClass（抽象类 父类） 

> **期待，并且要求子类去实现抽象方法**     

### 1.2.2、ConcreteClass（具体类 ，子类）

> **1、在子类中可以使用父类中定义的方法 **   
>
> **2、在子类中重新父类的方法可以改变程序的行为**   
>
> **3、在子类中添加方法来实现新的功能**    





## 1.3、示例程序

### 1.2.1、抽象父类 `AbstractDisplay`

```java
public abstract class AbstractDisplay { // 抽象类AbstractDisplay
    public abstract void open();        // 交给子类去实现的抽象方法(1) open
    public abstract void print();       // 交给子类去实现的抽象方法(2) print
    public abstract void close();       // 交给子类去实现的抽象方法(3) close
    public final void display() {       // 本抽象类中实现的display方法
        open();                         // 首先打开…
        for (int i = 0; i < 5; i++) {   // 循环调用5次print
            print();                    
        }
        close();                        // …最后关闭。这就是display方法所实现的功能
    }
}
```



### 1.2.2、子类 `CharDisplay`

```java
package com.hlj.moudle.design.D03_交给子类.D03_TeampleMethod模式.TemplateMethod.Sample;

public class CharDisplay extends AbstractDisplay {  // CharDisplay是AbstractDisplay的子类
    private char ch;                                // 需要显示的字符

    public CharDisplay(char ch) {                   // 构造函数中接收的字符被
        this.ch = ch;                               // 保存在字段中
    }

    public void open() {                            // 在父类中是抽象方法，此处重写该方法
        System.out.print("<<");                     // 显示开始字符"<<"
    }

    public void print() {                           // 同样地重写print方法。该方法会在display中被重复调用
        System.out.print(ch);                       // 显示保存在字段ch中的字符
    }

    public void close() {                           // 同样地重写close方法
        System.out.println(">>");                   // 显示结束字符">>"
    }
}

```



### 1.2.3、子类 `StringDisplay`

```java
package com.hlj.moudle.design.D03_交给子类.D03_TeampleMethod模式.TemplateMethod.Sample;
// StringDisplay也是AbstractDisplay的子类
public class StringDisplay extends AbstractDisplay {    
    // 需要显示的字符串
    private String string;                             
     // 以字节为单位计算出的字符串长度
    private int width;                                 

     // 构造函数中接收的字符串被
    public StringDisplay(String string) {              
        this.string = string;                          
        this.width = string.getBytes().length;          
    }

    // 重写的open方法
    public void open() {                                
        printLine();                                   
    }

    // print方法
    public void print() {                               
        System.out.println("|" + string + "|");         
    }

    // close方法
    public void close() {                               
        printLine();                                    
    }

    // 被open和close方法调用。由于可见性是private，因此只能在本类中被调用
    private void printLine() {                          
        System.out.print("+");                          // 显示表示方框的角的"+"
        for (int i = 0; i < width; i++) {               // 显示width个"-"
            System.out.print("-");                      // 组成方框的边框
        }
        System.out.println("+");                        // /显示表示方框的角的"+"
    }
}

```



#####  1.2.4、测试Main

```java
public class Main {
    public static void main(String[] args) {
        // 生成一个持有'H'的CharDisplay类的实例 
        AbstractDisplay d1 = new CharDisplay('H');        
        
        // 生成一个持有"Hello, world."的StringDisplay类的实例 
        AbstractDisplay d2 = new StringDisplay("Hello, world.");  
        
        // 生成一个持有"你好，世界。"的StringDisplay类的实例 
        AbstractDisplay d3 = new StringDisplay("你好，世界。");     
        
        // 由于d1、d2和d3都是AbstractDisplay类的子类
        d1.display();                                     
        // 可以调用继承的display方法
        d2.display();            
        // 实际的程序行为取决于CharDisplay类和StringDisplay类的具体实现
        d3.display();                                               

    }
}

```





## 1.4、UML图

![1558693456672](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1558693456672.png)

 





​       


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
		id: 'lYG9eMgaxPQWyTAr',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

