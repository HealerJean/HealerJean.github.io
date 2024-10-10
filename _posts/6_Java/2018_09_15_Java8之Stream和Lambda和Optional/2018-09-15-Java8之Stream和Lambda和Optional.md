---
title: Java8之Stream和Lambda和Optional
date: 2018-09-15 03:33:00
tags: 
- Java
category: 
- Java
description: Java8之Stream和Lambda和Optional
---
# **前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             





# 一、`lambda`

## 1、`lambd` 使用前提

> **1、必须保证只有一个接口，而且其中的抽象方法有且只有一个**        
>
> **2、必须具有上下文环境（代理所在的环境）**   
>
> **3、根据局部变量的赋值来推到 `lambda` 接口**    



```java
public interface Calculator {
    int add(int a,int b);
}
```

## 2、`lambda` 表达式

| 要求     | 说明                                       |
| -------- | ------------------------------------------ |
| 一些参数 | 前面一个小括号，不需要任何参数可以直接执行 |
| 一个箭头 | 箭头指向后面要做的事情                     |
| 一些代码 | 箭头后面方法体大括号，代表具体要做的事情   |



### 1）参数写法

**1、 如果参数有多个，那么使用逗号分隔，(Person s,Person) ->{一些代码}**  


```java
Person[] array = {
  new Person("迪丽热巴",13),
  new Person("张宇晋",26),
  new Person("赵春宇",13),
  new Person("刘利",15)
};

Arrays.sort(array, (Person o1,Person o2) ->{
  return o1.getAge()-o2.getAge();
});

```



**2、如果参数没有，则留空**   


```java
cookTest(() -> System.out.println("测试"));
```



### 2）省略格式

> **1、参数的类型可以省略，同时省略所有的参数类想，要么都写，要么都不写**   
>
> **2、如果参数有且仅有一个，那么小括号可以省**   
>
> **3、如果大括号中的语句有且仅有一个，那么无论有没有返回值，return ,大括号，分号，都可以省略   **



#### a、案例测试

**1、接口**

```java
public interface CalculatorInterface {

    int add(int a, int b);
}

```

**2、启动测试**


```java
public class TestMain {

    @Test
    public void test() {
        sum((int a, int b) -> {
            return b + a;
        });

        sum((a, b) -> a + b);
    }

    public void sum(CalculatorInterface calculatorInterface) {
        int result = calculatorInterface.add(100, 200);
        System.out.println("结果是" + result);
    }

}
```






## 3、方法引用符

> >  **格式： 对象名称::方法名称**     
>
> **如果对象中有一个成员方法，正好就是lambda表示式所唯一希望使用的内容，那么这个时候就可以使用方法引用**       


```java
lambda写法     s-> System.out.println(s+"是好人")
方法引用写法    System.out::println
```



### 1）案例测试

#### a、接口  

```java
//该接口作为参数的额是，需要我们写出实现这个接口的方法 ： 
public interface PrinterInterface {
    void print(String str);
}

```

#### **b、测试** 

```java
public class TestMain {

    @Test
    public void test() {
        //  str 就是 我们接口的参数，
        //  System.out.println(str) => 接口的实现方法
        print(str -> System.out.println(str));
        print(System.out::println);
    }


    public void print(PrinterInterface printerInterface) {
        printerInterface.print("HealerJean");
    }

}

```



### 2）对象的引用输出

> 构造器引：`MethodRefObject::new`   
>
> 对象的引用输出：`new MethodRefObject()::printStringUpper`   
>
> 静态类方法引用：`MethodRefObject::staticPrintStringUpper`



#### a、普通对象案例测试

**1、接口**

```java
public interface PrinterInterface {

    void print(String str);
}

```

**2、引用对象**


```java

public class BeanObjectDTO {


    public BeanObjectDTO() {
    }

    /**
     * 构造器引用
     */
    public BeanObjectDTO(String s) {
        System.out.println("构造器引用"+s.toUpperCase());
    }

    /**
     * 静态类方法引用  MethodRefObject::staticPrintStringUpper
     */
    public static void staticPrintStringUpper(String string){
        System.out.println("静态类方法引用"+string.toUpperCase());
    }


    /**
     * 非静态的方法引用  new MethodRefObject()::printStringUpper
     */
    public void printStringUpper(String s) {
        System.out.println("非静态的方法引用"+s.toUpperCase());
    }

}

```

**3、启动测试**


