---
title: QLExpress
date: 2022-06-17 00:00:00
tags: 
- Java
category: 
- Java
description: QLExpress
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`QlExpress` 的基本规则语法

## 1.1、普通java语法

> ⬤ 支持 `+` , `-` , `*` , `/`,`<` ,`>` , `<=` , `>=` ,`==`, `!=` , `<>`【等同于 `!=` 】,`%` , `mod`【取模等同于`%`】,`++` , `--`,     
>
> ⬤ `in`【类似 `sql` 】,`like`【`sql` 语法】,`&&` , `||` , `!` ,等操作符     
>
> ⬤ 支持 `for` ，`break` 、`continue`、`if` `then` `else` 等标准的程序控制逻辑



```java
@Test
public void test() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  DefaultContext<String, Object> context = new DefaultContext<>();
  context.put("a", 1);
  context.put("b", 2);
  context.put("c", 3);
  String express = "a + b *c ";
  Object r = runner.execute(express, context, null, true, true);
  System.out.println(r);
}
```



## 1.2、脚本中定义 `function`

### 1.2.1、`function`

```java
function add(int a,int b){
  return a+b;
};

function sub(int a,int b){
    
  return a - b;
};

a=10;
return add(a,4) + sub(a,9);
```

### 1.2.2、`Test`

```java
@Test
public void test_functionStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  String functionStatement = "function add(int a,int b){\n" +
    "  return a+b;\n" +
    "};\n" +
    "\n" +
    "function sub(int a,int b){\n" +
    "    \n" +
    "  return a - b;\n" +
    "};\n" +
    "\n" +
    "a=10;\n" +
    "return add(a,4) + sub(a,9);";

  Object result = runner.execute(functionStatement, new DefaultContext<>(), null, false, false);
  System.out.println(result);
  // 10
}
```



## 1.3、对 `Java`对象的操作

> 系统自动会 `import java.lang.*`, `import java.util.*;`

```java
@Test
public void test_objectStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();

  //        TradeEvent tradeEvent = new TradeEvent();
  //        tradeEvent.setPrice(20.0);
  //        tradeEvent.setName("购物");
  //        tradeEvent.setId(UUID.randomUUID().toString());//
  //
  String objectStatement = "import me.aihe.demo.trade.TradeEvent;\n" +
    "        tradeEvent = new TradeEvent();\n" +
    "        tradeEvent.setPrice(20.0);\n" +
    "        tradeEvent.id=UUID.randomUUID().toString();\n" +
    "        System.out.println(tradeEvent.getId());\n" +
    "        System.out.println(tradeEvent.price);";
  runner.execute(objectStatement, new DefaultContext<>(), null, false, false);
}
```



## 1.4、扩展操作符：`Operator`

### 1.4.1、替换 `if`  `then` `else` 等关键字

```java
@Test
public void test_extendOperatorStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  runner.addOperatorWithAlias("如果", "if", null);
  runner.addOperatorWithAlias("则", "then", null);
  runner.addOperatorWithAlias("否则", "else", null);

  IExpressContext<String, Object> context = new DefaultContext<>();
  context.put("语文", 88);
  context.put("数学", 99);
  context.put("英语", 95);

  String exp = "如果  (语文 + 数学 + 英语 > 270) 则 {return 1;} 否则 {return 0;}";
  Object result = runner.execute(exp, context, null, false, false, null);
  System.out.println(result);
  //1
}
```



### 1.4.2、自定义操作符

```java
public class JoinOperator extends Operator {

  public Object executeInner(Object[] list) throws Exception {
    java.util.List result = new java.util.ArrayList();
    Object opdata1 = list[0];
    if (opdata1 instanceof java.util.List) {
      result.addAll((java.util.List) opdata1);
    } else {
      result.add(opdata1);
    }
    for (int i = 1; i < list.length; i++) {
      result.add(list[i]);
    }
    return result;
  }
}

```



#### 1.4.2.1、`addOperator` 添加操作符

