---
title: 编排之_规则引擎_EasyRules
date: 2023-11-94 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: 编排之_规则引擎_EasyRules
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、概念

![image-20231106194129608](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20231106194129608.png)

![image-20231106194158074](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20231106194158074.png)





## 1、规则说明

> 当一个 `facts` 参数对象传入的时候，遍历 `rules` 各个规则。每个规则进行规则的条件判断，如果满足条件，那么就触发执行相应的业务逻辑。

| 概念            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| `facts`：       | 触发规则时的一组已知事实（表示当前被传入的 `key`: `value`结构的参数） |
| `rule`          | 一整个规则                                                   |
| `Condition`     | 在给定一些事实的情况下，为了应用该规则，需要满足的一组条件（`Condition` 就是 `rule` 的判断条件） |
| `actions`：     | 满足条件时要执行的一组操作(可能会添加/删除/修改事实)（`action` 就是满足 `Condition` 以后需要触发的动作） |
|                 |                                                              |
| `name`：        | 规则命名空间中的唯一规则名称                                 |
| `description`： | 规则的简要描述                                               |
| `priority`：    | 规则的优先级                                                 |

![image-20231104164347135](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20231104164347135.png)





## 2、规则定义方式

> 如二



## 3、组合规则

> `Easy Rules`提供了3种`CompositeRule`的实现。

| 概念                     | 说明                                                         | 结果                 |
| ------------------------ | ------------------------------------------------------------ | -------------------- |
| `UnitRuleGroup`：        | 要么应用所有规则，要么不应用任何规则。                       | **要么全用要么不用** |
| `ActivationRuleGroup`：  | 激活规则组触发第一个适用规则并忽略组中的其他规则。规则首先按照其在组中的自然顺序(默认情况下优先级)进行排序。 | **首个选用**         |
| `ConditionalRuleGroup`： | 条件规则组将具有最高优先级的规则作为条件，如果具有最高优先级的规则的计算结果为 `true`，那么将触发其余的规则。 | **优先级最高说了算** |



## 4、自定义优先级

> 值越低优先级越高。要覆盖此行为，可重写`compareTo()`方法以提供自定义优先级策略。



## 5、引擎执行模式

> ⬤ "没有被触发"指的是规则的执行条件从未被满足。当一个规则的条件从未被满足时，它不会执行任何操作。例如，如果一个规则是检查系统是否在运行中，而系统实际上是关闭的，那么这个规则就不会被触发，不会执行任何操作。    
>
> ⬤ "失败"指的是规则在执行过程中遇到了错误或异常情况。这可能是因为规则的条件是错误的，或者在执行规则的操作时出现了异常。例如，如果一个规则是检查文件是否存在，但由于文件不存在，导致规则无法执行文件检查的操作，那么这个规则就会失败。

| 概念                            | 类型      | 说明                                                         | 结果                     |
| ------------------------------- | --------- | ------------------------------------------------------------ | ------------------------ |
| `skipOnFirstAppliedRule`：      | `boolean` | 这个参数决定了当一个规则被应用后，是否跳过后续的所有规则。如果设置为 true，那么当一个规则被应用后（也就是这个规则的执行动作已经完成），将不会执行后续的任何规则。这个特性可以用来防止当一个关键规则已经被应用后，仍然执行后续的规则，避免浪费资源。 | -**一个成功，不管其他**  |
| `skipOnFirstFailedRule`：       | `boolean` | 这个参数决定当一个规则执行失败后（`Action`），是否跳过后续的所有规则。如果设置为 true，那么当一个规则失败后，将不会执行后续的任何规则。 | **一个失败，不管其他**   |
| `skipOnFirstNonTriggeredRule`： | `boolean` | 这个参数决定当一个规则没有被触发时（`Action`），是否跳过后续的所有规则。如果设置为 true，那么当一个规则没有被触发（也就是这个规则的执行条件从来没有满足过）后，将不会执行后续的任何规则。这个特性可以用来防止当一个关键规则未被触发时，仍然执行后续的规则，避免浪费资源。 | **一个不符合，不管其他** |
| `rulePriorityThreshold`         | `int`     | 这个参数决定了一个规则是否需要被优先执行的阈值。如果一个规则的优先级高于这个阈值，那么这个规则将被优先执行。例如，你可以设置这个参数为 100，然后给一些重要的规则设定更高的优先级（比如 200），那么这些规则将会在所有优先级低于 100 的规则之前被执行。 |                          |