```java

public class TestMain_1_普通对象的引用输出 {

    @Test
    public void test() {

        //1、lambda 常规:表达式写法
        method(s -> {
            System.out.println(s.toUpperCase());
        });
        method(System.out::println);


        //2、构造器引用
        method((s) -> {
            new BeanObjectDTO(s);
        });
        method(BeanObjectDTO::new);


        //3、对象的引用输出
        method(s -> {
            new BeanObjectDTO().printStringUpper(s);
        });
        method(new BeanObjectDTO()::printStringUpper);


        //4、静态类方法引用
        method(s -> {
            BeanObjectDTO.staticPrintStringUpper(s);
        });
        method(BeanObjectDTO::staticPrintStringUpper);
    }


    public void method(PrinterInterface printerInterface) {
        printerInterface.print("HealerJean");
    }

}

```



#### b、数组构造器的引用输出：案例测试

**1、接口**

```java
public interface ArrayBuilerInterface {

    int[] build(int length);
}

```

**2、启动测试**

```java


public class TestMain_2_数组构造器的引用输出 {

    @Test
    public void test() {
        method(s -> new int[s]);

        method(int[]::new);
    }

    public void method(ArrayBuilerInterface builerInterface){
        int [] nums = builerInterface.build(10);
    }
}
```



## 4、`lombda` 延迟加载

### 1）正常情况下，资源浪费

```java
public class TestMain {

    /**
     * 1、正常情况，资源浪费
     */
    @Test
    public void test1() {
        String msgA = "Hello";
        String msgB = "你好";
         // 如果level等于2的时候， 使用这种方法就白白拼接了，因为最终结果中也没有打印
        logger(2, msgA + msgB);
    }
    /**
     * 日志级别为1的时候，打印信息
     */
    private void logger(int level, String msg) {
        if (level == 1) {
            System.out.println(msg);
        }
    }
}
```

### 2）`lombda` 延迟加载

#### a、接口

```java
public interface MsgBuilder {

    String buildMsg();
}

```

#### b、测试

```java
public class TestMain_2_lambda延迟加载 {

    @Test
    public void test1() {
        String msgA = "Hello";
        String msgB = "你好";

         // 如果level等于2的时候， 使用这种方法就白白拼接了，因为最终结果中也没有打印
        logger(2, () -> msgA + msgB);
    }
    /**
     * 日志级别为1的时候，打印信息
     */
    private void logger(int level, MsgBuilder msgBuilder) {
        if (level == 1) {
            System.out.println(msgBuilder.buildMsg());
        }
    }
}
```



## 1.5、功能性

### 1）`function` 

> 传入参数返回结果，`Cousumer` 没有返回结果。`Function` 相当于 `stram` 中的 `map`

```java
public class TestMain {

    @Test
    public void demo() {
        method(s -> Integer.parseInt(s));
        method(Integer::parseInt);
    }

    /**
     * Function<String, Integer> 前面是参数类型，后面是返回结果
     */
    public void method(Function<String, Integer> function) {
        int num = function.apply("20");
        num += 100;
        System.out.println("结果是" + num);
    }
}
```



### 2）`Cousumer` 

> 解释：消费掉我们传入的参数  ,    **`Consumer`在 `stream`中相当于是 `forEach()`中执行**

```java
function.accept("HealerJean") 、 andThen
```


```java
public class TestMain {

    @Test
    public void test() {
        method(str -> System.out.printf(str));
        method(str -> System.out.printf(str), str -> System.out.println(str));

    }
    public void method(Consumer<String> consumer) {
        consumer.accept("HealerJean");
    }
    public void method(Consumer<String> consumer1, Consumer<String> consumer2) {
        // 先执行1，再执行2
        consumer1.andThen(consumer2).accept("HealerJean");
    }

}
```



### 3）`Supplier`：

> 解释：直接返回结果

```java
 supplier.get()
```

```java
public class TestMain {

    @Test
    public void test() {
        method(() -> "HealerJean");
    }
    public void method(Supplier<String> supplier){
        String str = supplier.get();
        System.out.println(str);
    }
}
```



### 4）`Predicate` ：

> 解释：断言，返回true或者false ，` stream`中相当于是 `filter()`中执行


```
test、 and、 or、 negate
```


```java
public class TestMain {

    @Test
    public void test() {
        method(str ->  str.equals("HealerJean"));

        method(str ->  str.equals("HealerJean"), str ->  str.equals("Jean"));

    }

    public void method(Predicate<String> predicate){
        boolean flag = predicate.test("HealerJean");
        //取反
        boolean flag2 = predicate.negate().test("HealerJean");

    }
    public void method(Predicate<String> predicate1, Predicate<String> predicate2){
        // and两个同时成立 ，or有一个成立即为true
        boolean flag = predicate1.and(predicate2).test("HealerJean");
        boolean flag2 = predicate1.or(predicate2).test("HealerJean");
    }
}
```





# 二、`Stream`