```java
@Test
public void test_add_operator() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  DefaultContext<String, Object> context = new DefaultContext<String, Object>();
  String express = "1 join 2 join 3";
  runner.addOperator("join", new JoinOperator());
  Object result = runner.execute(express, context, null, true, false);
  System.out.println(result);
  // [1, 2, 3]
}
```



#### 1.4.2.2、`replaceOperator ` 替换操作符

> 替换原有的操作符，比如将 `+` 替换为join

```java
@Test
public void test_replace_operator() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  DefaultContext<String, Object> context = new DefaultContext<String, Object>();
  String express = "1 + 2 + 3";
  runner.replaceOperator("+", new JoinOperator());
  Object result = runner.execute(express, context, null, true, false);
  System.out.println(result);
  // [1, 2, 3]
}

```



#### 1.4.2.3、`addFunction ` 添加方法

```java
@Test
public void test_add_function() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  DefaultContext<String, Object> context = new DefaultContext<String, Object>();
  String express = "join(1,2,3)";
  runner.addFunction("join", new JoinOperator());
  Object result = runner.execute(express, context, null, true, false);
  System.out.println(result);
  // [1, 2, 3]
}

```





> 暂时没看懂

```java
@Test
public void test_customOperatorStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  class JoinOperator extends Operator {
    @Override
    public Object executeInner(Object[] list) throws Exception {
      Object opdata1 = list[0];
      Object opdata2 = list[1];
      if (opdata1 instanceof java.util.List) {
        ((java.util.List) opdata1).add(opdata2);
        return opdata1;
      } else {
        java.util.List result = new java.util.ArrayList();
        result.add(opdata1);
        result.add(opdata2);
        return result;
      }
    }
  }

  // 返回结果  [1, 2, 3]
  //(1)addOperator 添加操作符
  DefaultContext<String, Object> context = new DefaultContext<>();
  runner.addOperator("join", new JoinOperator());
  Object r = runner.execute("1 join 2 join 3", context, null, false, false);
  System.out.println(r);

  // (2) addFunction 添加方法
  // 返回结果：[1 , 2 ]
  runner.addFunction("joinfunc", new JoinOperator());
  r = runner.execute("joinfunc(1,2,3)", context, null, false, false);
  System.out.println(r);

  //(3)replaceOperator 替换操作符
  // 返回结果  [1, 2, 3]
  runner.replaceOperator("+", new JoinOperator());
  r = runner.execute("1 + 2 + 3", context, null, false, false);
  System.out.println(r);

}
```





## 1.5、绑定 `java`类或者对象的`method`

```java
@Test
public void test_workWithJavaStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();

  // 1、在使用的时候会创建对象
  runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs", new String[]{"double"}, null);

  // 2、对象已经存在，直接调用对象中的方法
  runner.addFunctionOfServiceMethod("打印", System.out, "println", new String[]{"String"}, null);

  String exp = "a = 取绝对值(-100); 打印(\"Hello World\"); 打印(a.toString())";
  DefaultContext<String, Object> context = new DefaultContext<>();
  runner.execute(exp, context, null, false, false);
  System.out.println(context);
}


```



## 1.6、`macro` 宏定义

> 即预先定义一些内容，在使用的时候直接替换 `Macro` 中的变量为上下文的内容

```java
@Test
public void test_macronStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  //没有顺序限制
  runner.addMacro("是否优秀", "计算平均成绩 > 90");
  runner.addMacro("计算平均成绩", "(语文 + 数学 + 英语 ) /3.0");

  IExpressContext<String, Object> context = new DefaultContext<>();
  context.put("语文", 88);
  context.put("数学", 99);
  context.put("英语", 95);
  Object result = runner.execute("是否优秀", context, null, false, false);
  System.out.println(result);
}

```



## 1.7、编译脚本，查询外部需要定义的变量和函数。

```java
@Test
public void test_getOutVarNames() throws Exception {
  String express = "int 平均分 = (语文+数学+英语+综合考试.科目2)/4.0;return 平均分";
  ExpressRunner runner = new ExpressRunner(true,true);
  String[] names = runner.getOutVarNames(express);
  for(String s:names){
    System.out.println("var : " + s);
  }

  // var : 数学
  // var : 综合考试
  // var : 英语
  // var : 语文

}

```



