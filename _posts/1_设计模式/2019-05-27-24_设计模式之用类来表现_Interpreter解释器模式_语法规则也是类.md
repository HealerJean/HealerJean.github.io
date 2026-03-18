---
title: 设计模式之用类来表现_Interpreter解释器模式_语法规则也是类
date: 2019-05-27 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之用类来表现_Interpreter解释器模式_语法规则也是类
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



# **一、解释器模式（Interpreter Pattern）**

## **1. 模式概述**

> **解释器模式** 是一种 **行为型设计模式**，它为语言中的句子（表达式）提供了一种表示方式，并定义了一个解释器来解释该语言中的句子。

**核心思想**：**“把语法规则变成对象，让程序能读懂自己写的规则。”**   

**解释器模式 = 语法规则对象化 + 表达式树求值**

- 它不是“写个 if-else 解析字符串”，而是“让规则自己解释自己”。

类比理解：

- **编译器/解释器**：将 `a + b * c` 转换为抽象语法树（`AST`），逐节点求值；
- **正则表达式引擎**：将 `/[0-9]+/` 编译为状态机，匹配字符串；
- **SQL 解析器**：将 `SELECT * FROM users WHERE age > 18` 解析为执行计划。



## **2. 使用场景**

解释器模式适用于以下情况：

- 语言的 **文法比较简单**（复杂文法建议用专业解析器如 ANTLR）；
- 需要 **频繁解释同一类表达式**（如规则引擎、配置脚本）；
- 表达式可表示为 **树形结构（组合模式）**；
- 需要 **动态构建和执行表达式**。

**典型应用**：

- 规则引擎（如 Drools 的简单规则）；
- 计算器（支持加减乘除表达式）；
- SQL 条件解析（`WHERE name = 'Tom' AND age > 20`）；
- 配置文件 DSL（领域特定语言）。



## 3. **示例1：公交免费乘车规则系统**

### **1）抽象表达式** `Expression`

```java
/**
 * 抽象表达式（Abstract Expression）
 */
public interface Expression {
    
   boolean interpret(Context context);
}
```



### **2）终结符表达式**

#### **a、城市终结符**

```java
public class CityExpression implements Expression {
    private final String city;

    public CityExpression(String city) {
        this.city = city;
    }

    @Override
    public boolean interpret(Context context) {
        return context.getEligibleCities().contains(city);
    }
}
```

#### **b、人群终结符**

```java
public class PersonExpression implements Expression {
    private final String person;

    public PersonExpression(String person) {
        this.person = person;
    }

    @Override
    public boolean interpret(Context context) {
        return context.getEligiblePersons().contains(person);
    }
}
```



### **3）非终结符表达式：逻辑与（AND）**

**扩展性**：可轻松添加 `OrExpression`、`NotExpression`。

```java
/**
 * 非终结符表达式（Non-terminal Expression）
 * 表示 "城市 AND 人群" 规则
 */
public class AndExpression implements Expression {
    private final Expression cityExpr;
    private final Expression personExpr;

    public AndExpression(Expression cityExpr, Expression personExpr) {
        this.cityExpr = cityExpr;
        this.personExpr = personExpr;
    }

    @Override
    public boolean interpret(Context context) {
        return cityExpr.interpret(context) && personExpr.interpret(context);
    }
}
```



### **4）上下文** `Context`

```java
import java.util.Set;

/**
 * 上下文（Context）
 * 存储解释所需的外部数据
 */
public class Context {
    private final Set<String> eligibleCities;
    private final Set<String> eligiblePersons;

    public Context(Set<String> cities, Set<String> persons) {
        this.eligibleCities = cities;
        this.eligiblePersons = persons;
    }

    public Set<String> getEligibleCities() { return eligibleCities; }
    public Set<String> getEligiblePersons() { return eligiblePersons; }
}
```



### **5）客户端：构建表达式树并解释**

- 表达式树 **可复用、可组合**；
- 解释逻辑与数据 **完全分离**；
- 新增规则（如“学生”）只需改 Context，**无需动表达式类**。

```java
public class Main {
    public static void main(String[] args) {
        // 1. 准备上下文数据
        Context context = new Context(
            Set.of("韶关", "广州"),
            Set.of("老人", "妇女", "儿童")
        );

        // 2. 构建表达式树：("韶关" AND "老人")
        Expression rule1 = new AndExpression(
            new CityExpression("韶关"),
            new PersonExpression("老人")
        );

        // 3. 解释执行
        System.out.println("韶关的老人: " + (rule1.interpret(context) ? "免费" : "收费"));

        // 4. 复用表达式结构，仅换参数
        Expression rule2 = new AndExpression(
            new CityExpression("山东"),
            new PersonExpression("儿童")
        );
        System.out.println("山东的儿童: " + (rule2.interpret(context) ? "免费" : "收费"));
    }
}
```

**输出：**

```
韶关的老人: 免费
山东的儿童: 收费
```



## **5. 示例二：算术表达式解释器（支持表达式树）**

### **1）抽象表达式**

```java
public abstract class ArithmeticExpression {
    public abstract int interpret();
}
```

### **2）终结符：数字**

```java
public class NumberExpression extends ArithmeticExpression {
    private final int value;
    public NumberExpression(int value) { this.value = value; }
    @Override public int interpret() { return value; }
}
```

### **3）非终结符：加法 & 减法**

```java
public class AddExpression extends ArithmeticExpression {
    private final ArithmeticExpression left, right;
    public AddExpression(ArithmeticExpression l, ArithmeticExpression r) {
        this.left = l; this.right = r;
    }
    @Override public int interpret() {
        return left.interpret() + right.interpret();
    }
}

public class SubExpression extends ArithmeticExpression {
    private final ArithmeticExpression left, right;
    public SubExpression(ArithmeticExpression l, ArithmeticExpression r) {
        this.left = l; this.right = r;
    }
    @Override public int interpret() {
        return left.interpret() - right.interpret();
    }
}
```

### **4）客户端：构建** `(1 + 2) - 3`

```java
public class CalcMain {
    public static void main(String[] args) {
        // 构建表达式树: (1 + 2) - 3
        ArithmeticExpression expr = new SubExpression(
            new AddExpression(
                new NumberExpression(1),
                new NumberExpression(2)
            ),
            new NumberExpression(3)
        );

        System.out.println("Result: " + expr.interpret()); // 输出: 0
    }
}
```



## 4、`FQA`

### 1）**解释器模式 vs 其他模式**

| 对比项 | 解释器模式      | 策略模式 | 命令模式      |
| ------ | --------------- | -------- | ------------- |
| 目的   | 解释语言/表达式 | 封装算法 | 封装请求      |
| 结构   | 树形（组合）    | 扁平     | 单对象        |
| 适用   | 语法规则        | 算法切换 | 请求队列/撤销 |



### 2）模式优点

- **易于改变和扩展文法**（新增表达式类即可）；
- **实现文法容易**（每个规则对应一个类）；
- **表达式可动态构建、存储、复用**；
- **天然支持组合（树形结构）**。



### **3）注意事项**

- **类爆炸**：每条文法规则需一个类；
- **性能较低**：递归解释开销大；
- **仅适合简单文法**（复杂语法用 Parser Generator）；
- **调试困难**：表达式树嵌套深。













![](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)






<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css" />    

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'Hf75ThYCwgdyPQrj',
    });
    gitalk.render('gitalk-container');
</script> 