> **Stream是一次使用的，阅后即焚，stream方法会返回方法本身**     
>
> **Stram 本身不是集合，并不会存储任何元素，本身就是一个函数模型，Stream和labmda一样也有延迟执行的效果**   

**1、链式方法，返回值任然是Stream接口自身，支持链式调用，只是在进行函数模型拼接**       

**2、终结方法，返回值不在是Stram接口自身，不支持链式滴啊用，会将所有操作都触发（比如Count，forEach）**



## 1、`Collection`、`Map`、`Array`：获取`Stream`


```java
@Test
public void test() {
        //1、集合获取Stream
        Stream<Integer> stream = new ArrayList<Integer>().stream();

        //2、map获取Stream
        Stream<Integer> mapKeyStream = new HashMap<Integer, Integer>().keySet().stream();
        Stream<Integer> mapValueStream = new HashMap<Integer, Integer>().values().stream();
        Stream<Map.Entry<Integer, Integer>> mapEntryStream = new HashMap<Integer, Integer>().entrySet().stream();

        //3.1、数组获取Stream
        Stream<Integer> arrayStringStream1 = Arrays.stream(new Integer[3]);
        Stream<Integer> arrayStringStream2 = Stream.of(new Integer[3]);
        //3.2、基本类型获取数组
        IntStream intStream1 = Arrays.stream(new int[5]);
        Stream<int[]> intStream2 = Stream.of(new int[5]);

    }
```



## 2、简单属性



### 1）`filter`

> **解释：过滤，参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,**   




```java
public class TestMain {

    /**
     * filter 过滤 ，返回值为Stream
     * 功能： 查找到list中长度等于3 并且是以 张开头的
     * 解答： ilter 参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,
     * 案例：打印长度为3，以张开头的字符串
     */
    @Test
    public void filter() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");

        list.stream()
                .filter(str -> str.length() == 3)
                .filter(str -> str.startsWith("张"))
                .forEach(s -> {
                    System.out.printf(s + ",");
                });
        //输入答案
        //张三丰,张无忌,
    }

}
```



### 2）`Count`：

> 解释：统计stream 执行结果的个数   
>

```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        Long count = list.stream()
                .filter(str -> str.length() == 3)
                .filter(str -> str.startsWith("张"))
                .count();
    }
}
```



### 1）`Limit`

> 解释：获取执行结果的前几个 ,，返回值为Stream   
>


```java
public class TestMain {

    /**
     * limit 获取执行结果的前几个 ,，返回值为Stream
     */
    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        list = list.stream().limit(2).collect(Collectors.toList());
    }

}

```



### 4）`skip`

>  解释：`skip`跳过执行结果的前几个，返回值为Stream


```java
public class TestMain {

    /**
     * skip跳过执行结果的前几个，返回值为Stream
     */
    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        list = list.stream().skip(2L).collect(Collectors.toList());

        System.out.println(list.toString());
        //[岳不群, 乔峰, 爱因斯坦]
    }
}

```



### 5）`Map`

> 解释：参数是一个Function ,返回结果 ，返回值为Stream，
>


```java
public class TestMain {

    @Test
    public void test() {

        // 1、普通数据
        List<String> strList = Arrays.asList("10", "20", "30");
        List<Integer> numList = strList.stream()
            .map(s -> Integer.parseInt(s))
            .map(s -> s + 1000)
            .collect(Collectors.toList());


        //2、对象
        List<Person> personList = Arrays.asList(
            new Person(1L, "a"),
            new Person(2L, "b"),
            new Person(3L, "c"));
        List<String> names = personList.stream().map(Person::getName).collect(Collectors.toList());
        names = personList.stream().collect(Collectors.mapping(item -> item.getName(), Collectors.toList()));


        List<Long> ids = personList.stream().map(Person::getId).collect(Collectors.toList());

    }

}
```



### 6）`Concat`

> `Stream.concat` 可以可以将两个集合合并成一个整体


```java
public class TestMain {

    @Test
    public void demo() {
        List<String> list1=  Arrays.asList("张三丰");
        List<String> list2 = Arrays.asList("张无忌", "赵敏");
        
        List<String> list = Stream.concat(list2.stream(), list2.stream()).collect(Collectors.toList());
    }

}
```



### 7）`ForEach`

> 解释：如果希望对流当中的元素，进行逐一挨个处理，参数是一个Consumer接口(方法，lambda，方法引用)        
>
> **性能比较**：`for` > `forEach` > `map`         
>
> **原因分析：**`for`：`for` 循环没有额外的函数调用栈和上下文，所以它的实现最为简单。`forEach`：对于 `forEach`来说，它的函数签名中包含了参数和上下文，所以性能会低于 `for` 循环。`map`：`map` 最慢的原因是因为 `map` 会返回一个新的数组，数组的创建和赋值会导致分配内存空间，因此会带来较大的性能开销。