## 1.8、集合的快速写法

```java
@Test
public void test_fast() throws Exception {
  ExpressRunner runner = new ExpressRunner(false,false);
  DefaultContext<String, Object> context = new DefaultContext<>();
  String express = "abc = NewMap(1:1,2:2); return abc.get(1) + abc.get(2);";
  Object r = runner.execute(express, context, null, false, false);
  System.out.println(r);
  //3

  express = "abc = NewList(1,2,3); return abc.get(1)+abc.get(2)";
  r = runner.execute(express, context, null, false, false);
  System.out.println(r);
  //5

  express = "abc = [1,2,3]; return abc[1]+abc[2];";
  r = runner.execute(express, context, null, false, false);
  System.out.println(r);
  //5

}
```



## 1.9、集合的遍历

### 1.9.1、遍历 `Map`

#### 1.9.1.1、`Test`

```java
keySet = map.keySet();
objArr = keySet.toArray();
for (i=0;i<objArr.length;i++) {
  key = objArr[i];
  System.out.println(map.get(key));
}
```

```java
@Test
public void test_collectionStatement() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  DefaultContext<String, Object> defaultContext = new DefaultContext<>();
  HashMap<String, String> mapData = new HashMap() {{
    put("a", "hello");
    put("b", "world");
    put("c", "!@#$");
  }};
  defaultContext.put("map", mapData);
  //ql不支持for(obj:list){}的语法，只能通过下标访问。
  String mapTraverseStatement = " keySet = map.keySet();\n" +
    "  objArr = keySet.toArray();\n" +
    "  for (i=0;i<objArr.length;i++) {\n" +
    "  key = objArr[i];\n" +
    "   System.out.println(map.get(key));\n" +
    "  }";
  runner.execute(mapTraverseStatement, defaultContext, null, false, false);
  System.out.println(defaultContext);

  // hello
  // world
  // !@#$
  // {i=3, objArr=[Ljava.lang.Object;@6b419da, map={a=hello, b=world, c=!@#$}, keySet=[a, b, c], key=c}

}
```



# 2、运行参数和 `api` 介绍

## 2.1、属性开关

### 2.1.1、`isPrecise` ：是否需要高精度计算

> 高精度计算在会计财务中非常重要，`java` 的 `float` 、`double` 、`int` 、`long` 存在很多隐式转换，做四则运算和比较的时候其实存在非常多的安全隐患。 所以类似汇金的系统中，会有很多 `BigDecimal` 转换代码。而使用 `QLExpress`，你只要关注数学公式本身 订单总价 = 单价 * 数量 + 首重价格 + （ 总重量 - 首重） * 续重单价 ，然后设置这个属性即可，所有的中间运算过程都会保证不丢失精度。

```java
@Test
public void is_precise() throws Exception {
  // 一个参数是是否开启高精度默认false，第二个参数是是否开启trace。
  ExpressRunner runner = new ExpressRunner(false, false);
  DefaultContext<String, Object> context = new DefaultContext<>();
  //订单总价 = 单价 * 数量 + 首重价格 + （总重量 - 首重） * 续重单价
  context.put("单价", 1.25);
  context.put("数量", 100);
  context.put("首重价格", 125.25);
  context.put("总重量", 20.55);
  context.put("首重", 10.34);
  context.put("续重单价", 333.33);

  String express = "单价 * 数量 + 首重价格 + ( 总重量 - 首重 ) / 续重单价";
  Object totalPrice = runner.execute(express, context, null, true, false);
  System.out.println("totalPrice:" + totalPrice);
}
```



### 2.1.2、`isShortCicuit` ：是否使用逻辑短路特性

