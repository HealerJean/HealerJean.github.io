---
title: 设计模式之生成实例_AbstractFactory模式_将关联零件组装成产品
date: 2019-05-27 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之生成实例_AbstractFactory模式_将关联零件组装成产品
---



# 1、`AbstractFactory`模式



> 抽象工厂的工作是将`抽象零件`构件组装成`抽象产品 `,我们不关心零件的具体实现，而是只关心接口，我们仅适用该接口将零件组装成产品     



## 1.1、示例程序

### 1.1.1、抽象工厂 `Factory` （抽象类或者接口）

```java

/**
 * 表示抽象工程的类 制作 鼠标，键盘
 */
public abstract class Factory {

    public abstract Key creatKey();

    public abstract Mouse createMouse();

}

```



### 1.1.2、抽象的零件 - 键盘 `Key` （抽象类或者接口）

```java
public interface Key {

    void key() ;
}

```



### 1.1.3、抽象的零件 - 鼠标 `Mouse` （抽象类或者接口）

```java
public interface Mouse {

    void mouse() ;

}
```



### 1.1.4、具体的工厂`HPFactory`

```java
/**
 * 具体工厂，惠普工厂
 */
public class HPFactory extends Factory {


    @Override
    public Key creatKey() {
        return new HPKey();
    }

    @Override
    public Mouse createMouse() {
        return new HPMouse();
    }

}

```



#### 1.1.4.1、具体的零件 `HPKey`

```java
public class HPKey implements Key {

    @Override
    public void key() {
        System.out.println("惠普创建键盘零件");
    }
}

```



#### 1.1.4.2、具体的零件`HPMouse`

```java
public class HPMouse implements Mouse {

    @Override
    public void mouse() {
        System.out.println("惠普创建鼠标零件");
    }

}
```





### 1.1.5、具体的工厂`DellFactory`

```java

public class DellFactory  extends Factory {

    @Override
    public Key creatKey() {
        return new DellKey();
    }

    @Override
    public Mouse createMouse() {
        return new DellMouse();
    }

}
```

#### 1.1.5.1、具体的零件 `DellKey`

```java

public class DellKey implements Key {

    @Override
    public void key() {
        System.out.println("戴尔创建键盘零件");
    }
}

```

#### 1.1.5.2、具体的零件`DellMouse`

```java
escription 零件：戴尔鼠标
 */
public class DellMouse implements Mouse {

    @Override
    public void mouse() {
        System.out.println("戴尔创建鼠标零件");
    }

}

```



### 1.1.6、Main测试

```java
package com.hlj.moudle.design.D05_生成实例.D08_AbstractFactory.Sample;


import com.hlj.moudle.design.D05_生成实例.D08_AbstractFactory.Sample.factory.Factory;
import com.hlj.moudle.design.D05_生成实例.D08_AbstractFactory.Sample.factory.Key;
import com.hlj.moudle.design.D05_生成实例.D08_AbstractFactory.Sample.factory.Mouse;
import com.hlj.moudle.design.D05_生成实例.D08_AbstractFactory.Sample.listfactory.HPFactory;

public class Main {
    public static void main(String[] args) {

        Factory factory = new HPFactory();
//        Factory factory = new DellFactory();

        Mouse mouse = factory.createMouse();
        Key key = factory.creatKey();

        mouse.mouse();
        key.key();

    }
}

```







## 1.2、`UML` 图

![1559097212752](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1559097212752.png)



## 1.3、角色

#### 1.3.1.抽象产品（`Product`):

抽象产品是一个抽象类或接口，负责定义具体产品必须实现的方法。

#### 1.3.2.具体产品（`ConcreteProduct`):

具体产品是一个类，如果Product是一个抽象类，那么具体产品是Product的子类；如果Product是一个接口，那么具体产品是实现Product接口的类。

#### 1.3.3.抽象工厂（`AbstractFactory`）：

抽象工厂是一个接口或抽象类，负责定义若干个抽象方法。

#### 1.4.具体工厂（`ConcreteFactory`）：

如果抽象工厂是抽象类，具体工厂就是抽象工厂的子类；如果抽象工厂是接口，具体工厂是实现抽象工厂的类。具体工厂覆写抽象工厂中的抽象方法，使该方法返回具体产品的实例。







## 1.4、扩展思路的要点

> 抽象工厂模式也就是不仅生产鼠标，同时生产键盘。就是 工厂是个父类，有生产鼠标，生产键盘两个接口。  戴尔工厂，惠普工厂继承它，可以分别生产戴尔鼠标+戴尔键盘，和惠普鼠标+惠普键盘。



### 1.4.3、增加一个工厂  

> 容易：假设我们增加华硕工厂，则我们需要增加华硕工厂，和戴尔工厂一样，继承Factory工厂。之后创建华硕鼠标，继承鼠标类。创建华硕键盘，继承键盘类即可。



![1559098490451](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1559098490451.png)

### 1.4.3、增加一个零件

>*困难：添加产品容易，添加零件难**

假设我们增加耳麦这个零件，则首先我们需要增加耳麦这个父类（或接口），再加上戴尔耳麦，惠普耳麦这两个子类。之后在Factory这个工厂中中，增加生产耳麦的接口。最后在戴尔工厂，惠普工厂这两个类中，分别实现生产戴尔耳麦，惠普耳麦的功能。 以上    已经编写的具体的工厂越多，修改的工作量就会越大    












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
		id: 'QbsDeA2LJyfYh4Mo',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

