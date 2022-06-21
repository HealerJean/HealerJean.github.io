---
title: QLExpress_之_项目进阶
date: 2022-06-21 00:00:00
tags: 
- Java
category: 
- Java
description: QLExpress_之_项目进阶
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、简单进阶

## 1.1、`QlExpressContext`

```java
public class QlExpressContext extends HashMap<String, Object> implements IExpressContext<String, Object> {

  @Getter
  private ApplicationContext applicationContext;

  public QlExpressContext() {
  }

  public QlExpressContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public QlExpressContext(Map<String, Object> aProperties, 
                          ApplicationContext applicationContext) {
    super(aProperties);
    this.applicationContext = applicationContext;
  }

  public Object get(String key) {
    Object result = super.get(key);
    if (result == null 
        && applicationContext != null 
        && applicationContext.containsBean(key)) {
     
      result = applicationContext.getBean(key);
    }
    return result;
  }

  /**
     * 把key-value放到容器里面去
     *
     * @param key
     * @param value
     */
  @Override
  public Object put(String key, Object value) {
    return super.put(key, value);
  }
}
```

## 1.2、`QlExpressRunner`

```java
package com.healerjean.proj.qlexpress;

import com.google.common.collect.Sets;
import com.healerjean.proj.qlexpress.dto.UserDTO;
import com.healerjean.proj.qlexpress.operator.OperatorDeparmentBelong;
import com.healerjean.proj.qlexpress.operator.OperatorNotBlank;
import com.healerjean.proj.qlexpress.service.UserService;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.instruction.op.OperatorBase;
import com.ql.util.express.instruction.op.OperatorMinMax;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 语法分析和计算的入口类
 *
 * @author zhangyujin
 * @date 2022/6/20  20:51.
 */
public class QlExpressRunner extends ExpressRunner {

  /**
     * 类是否已经加载
     */
  private AtomicBoolean isRunning = new AtomicBoolean(false);
  /**
     * 函数名称集合
     */
  private Set<String> allFunctionNames = Sets.newHashSet();

  public QlExpressRunner() {
    super(true, false);
    init();
  }

  private void init() {
    // 保证之初始化一次
    if (!isRunning.compareAndSet(false, true)) {
      return;
    }

    try {
      initFunctions();
      initFunctionOfClassMethods();
      initOperatorWithAlias();
      initMacros();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }

  }
  /**
     * 初始化函数
     */
  private void initFunctions() {
    addFunction("是否不为空", new OperatorNotBlank());
    addFunction("最小", new OperatorMinMax("min"));
    addFunction("最大", new OperatorMinMax("max"));
    addFunction("部门归属", new OperatorDeparmentBelong("部门归属"));
  }

  @Override
  public void addFunction(String name, OperatorBase op) {
    super.addFunction(name, op);
    if (isRunning != null && isRunning.get()) {
      allFunctionNames.add(name);
    }
  }

  /**
     * 初始化类中已知存在的方法
     *
     * @throws Exception 异常抛出
     */
  private void initFunctionOfClassMethods() throws Exception {
    addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs", new String[]{"double"}, null);
    addFunctionOfClassMethod("registerCheck", UserService.class.getName(), "registerCheck", new String[]{UserDTO.class.getName(), "boolean"}, null);
  }

  /**
     * 替换关键字初始化
     *
     * @throws Exception 异常抛出
     */
  private void initOperatorWithAlias() throws Exception {
    addOperatorWithAlias("如果", "if", null);
    addOperatorWithAlias("则", "then", null);
    addOperatorWithAlias("否则", "else", null);
  }

  /**
     * 初始化 自定义宏
     */
  private void initMacros() throws Exception {
    addMacro("用户是否注册", "registerCheck(userInfo, login)");
  }



}

```

## 1.3、`ExpressManager`

```java
package com.healerjean.proj.qlexpress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/21  17:16.
 */
@Slf4j
@Component
public class ExpressManager implements ApplicationContextAware{

    private ApplicationContext applicationContext;


    /**
     * Spring重载方法
     * @param applicationContext Spring上下文对象
     * @throws BeansException 抛出异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 执行规则引擎
     * @param express 表达式
     * @param aProperties 上下文对象
     * @return 返回对象
     * @throws Exception 抛出异常
     */
    public Object execute(String express, Map<String, Object> aProperties) throws Exception {
        QlExpressRunner runner = new QlExpressRunner();
        QlExpressContext context = new QlExpressContext(aProperties, applicationContext);
        context.put("SYSTEM_VARIABLE_RUNNER", runner);
        return runner.execute(express, context, null, true, false);
    }

}

```

## 1.4、`Operator`

### 1.4.1、`AbstractOperator`

```java
public abstract class AbstractOperator extends OperatorBase {

    public QlExpressContext getQlExpressContext(InstructionSetContext instructionSetContext){
        IExpressContext<String, Object> parent = instructionSetContext.getParent();
        if (parent instanceof InstructionSetContext) {
            return (QlExpressContext)((InstructionSetContext) parent).getParent();
        }
        return (QlExpressContext)parent;
    }
}

```

### 1.4.2、`OperatorDeparmentBelong`