> 在很多业务决策系统中，往往需要对布尔条件表达式进行分析输出，普通的 `java运算一般会通过逻辑短路来减少性能的消耗。例如规则公式： *star > 10000 and shopType in ('tmall', 'juhuasuan') and price between (100, 900)* 假设第一个条件 *star>10000* 不满足就停止运算。但业务系统却还是希望把后面的逻辑都能够运算一遍，并且输出中间过程，保证更快更好的做出决策。

```java
@Test
public void testShortCircuit() throws Exception {
  ExpressRunner runner = new ExpressRunner();
  runner.setShortCircuit(false);
  runner.addOperatorWithAlias("小于", "<", "$1 小于 $2 不满足期望");
  runner.addOperatorWithAlias("大于", ">", "$1 大于 $2 不满足期望");

  IExpressContext<String, Object> expressContext = new DefaultContext<>();
  expressContext.put("违规天数", 100);
  expressContext.put("虚假交易扣分", 13);
  expressContext.put("VIP", false);
  List<String> errorInfo = new ArrayList<>();
  String expression = "2 小于 2 and (违规天数 小于 90 or 虚假交易扣分 小于 12)";
  boolean result = (Boolean)runner.execute(expression, expressContext, errorInfo, true, false);
  if (result) {
    System.out.println("result is success!");
  } else {
    System.out.println("result is fail!");
    for (String error : errorInfo) {
      System.out.println(error);
    }
  }
}

runner.setShortCircuit(false); 
result is fail!
 2  小于  2  不满足期望
 违规天数:100  小于  90  不满足期望
 虚假交易扣分:13  小于  12  不满足期望

   
runner.setShortCircuit(true); 
result is fail!
2  小于  2  不满足期望
```



### 2.1.3、`isTrace`：

> 是否输出所有的跟踪信息，同时还需要 `log` 级别是 `DEBUG` 级别    
>
> ⬤ 这个主要是是否输出脚本的编译解析过程，一般对于业务系统来说关闭之后会提高性能。

```java
ExpressRnner runner = new ExpressRunner(true, false);//第二个参数是否开启trace
```



## 2.2、调用入参

```java
/**
 * 执行一段文本
 * @param expressString 程序文本
 * @param context 执行上下文，可以扩展为包含ApplicationContext
 * @param errorList 输出的错误信息List
 * @param isCache 是否使用Cache中的指令集,建议为true
 * @param isTrace 是否输出详细的执行指令信息，建议为false
 * @param aLog 输出的log
 * @return
 * @throws Exception
 */
Object execute(String expressString, 
               IExpressContext<String, Object> context, 
               List<String> errorList, 
               boolean isCache, 
               boolean isTrace, 
               Log aLog);
```

| 参数            | 说明                                       |
| --------------- | ------------------------------------------ |
| `expressString` | 程序文本                                   |
| `context`       | 可以扩展为包含`ApplicationContext`         |
| `errorList`     | 输出的错误信息 `List`                      |
| `isCache`       | 是否使用 `Cache` 中的指令集,建议为 `true`  |
| `isTrace`       | 是否输出详细的执行指令信息，建议为 `false` |
| `aLog`          | 输出的 `log`                               |



## 2.3、功能扩展 `API` 列表

> `QLExpress` 主要通过子类实现 `Operator.java` 提供的以下方法来最简单的操作符定义，然后可以被通过 `addFunction` 或者`addOperator` 的方式注入到 `ExpressRunner` 中。     
>
> ⬤ **具体参见上面的 `JoinOperator` 的例子。**    
>
> ⬤ 如果你使用 `Operator` 的基类 `OperatorBase.java` 将获得更强大的能力，基本能够满足所有的要求。





### 2.3.1、`function` 相关API

```java
//通过name获取function的定义
OperatorBase getFunciton(String name);

//通过自定义的Operator来实现类似：fun(a, b, c)
void addFunction(String name, OperatorBase op);

//fun(a, b, c) 绑定 object.function(a, b, c)对象方法
void addFunctionOfServiceMethod(String name, Object aServiceObject, String aFunctionName, Class<?>[] aParameterClassTypes, String errorInfo);

//fun(a, b, c) 绑定 Class.function(a, b, c)类方法
void addFunctionOfClassMethod(String name, String aClassName, String aFunctionName, Class<?>[] aParameterClassTypes, String errorInfo);