```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        list.stream().forEach(System.out::print);

        // for 循环不能终止，应为是吧一个一个的消费者放进去的
        list.forEach(str -> {
            if ("张无忌".equals(str)) {
                //这里的return 相当于continue没有结束循环，而是继续下一个
                return;
            }
            System.out.println(str);
        });
    }

}
```

 

### 8）`parallelStream()`：并发流

> 1、直接获取并发流 `list.parallelStream()`   
>
> 2、`list.stream().parallel()`.先获取普通流，然后变成并发的   



```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        // 1、直接获取并发流 list.parallelStream()
        list.parallelStream().forEach(System.out::print);

        // 2、先获取普通流，然后变成并发的
        list.stream().parallel().forEach(System.out::print);
    }

}
```





### 9）`distinct` 

> 解释：去重  
>

```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "张三丰");
        list.stream().distinct().forEach(System.out::print);
    }

}
```



### 10）`anyMatch`、`allMatch`、`noneMatch`   

> **使用：参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,返回的是true，或者false**

```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        boolean allMatch = list.stream().allMatch(str -> "张三丰".equals(str));
        System.out.println(allMatch);//false

        boolean noneMatch = list.stream().noneMatch(str -> "张三丰".equals(str));
        System.out.println(noneMatch);//false

        boolean anyMatch = list.stream().anyMatch(str -> "张三丰".equals(str));
        System.out.println(anyMatch);//true

    }

}
```



### 11）`findAny、findFirst`、

> `findAny`：能够从流中随便选一个元素出来，它返回一个Optional类型的元素。    
>
> `findFirst`：找出第一个。它返回一个Optional类型的元素。    

```java
public class TestMain {

    @Test
    public void test() {
        //findAny
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        Optional<String> strOptional = list.stream().findAny();
        System.out.println(strOptional.get());

        //findFirst
        Optional<String> first = list.stream().findFirst();
        System.out.println(first.get());
    }

}
```





### 12）`mapToObj`

> `mapToObj` 可以为流中的每个元素返回一个对象值流（`map` 方法只能为流中的每个元素返回另一个对象）

```java
  // 7、字符串转 int数组
  int[] nums = Arrays.stream(array).mapToInt(Integer::new).toArray();
  nums = Arrays.stream(array).mapToInt(Integer::valueOf).toArray();
  // 8、int数 转 String集合
  list = Arrays.stream(nums).mapToObj(String::valueOf).collect(Collectors.toList());
```



### 13）`flatMap`

> **`flatMap` 方法可以将多个流合并为一个流，非常适用于处理多层嵌套的集合结构**



```java
List<List<Integer>> numbers = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5, 6),
    Arrays.asList(7, 8, 9)
);

List<Integer> flattenedNumbers = numbers.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());

```



## 3、`collect`

> **解释 ：当中收集集合需要用到collect方法，方法的参数是一个`Collector`接口**
>
> **`Collector`接口通常不需要自己实现，借助工具类中的  `Collectors.toList() Collectors.toSet()` 即可**     

### 1）收集成 `Collection`

```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        // Collectors.toList()
        List<String> setList = list.stream().collect(Collectors.toList());

        // Collectors.toSet()
        Set<String> setSet = null;
        setSet = list.stream().collect(Collectors.toSet());
        setSet = list.stream().collect(Collectors.toCollection(() -> new HashSet<>()));
    }
}
```



#### a、单一属性去重

```java
@Test
public void test2() {
    List<Person> personList = Arrays.asList(
        new Person(1L, "a"),
        new Person(1L, "b"),
        new Person(2L, "b"),
        new Person(2L, "b"));

    //单属性去重
    List<Person> collect = personList.stream().collect(
        Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getName()))),
            ArrayList::new));
    System.out.println(collect);
}
```



#### b、多个属性去重

```java

@Test
public void test2() {
    List<Person> personList = Arrays.asList(
        new Person(1L, "a"),
        new Person(1L, "b"),
        new Person(2L, "b"),
        new Person(2L, "b"));

    //多属性去重
    collect = personList.stream().collect(
        Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getName() + ":" + o.getId()))),
            ArrayList::new));
    System.out.println(collect);

}
```



### 2）收集成`map`

> j解释：从下面的`Collectors.toMap`可以看到，里面需要的参数是两个`function`，`function`的参数实际上就是我们的流中的数据   
>
> **注意：收集成`map`，则Key 不能重复，否则会报错**

```java
Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends U> valueMapper) {
    return toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new);
}
```



#### a、`Collectors.toMap`：普通数据收集成`map`