## 6、其他

### 1）多种监听器可供选择

> 支持自定义规则监听器、规则引擎监听器。



### 2）表达式语言支持

> 支持`MVEL`、`SPEL`表达式语言，可通过编程方式定义规则。



### 3）规则中的错误处理

> ⬤ 对于条件求值过程中可能发生的任何运行时异常(丢失事实、表达式中输入错误等)，引擎将记录一个警告，并认为条件求值为false。   
>
> ⬤ 对于任何在执行操作时可能发生的运行时异常(丢失事实、表达式中输入错误等)，该操作将不会执行，引擎将记录一个错误。





# 二、定义规则的方式

## 1、注解

```java
@Rule(name = "weather rule", description = "if it rains then take an umbrella")
public class WeatherRule {

  @Condition
  public boolean itRains(@Fact("rain") boolean rain) {
    return rain;
  }
  
  @Action
  public void takeAnUmbrella() {
    System.out.println("It rains, take an umbrella!");
  }
}
```

`@Condition`：`@Condition` 只能有1个，`@Condition` 访问的修饰符要 `public`，返回是 `boolean`         

`@Action`：`@Action` 可有有多个，当有多个的时候，按照 `order` 值的优先级执行，如果存在多个，则按照自然顺序执行    





## 2、链式编程

```java
Rule weatherRule = new RuleBuilder()
    .name("weather rule")
    .description("if it rains then take an umbrella")
    .when(facts -> facts.get("rain").equals(true))
    .then(facts -> System.out.println("It rains, take an umbrella!"))
    .build();
```



## 3、表达式

```java
Rule weatherRule = new MVELRule()
    .name("weather rule")
    .description("if it rains then take an umbrella")
    .when("rain == true")
    .then("System.out.println(\"It rains, take an umbrella!\");");
```



## 4、`yml` 配置文件

```java
name: "weather rule"
description: "if it rains then take an umbrella"
condition: "rain == true"
actions:
 - "System.out.println(\"It rains, take an umbrella!\");"
```



# 三、执行事件

## 1、`RuleListener`监听规则执行事件

```java
public interface RuleListener {

    /**
     * 在评估规则之前触发。
     *
     * @param rule 正在被评估的规则
     * @param facts 评估规则之前的已知事实
     * @return 如果规则应该评估，则返回true，否则返回false
     */
    default boolean beforeEvaluate(Rule rule, Facts facts) {
        return true;
    }

    /**
     * 在评估规则之后触发
     *
     * @param rule 评估之后的规则
     * @param facts 评估规则之后的已知事实
     * @param evaluationResult 评估结果
     */
    default void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) { }

    /**
     * 运行时异常导致条件评估错误时触发
     *
     * @param rule 评估之后的规则
     * @param facts 评估时的已知事实
     * @param exception 条件评估时发生的异常
     */
    default void onEvaluationError(Rule rule, Facts facts, Exception exception) { }

    /**
     * 在规则操作执行之前触发。
     *
     * @param rule 当前的规则
     * @param facts 执行规则操作时的已知事实
     */
    default void beforeExecute(Rule rule, Facts facts) { }

    /**
     * 在规则操作成功执行之后触发
     *
     * @param rule t当前的规则
     * @param facts 执行规则操作时的已知事实
     */
    default void onSuccess(Rule rule, Facts facts) { }

    /**
     * 在规则操作执行失败时触发
     *
     * @param rule 当前的规则
     * @param facts 执行规则操作时的已知事实
     * @param exception 执行规则操作时发生的异常
     */
    default void onFailure(Rule rule, Facts facts, Exception exception) { }

}
```

```java
DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
rulesEngine.registerRuleListener(myRuleListener);
```





# 四、样例

## 1、简单样例