//给Class增加或者替换method，同时支持 a.fun(b), fun(a, b) 两种方法调用
//比如扩展String.class的isBlank方法:"abc".isBlank()和isBlank("abc")都可以调用
void addFunctionAndClassMethod(String name, Class<?> bindingClass, OperatorBase op);
```



### 2.3.2、`Operator` 相关 `API`

> 提到脚本语言的操作符，优先级、运算的目数、覆盖原始的操作符(+,-,*,/等等)都是需要考虑的问题，`QLExpress` 统统帮你搞定了。

```java
//添加操作符号,可以设置优先级
void addOperator(String name,Operator op);
void addOperator(String name,String aRefOpername,Operator op);

//替换操作符处理
//比如将 + 替换成自定义的 operator 见上面的例子
OperatorBase replaceOperator(String name,OperatorBase op);

//添加操作符和关键字的别名，比如 if..then..else -> 如果。。那么。。否则。。
//具体见上面的例子
void addOperatorWithAlias(String keyWordName, 
                          String realKeyWordName,
                          String errorInfo);
```



### 2.3.3、宏相关 `API`

> `QLExpress` 的宏定义比较简单，就是简单的用一个变量替换一段文本，和传统的函数替换有所区别。      
>
> 见上面使用宏计算是否优秀的例子

```java
//比如addMacro("天猫卖家","userDO.userTag &1024 ==1024")
void addMacro(String macroName,String express) 
```



### 2.3.4、`java` `class` 相关 `API`

> `QLExpress` 可以通过给 `java` 类增加或者改写一些`method` 和 `field`，比如链式调用：`"list.join("1").join("2")"`，比如中文属性："`list.长度`"。     
>
> **注意：**这些类的字段和方法是执行器通过解析语法执行的，而不是通过字节码增强等技术，所以只在脚本运行期间生效，不会对 `jvm` 整体的运行产生任何影响，所以是绝对安全的。

```java
//添加类的属性字段
void addClassField(String field, Class<?>bindingClass, Class<?>returnType, Operator op);

//添加类的方法
void addClassMethod(String name, Class<?>bindingClass, OperatorBase op);
```



### 2.3.5、语法树解析变量、函数的 `API`

> 这些接口主要是对一个脚本内容的静态分析，可以作为上下文创建的依据，也可以用于系统的业务处理。 比如：计算 "a + fun1(a) + fun2(a + b) + c.getName()" 包含的变量: a, b, c 包含的函数: fun1, fun2

```java
//获取一个表达式需要的外部变量名称列表
String[] getOutVarNames(String express);
String[] getOutFunctionNames(String express);
```



### 2.3.6、语法解析校验 `api`

> 脚本语法是否正确，可以通过 `ExpressRunner` 编译指令集的接口来完成。

```java
String expressString = "for(i = 0; i < 10; i++) {sum = i + 1;} return sum;";
InstructionSet instructionSet = expressRunner.parseInstructionSet(expressString);
//如果调用过程不出现异常，指令集instructionSet就是可以被加载运行（execute）了！
```



### 2.3.7、指令集缓存相关的 `api`

> 因为 `QLExpress` 对文本到指令集做了一个本地 `HashMap`缓存，通常情况下一个设计合理的应用脚本数量应该是有限的，缓存是安全稳定的，但是也提供了一些接口进行管理。

```java
//优先从本地指令集缓存获取指令集，没有的话生成并且缓存在本地
InstructionSet getInstructionSetFromLocalCache(String expressString);
//清除缓存
void clearExpressCache();
```



### 2.3.8、安全风险控制

#### 2.3.8.1、防止死循环

```java
try {
  express = "sum = 0; for(i = 0; i < 1000000000; i++) {sum = sum + i;} return sum;";
  //可通过timeoutMillis参数设置脚本的运行超时时间:1000ms
  Object r = runner.execute(express, context, null, true, false, 1000);
  System.out.println(r);
  throw new Exception("没有捕获到超时异常");
} catch (QLTimeOutException e) {
  System.out.println(e);
}
```

#### 2.3.8.2、防止调用不安全的系统 `api`

```java
ExpressRunner runner = new ExpressRunner();
QLExpressRunStrategy.setForbiddenInvokeSecurityRiskMethods(true);

