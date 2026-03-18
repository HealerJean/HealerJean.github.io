---
title: 设计模式之生成实例_AbstractFactory模式_将关联零件组装成产品
date: 2019-05-27 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之生成实例_AbstractFactory模式_将关联零件组装成产品
---



# 1、**抽象工厂（`Abstract Factory Pattern`）设计模式**

## 1、**模式概述**

> **抽象工厂模式** 是一种 **创建型设计模式**，它提供一个 **创建一系列相关或相互依赖对象的接口**，而无需指定它们具体的类。
> 客户端只依赖 **抽象接口**，由具体工厂决定使用哪一套“产品族”进行组装。

**核心思想**：**“一族零件，一套工厂 —— 保证组件之间的兼容性。”**   

**抽象工厂 = 产品族 + 工厂族**

- 它不是“造一个零件”，而是“造一套配套”，确保系统内部组件的协调与兼容。

**关键特征**：**“产品族” vs “产品等级”**

- 同一 **产品族**：来自同一厂商的一组配套产品（如 HP 键盘 + HP 鼠标）；
- 同一 **产品等级**：同一类型的不同实现（如 HP 键盘、Dell 键盘、Asus 键盘）。



## 2、**使用场景**

- 系统需要 **独立于产品创建、组合和表示**；
- 系统需配置为 **使用多个产品系列之一**；
- 相关产品 **必须一起使用**，且要确保 **不混用不同系列的组件**（如不能用惠普键盘配戴尔鼠标）；
- 提供 **产品类库**，但只暴露其接口，不暴露实现细节。

典型应用场景：

- 跨平台 UI 组件（Windows 风格 / macOS 风格的按钮、文本框）；
- 多品牌硬件套装（惠普键盘+鼠标、戴尔键盘+鼠标）；
- 多数据库驱动（MySQL 连接 + 事务管理器、PostgreSQL 连接 + 事务管理器）。

在实际开发中：

- `Java AWT/Swing` 的 `Toolkit` 是抽象工厂的典型应用；
- `Spring` 中通过 `@Profile` 切换不同环境的 Bean 配置，隐含抽象工厂思想；
- 游戏开发中“主题皮肤”（UI + 音效 + 动画）常采用此模式。







## 3、**示例程序：电脑外设产品族构建**

- 客户端 **只与抽象工厂和抽象产品交互**；
- 具体工厂确保 **返回的产品属于同一族**，避免不兼容组合。

| 角色                        | 职责                                       | 示例                                       |
| --------------------------- | ------------------------------------------ | ------------------------------------------ |
| AbstractFactory（抽象工厂） | 声明创建一组抽象产品的接口                 | `Factory`                                  |
| ConcreteFactory（具体工厂） | 实现抽象工厂接口，创建某一产品族的具体产品 | `HPFactory`, `DellFactory`                 |
| AbstractProduct（抽象产品） | 为每种产品定义接口                         | `Key`, `Mouse`                             |
| ConcreteProduct（具体产品） | 实现抽象产品接口，属于某一个产品族         | `HPKey`, `HPMouse`, `DellKey`, `DellMouse` |
| Client（客户端）            | 通过抽象接口使用产品，不依赖具体实现       | `Main`                                     |



### **1）抽象产品接口**

#### **a、键盘接口** `Key`

```java
/**
 * 抽象产品：键盘
 */
public interface Key {
    void key();
}
```

#### **b、鼠标接口** `Mouse`

```java
/**
 * 抽象产品：鼠标
 */
public interface Mouse {
    void mouse();
}
```



### 2）抽象工厂 `Factory`

 **命名规范**：方法名统一为 `createXxx()`，语义清晰。

```java
/**
 * 抽象工厂：定义创建产品族的接口
 */
public abstract class Factory {
   
  public abstract Key createKey();     // 创建键盘
  
  public abstract Mouse createMouse(); // 创建鼠标
}
```



### **3）具体产品族**

#### **a、惠普产品族**

