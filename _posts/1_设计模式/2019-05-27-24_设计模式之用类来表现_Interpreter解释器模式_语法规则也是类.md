---
title: 设计模式之用类来表现_Interpreter解释器模式_语法规则也是类
date: 2019-05-27 03:33:00
tags: 
- DesignPattern
category: 
- DesignPattern
description: 设计模式之用类来表现_Interpreter解释器模式_语法规则也是类
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




**前言**     

[博主github](https://github.com/HealerJean)      

[博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



## 1、解释



**解释器模式：给定一个语言，定义它的文法表示，并定义一个解释器，这个解释器使用该标识来解释语言中的句子。**    



博主说实话，这个模式有点可爱，没有实战过，怎么说，个人理解吧，    别的不想多聊，先看看这个模式的结构，如果看不懂结构，建议先看代码，再回来看结构     

先说明，本篇文档来源于大神，个人知识加入了自己的理解  ，进行本篇解释器前先看看，文法和终结符以及非终结符吧

### 1.1、文法，终结符、非终结符



####  1.1.1、文法 = 规则

文法是用于描述语言的语法结构的形式规则。没有规矩不成方圆，任何事情都要有规则，语言也一样，不管它是机器语言还是自然语言，都有它自己的文法规则。例如，  

```
有文法G2[S]为： 
S->Ap 
S->Bq 
A->a 
A->cA 
B->b 
B->dB 

则表示：S 为开始符，S，A，B 为非终结符，而p,q,a,b,c,d 为终结符

```



##### 1.1.1.1、终结符 和 非终结符   

**终结符：** 通俗的说就是不能单独出现在推导式左边的符号，也就是说终结符不能再进行   

**非终结符：** 不是终结符的都是非终结符。非终结符可理解为一个可拆分元素，而终结符是不可拆
分的最小元素。   



### 1.2、模式的结构



#### 1.2.1、抽象表达式解释器：  

声明一个所有具体表达式都要实现的抽象接口（或者抽象类），接口中主要是一个interpret()方法，称为解释操作。具体解释任务由它的各个实现类来完成，具体的解释器分别由终结符解释器和非终结符解释器完成。    



#### 1.2.2、终结符表达式：     

实现与文法中的元素相关联的解释操作，每个终结符表达式对应一种实例。。**终结符一般是文法中的运算单元**，   



**比如有一个简单的公式R=R1+R2，在里面R1和R2就是终结符，对应的解析R1和R2的解释器就是终结符表达式。**   



#### 1.2.3、非终结符表达式：    

文法中的每条规则对应于一个非终结符表达式，**非终结符表达式一般是文法中的运算符或者其他关键字**，

​      

**比如公式R=R1+R2中，+就是非终结符，解析+的解释器就是一个非终结符表达式。非终结符表达式根据逻辑的复杂程度而增加，原则上每个文法规则都对应一个非终结符表达式。**     



#### 1.2.4、环境角色：    

通常包含各个解释器需要的数据





## 2、实例代码



### 2.1、 抽象表达式`AbstractExpression`

```java
/**
 * @Description   抽象表达式
 * 定义解释器的接口，约定解释器的解释操作，主要包含解释方法 interpret()。
 */
public interface AbstractExpression {

     boolean interpret(String info);
}

```



### 2.2、终结符表达式



```java
/**
 * @author HealerJean
 * @ClassName TerminalExpression
 * @date 2019/8/23  11:52.
 * @Description 终结符表达式 用来实现文法中与终结符相关的操作，文法中的每一个"终结符"都有一个具体终结表达式与之相对应。
 * 它用集合（Set）类来保存满足条件的城市或人，
 * 并实现抽象表达式接口中的解释方法 interpret(Stringinfo)，用来判断被分析的字符串是否是集合中的终结符。
 */
class TerminalExpression implements AbstractExpression {

    private Set<String> set = new HashSet<>();

    public TerminalExpression(String[] data) {
        set.addAll(  Arrays.stream(data).collect(Collectors.toSet()));
    }

    @Override
    public boolean interpret(String info) {
        if (set.contains(info)) {
            return true;
        }
        return false;
    }
}

```





### 2.3、非终结符表达式

```java
/**
 * @author HealerJean
 * @ClassName AndExpression
 * @date 2019/8/23  11:55.
 * @Description 非终结符表达式（AndExpressicm）类，用来实现文法中与非终结符相关的操作，文法中的"每条规则"都对应于一个非终结符表达式。
 * 它也是抽象表达式的子类，它包含满足条件的城市的终结符表达式对象和满足条件的人员的终结符表达式对象，
 * 并实现 interpret(String info) 方法，用来判断被分析的字符串是否是满足条件的城市中的满足条件的人员。
 */
public class AndExpression implements AbstractExpression {

    private AbstractExpression city;

    private AbstractExpression person;

    public AndExpression(AbstractExpression city,AbstractExpression person) {
        this.city=city;
        this.person=person;
    }

    /**
     * 校验语法规则
     */
    @Override
    public boolean interpret(String info) {
        String s[] = info.split("的");
        return city.interpret(s[0]) && person.interpret(s[1]);
    }
}

```



### 2.4、环境角色 （其实也可以放到客户端调用中去）



```java

/**
 * @author HealerJean
 * @ClassName Context
 * @date 2019/8/23  11:53.
 * @Description ：通常包含各个解释器需要的数据
 * 它包含解释器需要的数据，完成对终结符表达式的初始化，并定义一个方法 freeRide(String info) 调用表达式对象的解释方法来对被分析的字符串进行解释。其结构图如图 3 所示。
 */
public class Context {


    private String[] citys = {"韶关", "广州"};
    private String[] persons = {"老人", "妇女", "儿童"};

    private AbstractExpression cityPerson;

    public Context() {
        //初始化终结符表达式
        AbstractExpression city = new TerminalExpression(citys);
        AbstractExpression person = new TerminalExpression(persons);
        cityPerson = new AndExpression(city, person);
    }

    public void freeRide(String info) {
        boolean ok = cityPerson.interpret(info);
        if (ok) {
            System.out.println("您是" + info + "，您本次乘车免费！");
        } else {
            System.out.println(info + "，您不是免费人员，本次乘车扣费2元！");
        }
    }
}

```



### 2.5、测试

```java
public class D23Main {

    public static void main(String[] args)
    {
        Context bus=new Context();
        bus.freeRide("韶关的老人");
        bus.freeRide("韶关的年轻人");
        bus.freeRide("广州的妇女");
        bus.freeRide("广州的儿童");
        bus.freeRide("山东的儿童");
    }
}




您是韶关的老人，您本次乘车免费！
韶关的年轻人，您不是免费人员，本次乘车扣费2元！
您是广州的妇女，您本次乘车免费！
您是广州的儿童，您本次乘车免费！
山东的儿童，您不是免费人员，本次乘车扣费2元！


```





### 2.3、实例代码二、



### 2.3.1、 抽象解释器

```java
public abstract class ArithmeticExpression {

     abstract int interpret();

}


/**
 * @author HealerJean
 * @ClassName CommonAbstractExpression
 * @date 2019/8/23  16:27.
 * @Description 非中介表达式公共抽象部分
 */
public abstract class CommonAbstractExpression  extends ArithmeticExpression{

    /**
     * 运算符两边的解释器
     */
    protected ArithmeticExpression exp1,exp2 ;

    public CommonAbstractExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
}





```



### 1.2、终结表达式解释器

```java

/**
 * 仅仅为了解释数字
 */
public class NumExpression extends ArithmeticExpression {
    private int num;

    public NumExpression(int num) {
        this.num = num;
    }

    @Override
    public int interpret() {
        return num;
    }
}

```



### 1.3、非终结表达式加法解释器

```java
/**
 * 加法运算解释器
 */
public class AdditionExpression extends CommonAbstractExpression {

    public AdditionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        super(exp1, exp2);
    }

    @Override
    public int interpret() {
        return exp1.interpret()+exp2.interpret();
    }
}

```





### 1.4、非终结表达式减法解释器

```java
/**
 * @author HealerJean
 * @ClassName SubtractionExpression
 * @date 2019/8/23  16:23.
 * @Description
 */
public class SubtractionExpression extends CommonAbstractExpression {


    public SubtractionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        super(exp1, exp2);
    }

    @Override
    public int interpret() {
        return exp1.interpret() - exp2.interpret();
    }
}

```



### 1.5、环境角色（计算）

```java

/**
 * @author HealerJean
 * @ClassName Calculator
 * @date 2019/8/23  16:19.
 * @Description
 */
public class Calculator {

   private   ArithmeticExpression exp1 ;
    private  ArithmeticExpression exp2 ;

    public Calculator(String expression,String rule){
        String[] elements = expression.split(rule);
        exp1 = new NumExpression(Integer.valueOf(elements[0]));
        exp2 = new NumExpression(Integer.valueOf(elements[1]));
    }

    public int  add() {
        ArithmeticExpression  add = new AdditionExpression(exp1,exp2);
        return add.interpret();
    }


    public int  sub() {
        ArithmeticExpression  sub = new SubtractionExpression(exp1,exp2);
        return sub.interpret();
    }

}

```



### 1.6、测试吧

```java
public class D02Main {

    public static void main(String[] args) {

        Calculator add = new Calculator("1+2","\\+");
        System.out.println("1+2 = " + add.add());

        Calculator sub = new Calculator("1-2","\\-");
        System.out.println("1-2 = " + sub.sub());
    }
}

```




        
        
        
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