```java
  @Test
  public void test(){
      // 1、定义规则
      Rule weatherRule = new RuleBuilder()
              .name("weather rule")
              .description("if it rains then take an umbrella")
              .when(facts -> facts.get("rain").equals(true))
              .then(facts -> System.out.println("It rains, take an umbrella!"))
              .build();
      Rules rules = new Rules();
      rules.register(weatherRule);

      // 2、定义事实
      Facts facts = new Facts();
      facts.put("rain", true);

      // 3、执行规则
      RulesEngine rulesEngine = new DefaultRulesEngine();
      rulesEngine.fire(rules, facts);
  }
```





## 2、组合规则 

### 1）单独规则 `ARule`

> 说明：要求年龄大于18

```java
@Slf4j
@Rule(name = "A rule", description = "A description", priority = 2)
public class ARule {

    @Condition
    public boolean when(@Fact("person")Person person) {
        log.info("[ARule#when] when(person.getAge() > 18 result:{})", person.getAge() > 18);
        return person.getAge() > 18;
    }

    @Action(order = 1)
    public void then1(@Fact("person") Person person) {
        log.info("[ARule#then1] success");
    }


    @Action(order = 2)
    public void then2(@Fact("person") Person person) {
        log.info("[ARule#then2] success");
    }

}

```



### 2）单独规则 `BRule`

> 要求是男人 

```java
@Slf4j
@Rule(name = "B rule", description = "B description", priority = 1)
public class BRule {

    @Condition
    public boolean when(@Fact("person")Person person) {
        log.info("[BRule#when] when(person.getGender().equals(\"male\")) result:{})", person.getGender().equals("male"));
        return person.getGender().equals("male");
    }

    @Action(order = 1)
    public void then1(@Fact("person") Person person) {
        log.info("[BRule#then1] success");
    }


    @Action(order = 2)
    public void then2(@Fact("person") Person person) {
        log.info("[BRule#then2] success");
    }

}
```





### 3）组合规则 `DemoUnitRuleGroup`

> 包装 `AgeRule`、 `GenderRule`

```java
@Slf4j
public class DemoUnitRuleGroup extends UnitRuleGroup {

    /**
     * DemoUnitRuleGroup
     *
     * @param name name
     * @param description description
     */
    public DemoUnitRuleGroup(String name, String description) {
        super(name, description);
        addRule(new ARule());
        addRule(new BRule());
    }


    /**
     * 重新了该方法，单独规则里的Action 不会执行。如果想要单独规则里的Action也执行，需要在组合规则的excute方法里增加一句 super.execute(facts); 这个需要根据实际需求来选择。
     */
    @Override
    public void execute(Facts facts) throws Exception {
        super.execute(facts);
        Object o = facts.get("person");
        log.info("[DemoUnitRuleGroup#execute] person:{}", JsonUtils.toString(o));
    }
}

```



#### a、验证 `testUnitRuleGroup`

```java
@Test
public void testUnitRuleGroup() {
    // 1、组合规则封装
    DemoUnitRuleGroup myUnitRuleGroup = new DemoUnitRuleGroup("UnitRuleGroup", 
                                                              "unit of ARule and BRule");

    // 2、组合规则注册
    Rules rules = new Rules();
    rules.register(myUnitRuleGroup);

    // 3、定义事件
    Facts facts = new Facts();
    facts.put("person", new Person()
            .setName("tom")
            .setAge(19)
            .setGender("male"));

    // 4、执行
    RulesEngine adultEngine = new DefaultRulesEngine();
    adultEngine.fire(rules, facts);
}


[ARule#when] when(person.getAge() > 18 result:true)
[BRule#when] when(person.getGender().equals("male")) result:true)
[ARule#then1] success
[ARule#then2] success
[BRule#then1] success
[BRule#then2] success
person:{"name":"tom","age":19,"gender":"male"}
```



### 4）、组合规则 `DemoActivationRuleGroup`

> 激活规则组触发第一个适用规则并忽略组中的其他规则。规则首先按照其在组中的自然顺序(默认情况下优先级)进行排序。