```java
public class TestMain {

    /**
     * 1、普通数据
     */
    @Test
    public void test1() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "岳不群", "乔峰");
        Map<String, String> hashMap = list.stream().collect(Collectors.toMap(k -> k, v -> v + "6"));
        Map<String, String> concurrentMap = list.stream().collect(Collectors.toConcurrentMap(k -> k, v -> v + "6"));
    }

}

```



#### b、`Collectors.toMap`：对象收集成map：

```java
public class TestMain {
  
    /**
     * 2、对象
     */
    @Test
    public void test2() {
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                // 如果收集成map，则Key 不能重复，否则会报错，
                // new Person(1L, "d"),
                new Person(2L, "b"),
                new Person(3L, "c"));


        Map<Long, String> mapName = personList.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> v.getName()));


        Map<Long, Person> mapPerson = personList.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> v));

      //假如id存在重复值，则会报错Duplicate key xxx, 解决方案是：
     Map<Long, Person> map = list.stream()
        .collect(Collectors.toMap(Student::getId, v1 -> v1,(v1,v2)->v1));


```



### 3）`Collectors.mapping`：收集并映射

> **映射：先对集合中的元素进行映射，然后再对映射的结果使用 `Collectors` 操作**

```java
public class TestMain {

    /**
     * 收集并映射
     */
    @Test
    public void test3() {
        List<Person> personList = Arrays.asList(
            new Person(1L, "a"),
            new Person(2L, "b"),
            new Person(3L, "c"));

        List<String> names = personList.stream()
            .collect(Collectors.mapping(k -> k.getName(), Collectors.toList()));
    }
}

```






### 4）`groupby` 分组

#### a、`Collection`：分组收集

```java
public class TestMain {


    @Test
    public void test1() {
        List<Person> personList = Arrays.asList(
            new Person(1L, "a"),
            new Person(1L, "b"),
            new Person(2L, "b"),
            new Person(3L, "c"));

        //1、分组，组内数据收集成list
        Map<Long, List<Person>> mapPerson = personList.stream()
            .collect(Collectors.groupingBy(item -> item.getId(), Collectors.toList()));
        System.out.println(mapPerson);

        //输出答案
        //{ 1=[Person(id=1, name=a, scores=null), Person(id=1, name=b, scores=null)], 
        //  2=[Person(id=2, name=b, scores=null)], 
        //  3=[Person(id=3, name=c, scores=null)]}

    }

```



#### b、组内数据再映射（收集并映射）


```java
public class TestMain {


    @Test
    public void test1() {
        List<Person> personList = Arrays.asList(
            new Person(1L, "a"),
            new Person(1L, "b"),
            new Person(2L, "b"),
            new Person(3L, "c"));

        //1.2、组内数据再映射（收集并映射）
        Map<Long, List<String>> mapNameListPerson = personList.stream()
            .collect(Collectors.groupingBy(item -> item.getId(), 
                                           Collectors.mapping(item -> item.getName(), Collectors.toList())));
        System.out.println(mapNameListPerson);
        //输出答案
        //{1=[a, b], 2=[b], 3=[c]}

    }

```



#### c、分组计数

> 根据某个属性分组计数

```java
public class TestMain {


    @Test
    public void test1() {
        List<Person> personList = Arrays.asList(
            new Person(1L, "a"),
            new Person(1L, "b"),
            new Person(2L, "b"),
            new Person(3L, "c"));

        //2、分组计数
        Map<Long, Long> count = personList.stream()
            .collect(Collectors.groupingBy(item -> item.getId(), Collectors.counting()));
        System.out.println(count);

        //输出答案
        //{1=2, 2=1, 3=1}
    }
}
```





### 5）`Collectors.joining`：收集并拼接

 **将字符串结果用逗号隔开**  

```java
public class TestMain {

    @Test
    public void test() {
        //1、Collectors.joining()
        List<String> list = Arrays.asList("张三丰", "张无忌", "张三丰");
        String str = list.stream().collect(Collectors.joining());
        System.out.println(str); //张三丰张无忌张三丰

        //1、Collectors.joining(",")
        str = list.stream().collect(Collectors.joining(","));
        System.out.println(str); //张三丰,张无忌,张三丰
    }

}
```



### 6）`collectingAndThen`：收集然后处理

> 最终返回的是一个普通对象/普通数据

```java
public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("1","2", "3");

        String str = null;
        str = list.stream() .collect(Collectors.collectingAndThen(Collectors.joining(","), item -> item + " ,"));
        System.out.println(str); //1,2,3 ,
        str = list.stream() .collect(Collectors.collectingAndThen(Collectors.joining(","), item -> "1"));
        System.out.println(str); //1

        Optional<Integer> max =  list.stream()
            .map(Integer::valueOf)
            .collect(Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(o -> o)), item -> item));
        System.out.println(max.get()); //3
    }


}
```