```java
/** 惠普键盘 */
public class HPKey implements Key {
    @Override
    public void key() {
        System.out.println("惠普创建键盘零件");
    }
}

/** 惠普鼠标 */
public class HPMouse implements Mouse {
    @Override
    public void mouse() {
        System.out.println("惠普创建鼠标零件");
    }
}
```



#### **b、戴尔产品族**

```java
/** 戴尔键盘 */
public class DellKey implements Key {
    @Override
    public void key() {
        System.out.println("戴尔创建键盘零件");
    }
}

/** 戴尔鼠标 */
public class DellMouse implements Mouse {
    @Override
    public void mouse() {
        System.out.println("戴尔创建鼠标零件");
    }
}
```



### **4）具体工厂**

#### **a、惠普工厂**

```java
/**
 * 具体工厂：惠普工厂 —— 专门生产惠普产品族
 */
public class HPFactory extends Factory {
    @Override
    public Key createKey() {
        return new HPKey();
    }

    @Override
    public Mouse createMouse() {
        return new HPMouse();
    }
}
```

#### **b、戴尔工厂**

```java
/**
 * 具体工厂：戴尔工厂 —— 专门生产戴尔产品族
 */
public class DellFactory extends Factory {
    @Override
    public Key createKey() {
        return new DellKey();
    }

    @Override
    public Mouse createMouse() {
        return new DellMouse();
    }
}
```



### **5）客户端测试** `Main`

**效果**：客户端只需切换工厂实例，即可获得 **完整且兼容的产品套装**，无需关心具体类名。

```java
public class Main {
    public static void main(String[] args) {
        // 选择惠普产品族
        Factory factory = new HPFactory();
        
        // 或选择戴尔产品族
        // Factory factory = new DellFactory();

        Key key = factory.createKey();
        Mouse mouse = factory.createMouse();

        key.key();    // 输出: 惠普创建键盘零件
        mouse.mouse(); // 输出: 惠普创建鼠标零件
    }
}
```







## 1.2、`UML` 图

- `Factory` 声明创建 `Key` 和 `Mouse` 的方法；
- `HPFactory` / `DellFactory` 实现这些方法，返回对应产品族；
- 客户端通过 `Factory` 获取产品，完全解耦具体实现。

![1559097212752](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1559097212752.png)







## 4、`FQA`

### 1）**扩展性分析**

#### a、**增加新工厂（产品族）—— 容易**

> **例如：新增华硕（ASUS）产品族**

1. 创建 `AsusKey`、`AsusMouse`；
2. 创建 `AsusFactory extends Factory`；
3. 实现 `createKey()` 和 `createMouse()`。

> 📌 **符合开闭原则**：无需修改现有代码，仅扩展。



#### **b、增加新产品（零件类型）—— 困难**

> **例如：新增“耳机”产品**

1. 新增抽象产品 `Headphone` 接口；
2. 所有具体工厂（`HPFactory`, `DellFactory`, `AsusFactory`...）**必须修改**，添加 `createHeadphone()` 方法；
3. 所有产品族需实现对应 `HPHeadphone`, `DellHeadphone` 等。

- **违反开闭原则**：每增加一个产品类型，所有工厂类都要改动。

-  **设计启示**：适合 **产品族稳定、产品类型固定** 的场景。若产品类型频繁变化，应考虑 **工厂方法模式 + 配置注入** 等更灵活方案。



### 2）**模式优点**

- **保证产品族一致性**：避免混用不兼容组件；
- **解耦客户端与具体类**：客户端只依赖抽象；
- **易于切换产品系列**：只需更换工厂实例；
- **封装产品创建逻辑**：集中管理对象生成。



### 3）**注意事项**

- **扩展新产品困难**：需修改所有工厂；
- **类爆炸风险**：每新增一个产品族 × 产品类型，类数量线性增长；
- **适用场景有限**：仅当存在明确“产品族”概念时才推荐使用。




















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

