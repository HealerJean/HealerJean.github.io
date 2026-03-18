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

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)       

# 一、**模板方法（`Template Method`）设计模式**

## 1、**模式概述**

> **模板方法模式** 是一种 **行为型设计模式**，它在 **父类中定义一个算法的骨架（流程框架）**，而将 **某些具体步骤延迟到子类中实现**。
> 子类可以在不改变算法结构的前提下，重新定义该算法的特定步骤。   

**模板方法 = 算法骨架 + 插槽实现**：-----> **流程我定，细节你填**

- **核心思想**：**“封装不变部分，扩展可变部分”** —— 将公共流程固化在父类，差异化逻辑由子类实现。

- 它不是让你“重写整个流程”，而是让你“填空”——在框架预留的位置，填入你的具体逻辑。



## 2、**使用场景**

模板方法适用于以下情况：

- 多个子类有 **相同的处理流程**，但 **某些步骤的具体实现不同**；
- 需要 **控制子类扩展范围**（通过 `final` 方法防止关键流程被篡改）；
- 希望 **避免代码重复**，将公共逻辑上提到父类。

**典型例子**：

- 数据库操作模板（连接 → 执行 → 关闭）
- 算法框架（初始化 → 处理 → 清理）
- 测试用例执行流程（setUp → test → tearDown）



## 3、**模式角色**

### 1）`AbstractClass`（抽象类 父类） 

> **定义模板方法，并声明若干抽象方法或钩子方法供子类实现**   

- **模板方法（如 `display()`）通常声明为 `final`**，防止子类覆盖破坏流程；
- **抽象方法（如 `open()`, `print()`, `close()`）强制子类实现**；
- 可引入 **钩子方法（`Hook Method`）**（空实现的 protected 方法），提供可选扩展点。



### 2）`ConcreteClass`（具体类 ，子类）

**1、在子类中可以使用父类中定义的方法 **   

**2、在子类中重写父类的方法可以改变程序的行为**   

**3、在子类中添加方法来实现新的功能**    



## 4、**示例程序：显示不同格式的内容**

### 1）**抽象父类** `AbstractDisplay`

- `display()` 是 **final** 的，确保流程不可篡改；
- 三个抽象方法构成 **可变点**，由子类填充。

```java
/**
 * 抽象显示类 —— 定义显示流程的模板
 */
public abstract class AbstractDisplay {

    // === 抽象方法：由子类实现具体行为 ===
    public abstract void open();   // 打开显示
    public abstract void print();  // 打印内容
    public abstract void close();  // 关闭显示

    // === 模板方法：定义固定流程（final 防止被重写）===
    public final void display() {
        open();
        for (int i = 0; i < 5; i++) {
            print();
        }
        close();
    }
}
```



### 2）**具体子类 1：**`CharDisplay`

```java
/**
 * 字符显示：用 <<H>> 形式显示单个字符
 */
public class CharDisplay extends AbstractDisplay {
    private final char ch;

    public CharDisplay(char ch) {
        this.ch = ch;
    }

    @Override
    public void open() {
        System.out.print("<<");
    }

    @Override
    public void print() {
        System.out.print(ch);
    }

    @Override
    public void close() {
        System.out.println(">>");
    }
}
```



### 3） **具体子类 2：**`StringDisplay`

```java
/**
 * 字符串显示：用方框包围字符串
 */
public class StringDisplay extends AbstractDisplay {
    private final String str;
    private final int width;

    public StringDisplay(String str) {
        this.str = str;
        // 注意：生产环境建议使用 str.length() 或指定编码
        this.width = str.getBytes().length;
    }

    @Override
    public void open() {
        printLine();
    }

    @Override
    public void print() {
        System.out.println("|" + str + "|");
    }

    @Override
    public void close() {
        printLine();
    }

    // 私有辅助方法：绘制横线
    private void printLine() {
        System.out.print("+");
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }
}
```

### 4）**客户端测试** `Main`

**优势体现**：客户端只需调用 `display()`，无需关心内部是字符还是字符串，**流程统一，实现各异**。

```java
public class Main {
    public static void main(String[] args) {
        AbstractDisplay d1 = new CharDisplay('H');
        AbstractDisplay d2 = new StringDisplay("Hello, world.");
        AbstractDisplay d3 = new StringDisplay("你好，世界。");

        d1.display(); // 输出: <<HHHHH>>
        d2.display(); // 输出: 方框包围 "Hello, world."（5次）
        d3.display(); // 输出: 方框包围 "你好，世界。"（5次）
    }
}
```





## 5、`UML` 图

- `AbstractDisplay` 定义模板方法 `display()` 和抽象方法；
- `CharDisplay` 与 `StringDisplay` 实现具体行为。

![1558693456672](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1558693456672.png)

 





## 6、 **模式优点与注意事项**

### 1）**优点**

- **代码复用**：公共流程集中管理，避免重复；
- **扩展灵活**：新增子类即可支持新行为；
- **控制扩展点**：通过 `final` 保护核心流程，通过抽象方法开放定制点；
- **符合开闭原则**：对扩展开放（加子类），对修改关闭（不动父类 `final` ）。



### 2）**注意事项**

- **避免过度设计**：如果只有两个相似类，可能不需要抽象；
- **谨慎使用继承**：Java 单继承限制，若父类已存在，可考虑组合 + 策略模式替代；
- **钩子方法（`Hook`）**：可添加 `protected boolean isPrintEnabled()` 等空方法，提供条件扩展。



### 3）**与相关模式对比**

- 流程固定、步骤差异小 → **模板方法**
- 算法完全可替换、运行时动态切换 → **策略模式**

| 模式                         | 区别                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| 策略模式（`Strategy`）       | 模板方法用 继承 实现多态，策略模式用 组合 实现算法替换，更灵活 |
| 工厂方法（`Factory Method`） | 工厂方法是模板方法的一种特例（模板方法用于创建对象）         |

在实际开发中：

- `Spring` 的 `JdbcTemplate`、`RestTemplate` 是经典应用；
- `JUnit` 的 `@Before` / `@Test` / `@After` 执行流程也是模板方法；
- 任何“先 A，再 B×N，最后 C”的流程都适合此模式。

















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