## 4、聚合计算

### 1）最大值、最小值

```java
public class TestMain {

    /**
     * 求最大值和最小值
     */
    @Test
    public void maxAndMin() {
        List<Integer> list = Arrays.asList(1, 2, 4);

        //1、求最大值
        Optional<Integer> max = null;
        max = list.stream().max((o1, o2) -> o1 - o2);
        max = list.stream().max(Comparator.comparingInt(o -> o));
        max = list.stream().collect(Collectors.maxBy((o1, o2) -> o1 - o2));
        max = list.stream().collect(Collectors.maxBy(Comparator.comparingInt(o -> o)));
        max = list.stream().collect(Collectors.collectingAndThen(Collectors.maxBy((o1, o2) -> o1 - o2), item -> item));


        //1、求最小值
        Optional<Integer> min = null;
        min = list.stream().min((o1, o2) -> o1 - o2);
        min = list.stream().min(Comparator.comparingInt(o -> o));
        min = list.stream().collect(Collectors.minBy((o1, o2) -> o1 - o2));
        min = list.stream().collect(Collectors.collectingAndThen(Collectors.minBy((o1, o2) -> o1 - o2), item -> item));
    }


}
```





### 2）平均值 

```java
public class TestMain {


    /**
     * 平均值
     */
    @Test
    public void avg() {
        //1、普通数据
        List<Integer> list = Arrays.asList(1, 2, 4);
        Integer avg = list.stream().collect(Collectors.averagingInt(o -> o)).intValue();
        System.out.println(avg);

        //2、对象
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                new Person(1L, "b"),
                new Person(2L, "b"),
                new Person(3L, "c"));

        avg = personList.stream().collect(Collectors.averagingLong(person -> person.getId())).intValue();
        System.out.println(avg);
    }


}
```





### 3）求和  

```java
public class TestMain {

    /**
     * 3、求和
     */
    @Test
    public void sum() {
        //1、普通数据
        List<Integer> list = Arrays.asList(1, 2, 4);
        Integer sum = null;
        //1、reduce求和
        sum = list.stream().reduce(0, (o1, o2) -> o1 + o2);
        sum = list.stream().reduce(0, Integer::sum);
        Optional<Integer> sumOptional = list.stream().reduce(Integer::sum);

        //2、collect收集求和
        sum = list.stream().collect(Collectors.summingInt(o -> o));
    }

}
```





### 4）排序

#### a、逆序

```java
List<Integer> list = Arrays.asList(1, 2, 4);
//逆序  [4, 2, 1]
Collections.reverse(list);
```



#### b、升序

```java
List<Integer> list = Arrays.asList(1, 2, 4);
//默认升序 [1, 2, 4]
Collections.sort(list);
Collections.sort(list, (o1, o2) -> o1 - o2);
list.stream().sorted(Comparator.comparingInt(o -> o));
```



#### c、降序

```java
List<Integer> list = Arrays.asList(1, 2, 4);
//降序Collections.reverseOrder() [4, 2, 1]
Collections.sort(list, Collections.reverseOrder());
System.out.println("降序   " + list);
```



#### d、多条件排序

```java
@Test
public void sort() {

    //多条件排序
    List<SortEntry> sortEntries = new ArrayList<>();
    sortEntries.add(new SortEntry(23, 100));
    sortEntries.add(new SortEntry(27, 98));
    sortEntries.add(new SortEntry(29, 99));
    sortEntries.add(new SortEntry(29, 98));
    sortEntries.add(new SortEntry(22, 89));
    Collections.sort(sortEntries, (o1, o2) -> {
        int i = o1.getScore();
        int j = o2.getScore();  //先按照分数排序
        if (i != j) {
 						return i-j;
        }
        return o1.getAge() - o2.getAge();  //如果年龄相等了再用分数进行排序
    });
    System.out.println(sortEntries);

    //数组首个排序
    int[][] nums = {
        {1, 2},
        {3, 4},
        {2, 2}
    };
    Arrays.sort(nums, (o1, o2) -> o1[0] - o2[0]);
    Arrays.sort(nums, Comparator.comparingInt(o -> o[0]));


    //对象中子母排序
    List<SortEntry> sortEntries2 = new ArrayList<>();
    sortEntries2.add(new SortEntry("c"));
    sortEntries2.add(new SortEntry("a"));
    sortEntries2.add(new SortEntry("d"));
    sortEntries2.add(new SortEntry("A"));
    sortEntries2.add(new SortEntry("b"));
    // sortEntries2 = sortEntries2.stream().sorted(Comparator.comparing(sortEntry -> sortEntry.getName())).collect(Collectors.toList());
    Collections.sort(sortEntries2, Comparator.comparing(sortEntry -> sortEntry.getName()));
    System.out.println(sortEntries2);
}





```