```java
public class OperatorDeparmentBelong extends AbstractOperator {

    public final static String SYSTEM_VARIABLE_DEPARTMENT = "DEPARTMENT_BELONG";

    public OperatorDeparmentBelong(String name) {
        this.name = name;
    }

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        if (list == null || list.length != 1){
            throw new IllegalArgumentException("[OperatorDeparmentBelong] 数据错误");
        }
        QlExpressContext context = super.getQlExpressContext(parent);
        if (!context.containsKey(SYSTEM_VARIABLE_DEPARTMENT)) {
            throw new IllegalArgumentException("[OperatorDeparmentBelong] 缺少 department_belong");
        }
        List<String> departments = (List<String>)context.get(SYSTEM_VARIABLE_DEPARTMENT);
        String department = (String)list.get(0).getObject(parent);
        boolean contains = departments.contains(department);
        return new OperateData(contains ? "是" : "否", String.class);
    }
}

```

### 1.4.3、`OperatorNotBlank`

```java
public class OperatorNotBlank extends Operator {

    @Override
    public Object executeInner(Object[] params) throws Exception {
        return isNotBlank(params[0]) ? true : false;
    }

    private boolean isNotBlank(Object param) {
        if (param == null) {
            return false;
        }
        if (param instanceof String) {
            return StringUtils.isNotEmpty((String) param);
        }
        return true;
    }
}

```



## 1.5、`Service`

```java
@Data
public class UserDTO {

    private Long id;

    private String name;
}

```



### 1.5.1、`UserService`

```java
package com.healerjean.proj.qlexpress.service;

import com.healerjean.proj.qlexpress.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangyujin
 * @date 2022/6/21  21:13.
 */
public class UserService {

    /**
     * 判断用户是否注册
     *
     * @param userDTO 用户dto
     * @return 注册 true，未注册 false
     */
    public UserDTO registerCheck(UserDTO userDTO, boolean login) {
        if (login &&  StringUtils.endsWithIgnoreCase("HealerJean", userDTO.getName())) {
            userDTO.setId(1L);
            return userDTO;
        }
        return null;
    }
}

```

## 1.6、`TestMain`

```java
package com.healerjean.proj.qlexpress;

import com.google.common.collect.Lists;
import com.healerjean.proj.qlexpress.dto.UserDTO;
import com.healerjean.proj.qlexpress.operator.OperatorDeparmentBelong;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/21  17:16.
 */
@Slf4j
public class TestMain {


    /**
     * 1、自定义function
     * @throws Exception 抛出异常
     */
    @Test
    public void test_function() throws Exception {
        ExpressManager expressManager = new ExpressManager();
        Map<String, Object> context = new HashMap<>();
        context.put(OperatorDeparmentBelong.SYSTEM_VARIABLE_DEPARTMENT, 
                    Lists.newArrayList("产品部", "研发部"));

        String express = "部门归属('商务部')";
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_function] 商务部 是否归属于部门:{}", 
                 JsonUtils.toJsonString(execute));

        express = "部门归属('研发部')";
        execute = expressManager.execute(express, context);
        log.info("[TestMain#test_function] 研发部 是否归属于部门:{}", 
                 JsonUtils.toJsonString(execute));
    }

    /**
     * 2、内置函数
     */
    @Test
    public void test_innner_function() throws Exception {
        ExpressManager expressManager = new ExpressManager();
        Map<String, Object> context = new HashMap<>();
        String express = "最大(1,2,5,4)";
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_innner_function] (1,2,5,4) 最大的是:{}", 
                 JsonUtils.toJsonString(execute));

    }

    /**
     * 3、验证 addFunctionOfClassMethod
     * @throws Exception 抛出异常
     */
    @Test
    public void test_addFunctionOfClassMethod() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("数字", -3);
        String express = "取绝对值(数字)";
        ExpressManager expressManager = new ExpressManager();
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test] 取绝对值:{}", JsonUtils.toJsonString(execute));
    }

    /**
     * 3、验证addOperatorWithAlias
     */
    @Test
    public void test_addOperatorWithAlias() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("喜欢", 2);

        String express = "如果(喜欢 > 1) 则  {return 结婚;} 否则 {return 分手;}";
        ExpressManager expressManager = new ExpressManager();
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addOperatorWithAlias] 恋爱状态:{}", 
                 JsonUtils.toJsonString(execute));
    }

    /**
     *
     */
    @Test
    public void test_addMacro() throws Exception {
        ExpressManager expressManager = new ExpressManager();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("HealerJean");
        Map<String, Object> context = new HashMap<>();
        context.put("userInfo", userDTO);
        context.put("login", true);

        String express = "用户是否注册";
        Object execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addMacro] name:{} 是否注册:{}", 
                 userDTO.getName(), 
                 JsonUtils.toJsonString(execute));

        userDTO.setName("zhang");
        execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addMacro] name:{} 是否注册:{}", 
                 userDTO.getName(), 
                 JsonUtils.toJsonString(execute));

        userDTO.setName("liu");
        context.put("login", false);
        execute = expressManager.execute(express, context);
        log.info("[TestMain#test_addMacro] name:{} 是否注册:{}", 
                 userDTO.getName(), 
                 JsonUtils.toJsonString(execute));
    }

}

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
		id: 'M4qkjRYinV1yoQWX',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