```java
@Slf4j
public class DemoActivationRuleGroup extends ActivationRuleGroup {

    /**
     * DemoActivationRuleGroup
     *
     * @param name name
     * @param description description
     */
    public DemoActivationRuleGroup(String name, String description) {
        super(name, description);
        addRule(new ARule());
        addRule(new BRule());
    }


    /**
     * 重新了该方法，单独规则里的Action 不会执行。如果想要单独规则里的Action也执行，需要在组合规则的excute方法里增加一句 super.execute(facts); 这个需要根据实际需求来选择。
     */
    @Override
    public void execute(Facts facts) throws Exception {
        // super.execute(facts);
        Object o = facts.get("person");
        log.info("[DemoActivationRuleGroup#execute] person:{}", JsonUtils.toString(o));
    }
}

```



#### a、验证

```java
@Test
public void testDemoActivationRuleGroup() {
    // 1、组合规则封装
    DemoActivationRuleGroup myUnitRuleGroup = new DemoActivationRuleGroup("ActivationRuleGroup", 
                                                                          "unit of ARule and BRule");

    // 2、组合规则注册
    Rules rules = new Rules();
    rules.register(myUnitRuleGroup);

    // 3、定义事件
    Facts facts = new Facts();
    facts.put("person", new Person()
            .setName("tom")
            .setAge(19)
            .setGender("male"));

    // 4、执行
    RulesEngine adultEngine = new DefaultRulesEngine();
    adultEngine.fire(rules, facts);
}


[BRule#when] when(person.getGender().equals("male")) result:true)
[DemoActivationRuleGroup#execute] person:{"name":"tom","age":19,"gender":"male"}
```





### 5）组合规则：`DemoConditionalRuleGroup`

> 条件规则组将具有最高优先级的规则作为条件，如果具有最高优先级的规则的计算结果为 `true`，那么将触发其余的规则。

```java
@Slf4j
public class DemoConditionalRuleGroup extends ConditionalRuleGroup {

    /**
     * DemoConditionalRuleGroup
     *
     * @param name name
     * @param description description
     */
    public DemoConditionalRuleGroup(String name, String description) {
        super(name, description);
        addRule(new ARule());
        addRule(new BRule());
    }


    /**
     * 重新了该方法，单独规则里的Action 不会执行。如果想要单独规则里的Action也执行，需要在组合规则的excute方法里增加一句 super.execute(facts); 这个需要根据实际需求来选择。
     */
    @Override
    public void execute(Facts facts) throws Exception {
        super.execute(facts);
        Object o = facts.get("person");
        log.info("[DemoConditionalRuleGroup#execute] person:{}", JsonUtils.toString(o));
    }
}
```



#### a、验证

```java
@Test
public void testDemoConditionalRuleGroup() {
    // 1、组合规则封装
    DemoConditionalRuleGroup myUnitRuleGroup = new DemoConditionalRuleGroup("ConditionalRuleGroup", 
                                                                            "unit of ARule and BRule");
    // 2、组合规则注册
    Rules rules = new Rules();
    rules.register(myUnitRuleGroup);

    // 3、定义事件
    Facts facts = new Facts();
    facts.put("person", new Person()
            .setName("tom")
            .setAge(19)
            .setGender("male"));

    // 4、执行
    RulesEngine adultEngine = new DefaultRulesEngine();
    adultEngine.fire(rules, facts);
}


[BRule#when] when(person.getGender().equals("male")) result:true)
[ARule#when] when(person.getAge() > 18 result:true)
[BRule#then1] success
[BRule#then2] success
[ARule#then1] success
[ARule#then2] success
[DemoConditionalRuleGroup#execute] person:{"name":"tom","age":19,"gender":"male"}
```



## 3、引擎执行模式

### 1）`skipOnFirstAppliedRule`

> 当一个规则成功应用时，跳过余下的规则。

```java
/**
 * 执行引擎1：skipOnFirstAppliedRule
 */
@Test
public void testRulesEngineParameters() {
    // 1、规则注册
    Rules rules = new Rules();
    rules.register(new ARule());
    rules.register(new BRule());

    // 2、定义事件
    Facts facts = new Facts();
    facts.put("person", new Person()
            .setName("tom")
            .setAge(19)
            .setGender("male"));

    // 3、执行
    RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
    RulesEngine adultEngine = new DefaultRulesEngine(parameters);
    adultEngine.fire(rules, facts);
}

[BRule#when] when(person.getGender().equals("male")) result:true)
[BRule#then1] success
[BRule#then2] success

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
		id: 'Fc39CnHWPGaIosMv',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