#### e、数组首个排序

```java
//数组首个排序
int[][] nums = {
    {1, 2},
    {3, 4},
    {2, 2}
};
Arrays.sort(nums, (o1, o2) -> o1[0] - o2[0]);
Arrays.sort(nums, Comparator.comparingInt(o -> o[0]));

```



#### f、对象中字段排序

```java
//对象中子母排序
List<SortEntry> sortEntries2 = new ArrayList<>();
sortEntries2.add(new SortEntry("c"));
sortEntries2.add(new SortEntry("a"));
sortEntries2.add(new SortEntry("d"));
sortEntries2.add(new SortEntry("A"));
sortEntries2.add(new SortEntry("b"));
//sortEntries2 = sortEntries2.stream().sorted(Comparator.comparing(sortEntry -> sortEntry.getName())).collect(Collectors.toList());

Collections.sort(sortEntries2, Comparator.comparing(sortEntry -> sortEntry.getName()));
```



#### g、指定规则排序-**简单方法**

```java
@Test
public void sort2() {
    List<String> list = new ArrayList<>();
    list.add("c");
    list.add("a");
    list.add("b");
    list.add(null);
    list.add("d");

    List<String> sortRule = new ArrayList<>();
    sortRule.add("a");
    sortRule.add("c");
    sortRule.add("b");
    list.sort(Comparator.comparingInt(sortRule::indexOf));
    // [null, d, a, c, b]
    System.out.println(list);

    // 不在规则中的放到后面 [a, c, b, null, d]
    Comparator<String> customComparator = (s1, s2) -> {
        boolean s1InRule = sortRule.contains(s1);
        boolean s2InRule = sortRule.contains(s2);
        if (s1InRule && s2InRule) {
            return Integer.compare(sortRule.indexOf(s1), sortRule.indexOf(s2));
        } else if (s1InRule) {
            return -1; // s1 在排序规则中，s2 不在排序规则中，将 s1 放在前面
        } else if (s2InRule) {
            return 1;  // s2 在排序规则中，s1 不在排序规则中，将 s2 放在前面
        } else {
            return 0; // s1 和 s2 都不在排序规则中，保持原顺序
        }
    };
    list.sort(customComparator);
    System.out.println(list);
}
```

#### H、指定规则排序-复杂方法

```java
package com.jd.merchant.business.platform.core.service.util.comparator;

import com.jd.merchant.business.platform.core.domain.bo.ButtonBusinessBO;
import com.jd.merchant.business.platform.core.domain.bo.ButtonInfoBO;

import java.util.Comparator;

/**
 * ButtonComparator
 *
 * @author zhangyujin
 * @date 2023/11/8
 */
public class ButtonComparator implements Comparator<ButtonInfoBO<ButtonBusinessBO>> {

    /**
     * sortRules
     */
    private final String[] sortRules;

    /**
     * ButtonComparator
     *
     * @param sortRules sortRules
     */
    public ButtonComparator(String[] sortRules) {
        this.sortRules = sortRules;
    }

    /**
     * compare
     *
     * @param obj1 obj1
     * @param obj2 obj2
     * @return {@link int}
     */
    @Override
    public int compare(ButtonInfoBO<ButtonBusinessBO> obj1, ButtonInfoBO<ButtonBusinessBO> obj2) {
        int index1 = getIndex(obj1.getButtonCode(), sortRules);
        int index2 = getIndex(obj2.getButtonCode(), sortRules);
        return Integer.compare(index1, index2);
    }

    /**
     * getIndex
     *
     * @param s s
     * @param order order
     * @return {@link int}
     */
    private int getIndex(String s, String[] order) {
        for (int i = 0; i < order.length; i++) {
            if (s.equals(order[i])) {
                return i;
            }
        }
        return -1;
    }
}


String[] sortRule = new String[]{"a","c"};
buttonInfos.sort(new ButtonComparator(sortRule));
IntStream.range(0, buttonInfos.size()).forEach(i -> buttonInfos.get(i).setButtonSort(i + 1));
```





#### **i：多字段排序**

```java
  List<String> sortRule = Lists.newArrayList(
              SignEnum.CurrentSignStatusEnum.UNSIGNED.getCode(),
              SignEnum.CurrentSignStatusEnum.READY_EFFECT.getCode(),
              SignEnum.CurrentSignStatusEnum.READY_STOP_SIGN.getCode(),
              SignEnum.CurrentSignStatusEnum.ONLY_SIGNING.getCode(),
              SignEnum.CurrentSignStatusEnum.EARLY_RENEWAL.getCode(),
              SignEnum.CurrentSignStatusEnum.STOP_SIGN.getCode());
return Comparator.comparingInt(o -> sortRule.indexOf(o.getCurrentSignStatus()))
      .thenComparing(o1 -> o2.getInsuranceId())
      .compare(o1, o2);
```