DefaultContext<String, Object> context = new DefaultContext<String, Object>();
try {
  express = "System.exit(1);";
  Object r = runner.execute(express, context, null, true, false);
  System.out.println(r);
  throw new Exception("没有捕获到不安全的方法");
} catch (QLException e) {
  System.out.println(e);
}
```



### 2.3.9、增强上下文参数 `Context` 相关的 `api`

> 上下文参数 `IExpressContext` `context` 非常有用，它允许 `put` 任何变量，然后在脚本中识别出来。在实际中我们很希望能够无缝的集成到 `spring` 框架中，可以仿照下面的例子使用一个子类。

#### 2.3.9.1、与 `spring` 框架的无缝集成

```java
public class QLExpressContext extends HashMap<String, Object> implements IExpressContext<String, Object> {
  private final ApplicationContext context;

  // 构造函数，传入context 和 ApplicationContext
  public QLExpressContext(Map<String, Object> map, ApplicationContext aContext) {
    super(map);
    this.context = aContext;
  }

  /**
     * 抽象方法：根据名称从属性列表中提取属性值
     */
  public Object get(Object name) {
    Object result;
    result = super.get(name);
    try {
      if (result == null && this.context != null && this.context.containsBean((String)name)) {
        // 如果在Spring容器中包含bean，则返回String的Bean
        result = this.context.getBean((String)name);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public Object put(String name, Object object) {
    return super.put(name, object);
  }
}
```

#### 2.3.9.2、自定义函数操作符获取原始的 `context` 控制上下文

> 自定义的 `Operator` 需要直接继承 `OperatorBase` ，获取到 `parent` 即可，可以用于在运行一组脚本的时候，直接编辑上下文信息，业务逻辑处理上也非常有用。

```java
public class ContextMessagePutTest {
  class OperatorContextPut extends OperatorBase {
    public OperatorContextPut(String aName) {
      this.name = aName;
    }

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
      String key = list.get(0).toString();
      Object value = list.get(1);
      parent.put(key, value);
      return null;
    }
  }

  @Test
  public void test() throws Exception {
    ExpressRunner runner = new ExpressRunner();
    OperatorBase op = new OperatorContextPut("contextPut");
    runner.addFunction("contextPut", op);
    String express = "contextPut('success', 'false'); contextPut('error', '错误信息'); contextPut('warning', '提醒信息')";
    IExpressContext<String, Object> context = new DefaultContext<String, Object>();
    context.put("success", "true");
    Object result = runner.execute(express, context, null, false, true);
    System.out.println(result);
    System.out.println(context);
  }
}
```



### 2.3.10、多级别安全控制

> `QLExpress` 与本地  `JVM` 交互的方式有：
>
> ⬤ 应用中的自定义函数/操作符/宏: 该部分不在 `QLExpress `运行时的管控范围，属于应用开放给脚本的业务功能，不受安全控制，应用需要自行确保这部分是安全的     
>
> ⬤ 在 `QLExpress` 运行时中发生的交互: 安全控制可以对这一部分进行管理, `QLExpress` 会开放相关的配置给应用
>
> - 通过 `.` 操作符获取 `Java` 对象的属性或者调用 `Java` 对象中的方法        
> - 通过 `import` 可以导入 `JVM` 中存在的任何类并且使用, 默认情况下会导入 `java.lang`, `java.util` 以及 `java.util.stream`    
>
> 在不同的场景下，应用可以配置不同的安全级别，安全级别由低到高：
>
> 1、黑名单控制：`QLExpress` 默认会阻断一些高危的系统 `API,` 用户也可以自行添加, 但是开放对 `JVM` 中其他所有类与方法的访问, 最灵活, 但是很容易被反射工具类绕过，只适用于脚本安全性有其他严格控制的场景，禁止直接运行终端用户输入      
>
> **2、白名单控制：`QLExpress` 支持编译时白名单和运行时白名单机制,** 编译时白名单设置到类级别, 能够在语法检查阶段就暴露出不安全类的使用, 但是无法阻断运行时动态生成的类(比如通过反射), 运行时白名单能够确保运行时只可以直接调用有限的` Java` 方法, 必须设置了运行时白名单, 才算是达到了这个级别     
>
> 3、沙箱模式：`QLExpress` 作为一个语言沙箱, 只允许通过自定义函数/操作符/宏与应用交互, 不允许与 JVM 中的类产生交互

#### 2.3.10.1、黑名单控制

**1、`QLExpess` 目前默认添加的黑名单有：**

◯ `java.lang.System.exit`    

◯ `java.lang.Runtime.exec`      

◯ `java.lang.ProcessBuilder.start`      

◯ `java.lang.reflect.Method.invoke`   

◯  `java.lang.reflect.Class.forName`    

◯  `java.lang.reflect.ClassLoader.loadClass`      

◯ `java.lang.reflect.ClassLoader.findClass`



2、支持通过 `QLExpressRunStrategy.addSecurityRiskMethod` 额外添加

```java
// 必须将该选项设置为 true
QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
// 这里不区分静态方法与成员方法, 写法一致
// 不支持重载, riskMethod 的所有重载方法都会被禁止
QLExpressRunStrategy.addSecurityRiskMethod(RiskBean.class, "riskMethod");
ExpressRunner expressRunner = new ExpressRunner();
DefaultContext<String, Object> context = new DefaultContext<>();
try {
  expressRunner.execute("import com.ql.util.express.example.RiskBean;" +
                        "RiskBean.riskMethod()", context, null, true, false);
  fail("没有捕获到不安全的方法");
} catch (Exception e) {
  assertTrue(e.getCause() instanceof QLSecurityRiskException);
}
```

#### 2.3.10.2、白名单控制

> **白名单有两种设置方式：**   
>
>  ⬤ 添加：`QLExpressRunStrategy.addSecureMethod`         
>
> ⬤ 置换：`QLExpressRunStrategy.setSecureMethods`

在应用中使用的时，推荐将白名单配置在诸如 `etcd`,`configServer` 等配置服务中，根据需求随时调整。

**1、编译期白名单：**编译期白名单是类维度的，脚本中只允许显式引用符合白名单条件的类，支持两种设置方式，精确设置某个类，以及设置某个类的全部子类。      

编译期白名单只能检测出脚本编译时能够确认的类型，任何运行时出现的类型都是无法检测的，诸如各种反射`Class.forName`, `ClassLoader.loadClass`，或者没有声明类型的变量等等，因为编译期白名单只能增加黑客的作案成本，是容易被绕过。因此建议编译期白名单只用来帮助脚本校验，如果需要接收终端用户输入，运行期白名单是务必要配置的。

```java
// 设置编译期白名单
QLExpressRunStrategy.setCompileWhiteClassList(Arrays.asList(
  // 精确设置
  CheckerFactory.must(Date.class),
  // 子类设置
  CheckerFactory.assignable(List.class)
));
ExpressRunner expressRunner = new ExpressRunner();
// Date 在编译期白名单中, 可以显示引用
expressRunner.execute("new Date()", new DefaultContext<>(), null,
                      false, true);
// LinkedList 是 List 的子类, 符合白名单要求
expressRunner.execute("LinkedList ll = new LinkedList; ll.add(1); ll.add(2); ll",
                      new DefaultContext<>(), null, false, true);
try {
  // String 不在白名单中, 不可以显示引用
  // 但是隐式引用, a = 'mmm', 或者定义字符串常量 'mmm' 都是可以的
  expressRunner.execute("String a = 'mmm'", new DefaultContext<>(), null,
                        false, true);
} catch (Exception e) {
  assertTrue(e.getCause() instanceof QLSecurityRiskException);
}

// Math 不在白名单中
// 对于不满足编译期类型白名单的脚本无需运行, 即可通过 checkSyntax 检测出
assertFalse(expressRunner.checkSyntax("Math.abs(-1)"));
```



**2、运行时白名单**：

⬤ 如果有白名单设置，所有的黑名单设置就都会无效，以白名单为准。默认没有白名单设置。

```java
// 必须将该选项设置为 true
QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
// 有白名单设置时, 则黑名单失效
QLExpressRunStrategy.addSecureMethod(RiskBean.class, "secureMethod");
// 白名单中的方法, 允许正常调用
expressRunner.execute("import com.ql.util.express.example.RiskBean;" +
                      "RiskBean.secureMethod()", context, null, true, false);
try {
    // java.lang.String.length 不在白名单中, 不允许调用
    expressRunner.execute("'abcd'.length()", context,
                          null, true, false);
    fail("没有捕获到不安全的方法");
} catch (Exception e) {
    assertTrue(e.getCause() instanceof QLSecurityRiskException);
}

// setSecureMethods 设置方式
Set<String> secureMethods = new HashSet<>();
secureMethods.add("java.lang.String.length");
secureMethods.add("java.lang.Integer.valueOf");
QLExpressRunStrategy.setSecureMethods(secureMethods);
// 白名单中的方法, 允许正常调用
Object res = expressRunner.execute("Integer.valueOf('abcd'.length())", context,
                                   null, true, false);
assertEquals(4, res);
try {
    // java.lang.Long.valueOf 不在白名单中, 不允许调用
    expressRunner.execute("Long.valueOf('abcd'.length())", context,
                          null, true, false);
    fail("没有捕获到不安全的方法");
} catch (Exception e) {
    assertTrue(e.getCause() instanceof QLSecurityRiskException);
}
```

#### 2.3.10.3、沙箱模式

> 如果你厌烦上述复杂的配置，只是想完全关闭 QLExpress 和 Java 应用的自由交互，那么推荐使用沙箱模式。
>
> **在沙箱模式中，脚本不可以：**    
>
> ⬤ import Java 类       
>
> ⬤ 显式引用 Java 类，比如 `String a = 'mmm'`        
>
> ⬤ 取 Java 类中的字段：`a = new Integer(11); a.value`        
>
> ⬤ 调用 Java 类中的方法：`Math.abs(12)`
>
> **在沙箱模式中，脚本可以：**
>
> ⬤ 使用 QLExpress 的自定义操作符/宏/函数，以此实现与应用的受控交互      
>
> ⬤ 使用 `.` 操作符获取 `Map` 的 `key` 对应的 `value`，比如 `a` 在应用传入的表达式中是一个 `Map`，那么可以通过 `a.b` 获取     
>
> ⬤ 所有不涉及应用 Java 类的操作

```java
// 开启沙箱模式
QLExpressRunStrategy.setSandBoxMode(true);
ExpressRunner expressRunner = new ExpressRunner();
// 沙箱模式下不支持 import 语句
assertFalse(expressRunner.checkSyntax("import com.ql.util.express.example.RiskBean;"));
// 沙箱模式下不支持显式的类型引用
assertFalse(expressRunner.checkSyntax("String a = 'abc'"));
assertTrue(expressRunner.checkSyntax("a = 'abc'"));
// 无法用 . 获取 Java 类属性或者 Java 类方法
try {
    expressRunner.execute("'abc'.length()", new DefaultContext<>(),
                          null, false, true);
    fail();
} catch (QLException e) {
    // 没有找到方法:length
}
try {
    DefaultContext<String, Object> context = new DefaultContext<>();
    context.put("test", new CustBean(12));
    expressRunner.execute("test.id", context,
                          null, false, true);
    fail();
} catch (RuntimeException e) {
    // 无法获取属性:id
}

// 沙箱模式下可以使用 自定义操作符/宏/函数 和应用进行交互
expressRunner.addFunction("add", new Operator() {
    @Override
    public Object executeInner(Object[] list) throws Exception {
        return (Integer) list[0] + (Integer) list[1];
    }
});
assertEquals(3, expressRunner.execute("add(1,2)", new DefaultContext<>(),
                                      null, false, true));
// 可以用 . 获取 map 的属性
DefaultContext<String, Object> context = new DefaultContext<>();
HashMap<Object, Object> testMap = new HashMap<>();
testMap.put("a", "t");
context.put("test", testMap);
assertEquals("t", expressRunner.execute("test.a", context,
                                        null, false, true));
```











![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)



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
		id: 'KEcsWhm8MviFTU9u',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