# 三、`Optional`

## 1、`get()`

> 解释：如果Optional有值则将其返回，否则抛出NoSuchElementException。
>

## 2、`of`

> 解释：通过工厂方法创建Optional类，创建对象时传入的参数不能为null。如果传入参数为null，则抛出NullPointerException 。



```java

@Test
public void test1() {
    //调用工厂方法创建Optional实例
    Optional<String> name = Optional.of("Sanaulla");
    System.out.println("name:" + name.get());         //name:Sanaulla
    
    //传入参数为null，抛出NullPointerException.
    Optional<String> someNull = Optional.of(null);// java.lang.NullPointerException
    System.out.println("someNull" + someNull);
}
```



## 3、`ofNullable`

> 解释：ofNullable与of方法相似，唯一的区别是可以接受参数为null的情况，为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。    



```java
@Test
public void test2() {
    //下面创建了一个不包含任何值的Optional实例
    //例如，值为'null'
    Optional<String> empty = Optional.ofNullable(null);
    System.out.println(empty.get()); //java.util.NoSuchElementException: No value present
}
```



## 4、`isPresent`

> 解释：如果值存在返回true，否则返回false。

```java
@Test
public void isPresent() {

    Optional<String> name = Optional.ofNullable("HealerJean");

    //isPresent方法用来检查Optional实例中是否包含值
    if (name.isPresent()) {
        System.out.println(name.get());
    }
    name.ifPresent((value) -> {
        System.out.println("The length of the value is: " + value.length());
    });

    Optional<String> namenull = Optional.ofNullable(null);
    if (!namenull.isPresent()) {
        System.out.println("namenull 为 null");
    }
}
```



## 5、`orElse`、`orElseGet`

> 解释：如果有值则将其返回，否则返回指定的其它值。      
>
> `orElseGet`：`orElseGet` 与 `orElse`方法类似，区别在于得到的默认值，`orElse`方法将传入的字符串作为默认值，`orElseGet`方法可以接受 `Supplier` 接口的实现用来生成默认值。示例如下：

```java
    @Test
    public void orElse() {
        Optional<String> empty = Optional.empty();
        //如果为null，返回传入的消息。
        //输出：There is no value present!
        System.out.println(empty.orElse("There is no value present!"));


        Optional<String> name = Optional.of("HealerJean");
        //如果值不为null，orElse方法返回Optional实例的值。
        //输出：HealerJean
        System.out.println(name.orElse("There is some value!"));
    }


    @Test
    public void orElseGet() {
        Optional<String> empty = Optional.empty();
        System.out.println(empty.orElseGet(() -> "Default Value"));
        //输出：Default Value

        Optional<String> name = Optional.of("HealerJean");
        System.out.println(name.orElseGet(() -> "Default Value"));
        //输出：HealerJean

    }





   /**
     * 比较  orElse  orElseGet null一致
     */
    @Test
    public void test3() {
        Person person = null;
        System.out.println("Using orElse");
        Person result = Optional.ofNullable(person).orElse(createNewUser());
        System.out.println("Using orElseGet");
        Person result2 = Optional.ofNullable(person).orElseGet(() -> createNewUser());
    }

    /**
     * 比较  orElse  orElseGet  非null时候
     * 解释：orElse 即使不打印自身也会执行orElse里面的东西，但是orElseGet 就不会执行，而是直接返回值该有的值
     */
    @Test
    public void test4() {
        Person person = new Person(1L, "1234");
        System.out.println("Using orElse");
        Person result = Optional.ofNullable(person).orElse(createNewUser());
        System.out.println("Using orElseGet");
        Person result2 = Optional.ofNullable(person).orElseGet(() -> createNewUser());
    }

    private Person createNewUser() {
        System.out.println("Creating New User");
        return new Person(1L, "HealerJean");
    }

```



## 6、`stream `使用

```java
@Test
public void map() {
    Optional<String> name = Optional.ofNullable("HealerJean");
    Optional<String> upperName = name.map((value) -> value.toUpperCase());
}

@Test
public void filter() {
    Optional<String> name = Optional.of("HealerJean");
    Optional<String> longName = name.filter((value) -> value.length() > 6);
}
```



## 7、`orElseThrow`

```java
    Optional.ofNullable(null).orElseThrow(RuntimeException::new);
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
		id: 'IhnPb1W4UQq56LTj',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

