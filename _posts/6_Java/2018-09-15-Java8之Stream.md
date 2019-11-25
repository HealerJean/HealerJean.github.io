---
title: Java8之Stream
date: 2018-09-15 03:33:00
tags: 
- Java
category: 
- Java
description: Java8之Stream
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进

-->

## 前言

对我而言，为什么要引入stream，傻子都知道，不好用，还引入干什么for 循环遍历不太好，for循环是形式,\for循环不是进行过滤的必要手段，这里使用stream进行过滤更高效  




### 1、stream是一次使用的，阅后即焚
### 2、stream方法会返回方法本身   

**Stram 本身不是集合，并不会存储任何元素，本身就是一个函数模型，Stream和labmda一样也有延迟执行的效果**



* **1、链式方法，返回值任然是Stream接口自身，支持链式调用，只是在进行函数模型拼接**   
* **2、终结方法，返回值不在是Stram接口自身，不支持链式滴啊用，会将所有操作都触发（比如Count，forEach）**






## 1、获取Stram的3种方法

### 1.1、Collection


```java
package com.hlj.java8.Stream;

import com.hlj.arithmetic.Array;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Demo02GetStream {


    @Test
    public void getStream(){

     
        Collection<String> collection = new ArrayList<>();
        Stream<String> stream = collection.stream();
        
    }
}


```

### 1.2、Map

```java

package com.hlj.java8.Stream;

import com.hlj.arithmetic.Array;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Demo02GetStream {

 	/**
	 * 2、通过Map
 	*/
   @Test
 	public void getStream(){

		Map<String,Integer> map = new HashMap<>();
		//获取key的stream
		Stream<String> mapKeyStream =  map.keySet().stream() ;
		//获取value的stream
		Stream<Integer> mapValueStream =  map.values().stream() ;
		//获取所有键值对
		Stream<Map.Entry<String,Integer>> entryStream = map.entrySet().stream();

    }
}

```

### 1.3、数组

**1、通过数组 推荐使用Arrays.stream 数组也用的不过，String类型最多**    

**2、Stream.of(ints)，或者可以使用快捷键 alt+enter自动补全**   




```java
package com.hlj.java8.Stream;

import com.hlj.arithmetic.Array;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Demo02GetStream {


    @Test
    public void getStream(){


		String[] strings= {"张三丰,刘利"};
		Stream<String> stringStream = Arrays.stream(strings);
		Stream<String> stringStream2 =Stream.of(strings);
		
		int[] ints= new int[5];
		IntStream intStream = Arrays.stream(ints);
		Stream<int[]> intStream2 = Stream.of(ints);
	



}

```



## 2、基本方法 



### 2.1、filter：过滤

> **解释：参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,**   




```java
package com.hlj.java8.Stream;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author HealerJean
 * @Date 2018/9/14  上午11:09.
 */
public class Demo01Filter {

    /**
     * 1、 filter 过滤 ，返回值为Stream
     * 功能： 查找到list中长度等于3 并且是以 张开头的
     * 解答：1、filter 参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,
      */
    @Test
    public  void filter(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");


        list.stream().filter((String s) -> {return s.length() == 3 ;} )
                .filter(s -> s.startsWith("张"))
                .forEach(s -> {
                    System.out.println(s);
                });
        System.out.println("---------------");



        Stream<String> stream =  list.stream().filter(s -> s.length() == 3)
                .filter(s -> s.startsWith("张"));



    }

}


```



### 2.2、Count：统计stream 执行结果的个数   



```java
package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 统计stream 执行结果的个数
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo03Count {

    @Test
    public void count(){
        List<String> list = new ArrayList<>();
        list.add("HeallerJean");
        list.add("zhangyujin");
        list.add("zhaochunyu");
        list.add("lintie");
        list.add("buzhidao");
        list.add("ab"); //记过

        Long count =  list.stream().filter(s->s.length()<=2).count();


    }
}


```





### 2.3、Limit：获取执行结果的前几个 ,，返回值为Stream   




```java
package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: limit 获取执行结果的前几个 ,，返回值为Stream
 * 			功能：获取长度等于2的前两个
 */
public class Demo04Limit {

    @Test
    public void limit(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");

        list.stream().filter(s->s.length()==2).limit(2).forEach(s -> {
            System.out.println(s);
        });

        /**
         林铁
         杜闯
         */
    }
}


```

### 2.4、skip： skip跳过执行结果的前几个，返回值为Stream


```java
package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: skip跳过执行结果的前几个，返回值为Stream
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo05Skip {

    @Test
    public void demo(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");

        list.stream().filter(s->s.length()==2).skip(1).forEach(s->{
            System.out.println(s);
        });

      
        /**
         杜闯
         高彤
         */
    }
}


```

### 2.5、Map： 映射操作

  

> 参数是一个Function ,返回结果   
>
> 如果希望进行映射操作，使用功能Map方法，，返回值为Stream，




```java
package com.hlj.java8.Stream;

import com.hlj.java8.Stream.Demo06MapPack.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Desc: steam映射方法Map，如果希望进行映射操作，使用功能Map方法，，返回值为Stream
 *      这个里面的参数是一个function<T,R>接口
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo06Map {

    /**
     * 功能
     *1、 将字符串10转化为数组10
     *2、然后累加1000,最后得到Integer
     */
    @Test
    public void demo(){
        List<String> list = new ArrayList<>();
        list.add("10");
        list.add("20");
        list.add("30");

        list.stream().filter(s -> s.equals("10"))
                .map(s -> Integer.parseInt(s))
                .map(s -> s + 1000)
                .forEach(s -> {
                    System.out.println(s);
                });

    }



    @Test
    public  void init() {
        List<Student> students  = Arrays.asList(
                new Student("Ice", "1234", new Date(), null, "Shannxi", "123456", "", "M", null, 170.5, 65.0),
                new Student("Leo", "2344", new Date(), null, "Liaoning", "123456", "", "M", null, 175.5, 50.0),
                new Student("Mark", "4345", new Date(), null, "Shannxi", "123456", "", "M", null, 169.5, 70.0),
                new Student("Will", "4552", new Date(), null, "Fujian", "123456", "", "M", null, 176.5, 45.0),
                new Student("Yuan", "4554", new Date(), null, "Fujian", "123456", "", "M", null, 180.5, 56.0),
                new Student("Bing", "5677", new Date(), null, "Shannxi", "123456", "", "M", null, 166.5, 67.0),
                new Student("Amy", "5675", new Date(), null, "Shannxi", "123456", "", "F", null, 156.5, 78.0),
                new Student("Lily", "7567", new Date(), null, "Shannxi", "123456", "", "F", null, 167.5, 66.0),
                new Student("Timiy", "4677", new Date(), null, "Shannxi", "123456", "", "F", null, 182.5, 68.0),
                new Student("Eline", "4697", new Date(), null, "Liaoning", "123456", "", "F", null, 188.5, 54.0),
                new Student("Chrich", "8799", new Date(), null, "Liaoning", "123456", "", "F", null, 155.5, 75.0));

        /**
         * 只取得Name
         */
        students.stream().map(student -> student.getName()).forEach(s -> {
            System.out.println(s.toString());
        });
        /**
         Ice
         Leo
         Mark
         Will
         Yuan
         Bing
         Amy
         Lily
         Timiy
         Eline
         Chrich
         */

    }


}


```

### 2.6、Concat ： Stream.concat 可以可以将两个集合合并成一个整体


```java
package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Desc:Concat  Stream.concat 可以可以将两个集合合并成一个整体
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo07Concat {

    @Test
    public void demo(){
        List<String> listOne = new ArrayList<>();
        listOne.add("高彤");
        listOne.add("张翠翠");
        listOne.add("张三丰");

        List<String> listTwo = new ArrayList<>();
        listTwo.add("赵丽颖");
        listTwo.add("张宇晋");
        listTwo.add("赵春宇");
        listTwo.add("林铁");
        listTwo.add("杜闯");

       Stream.concat(listOne.stream(),listTwo.stream() ).forEach(s->{
            System.out.println(s);
        });

        /**
         高彤
         张翠翠
         张三丰
         赵丽颖
         张宇晋
         赵春宇
         林铁
         杜闯
         */


    }
}


```

### 2.7、ForEach：如果希望对流当中的元素，进行逐一挨个处理

> 解释：参数是一个Consumer接口(方法，lambda，方法引用)


```java

package com.hlj.util.java8.Stream;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Desc: ForEach 如果希望对流当中的元素，进行逐一挨个处理，
 * 可以使用ForEach方法，参数是一个Consumer接口(方法，lambda，方法引用)
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo08ForEach {

    @Test
    public void demo(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");

        list.stream().forEach(s->{
            System.out.println(s);

            methodConsume();//非静态
             Demo08ForEach:;methodConsume();

            methodStaticConsume();//静态
            Demo08ForEach:;methodStaticConsume();

        });

        list.stream().forEach(System.out::println);
    }

    public void methodConsume(){
        System.out.println("高手在民间");
    }




    public static void methodStaticConsume(){
        System.out.println("静态高手在民间");
    }


    @Test
    public void remove(){

        LinkedList<Long> orderIds  =new LinkedList<>();
        orderIds.add(1L);
        orderIds.add(2L);
        orderIds.add(3L);

        List<Long> ids = new ArrayList<>() ;

        int i ;
        for(i=0;i<=2;i++){
                ids.add( orderIds.getFirst());
                orderIds.removeFirst();
        }

        System.out.println(ids);
        System.out.println(orderIds);

    }


    /**
     * for 循环不能终止
     *
     * 它里面有一个消费者，消费者里面结束了，但是不能结束for，使用return则是进行下一个数据进行消费
     *    default void forEach(Consumer<? super T> action) {
     *         Objects.requireNonNull(action);
     *         for (T t : this) {
     *             action.accept(t);
     *         }
     *     }
      */
    @Test
    public void continueForeach(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");


        list.forEach(s->{
            if(StringUtils.equals("a",s )){
              return; //这里的return 相当于continue没有结束循环，而是继续下一个
            }
            System.out.println(s);

        });


    }



}


```

 

### 2.8、并发流



> + 直接获取并发流 list.parallelStream()
> + list.stream().parallel().先获取普通流，然后变成并发的   



```java
package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/** 并发流的获取和使用,背后使用的是fork/join线程池
 * 如果获取并发流
 * 1、直接获取并发流 list.parallelStream()
 * 2、先获取普通流，然后变成并发的
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo11Concurrence {

    @Test
    public void demo(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");

        //打印结果随机
        list.parallelStream().forEach(s->{
            System.out.println(s);
        });

        System.out.println("----------");
        list.stream().parallel().forEach(s->{
            System.out.println(s);
        });
    }


}


```

### 2.9、collect：     



> **解释 ：当中收集集合需要用到collect方法，方法的参数是一个Collector接口     ，Collector接口通常不需要自己实现，借助工具类中的  Collectors.toList() Collectors.toSet() 即可**     



#### 2.9.1、收集成Collection



```java
package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Desc:
 * Stream 当中收集集合需要用到collect方法，方法的参数是一个Collector接口
 * Collector接口通常不需要自己实现，借助工具类中的  Collectors.toList() Collectors.toSet() 即可
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo12Collect {

    @Test
    public void demo(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");

        List<String> listResult =   list.stream().collect(Collectors.toList());

        Set<String> set =           list.stream().collect(Collectors.toSet());

    }
}


```

#### 2.9.2、收集成map


##### 2.9.2.1、返回对象中的某一个属性 `Map<String, Map<String, Double>>`



```java
//getScores Map<String, Double>
public Map<String, Map<String, Double>> toConcurrentMapTest(List<Student> students){
    return students.stream()
        .collect(Collectors.toConcurrentMap(Student::getName, Student::getScores));
}
```



##### 2.9.2.2、返回对象 `Map<Long, ScfSysRole>`  



```java
Map<Long, ScfSysRole> map = sysRoles.stream()
    .collect(Collectors.toMap(item -> item.getId(), item -> item));
```





### 2.10、distinct 去重  



```java
List<Person> result = list.stream()
                    .distinct()
                    .collect(toList());
```



### 2.11、anyMatch：   

> **解释 ： 是否匹配任一元素,allMatch:是否匹配所有元素,noneMatch：是否不匹配的所有元素**  
>
> **使用：参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,返回的是true，或者false**



```java

package com.hlj.java8.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: anyMatch 匹配任一元素 allMatch匹配所有元素 noneMatch 不匹配的所有元素
 * @Author HealerJean
 * @Date 2018/9/14  上午11:48.
 */
public class Demo14AnyMatch {

    @Test
    public void demo(){
        List<String> list = new ArrayList<>();
        list.add("赵丽颖");
        list.add("张宇晋");
        list.add("赵春宇");
        list.add("林铁");
        list.add("杜闯");
        list.add("高彤");
        list.add("张翠翠");
        list.add("张三丰");


        System.out.println(list.stream().anyMatch(s->s.startsWith("张")));
        System.out.println("-------------");
        System.out.println(list.stream().allMatch(s->s.startsWith("张")));
        System.out.println("-------------");
        System.out.println(list.stream().noneMatch(s->s.equals("张三丰")));


    }
}




```

### 2.12、findAny、findFirst ：  

> 能够从流中随便选一个元素出来，它返回一个Optional类型的元素。



### 2.13、findFirst ：返回集合的第一个对象 返回结果是Optional  



```java
Optional<Person> person = list.stream().findAny();
```






## 3、groupby 分组

### 3.1、分组计数  

#### 3.1.1、分组计数：根据对象中某一属性分组 `Map<String,Long>`    

> 根据某个属性分组计数

```java
List<Student> list1= Arrays.asList(
      new Student(1,"one","zhao"),new Student(2,"one","qian"),new Student(3,"two","sun"));
      

Map<String,Long> result1=list1.stream()
    .collect(                                    
   	 Collectors.groupingBy(Student::getGroupId,Collectors.counting()));
                
```



#### 17.1.2、分组计数：根据实体对象分组 

> 根据整个实体对象分组计数,当其为String时常使用

```java

List<Student> list1= Arrays.asList(
      new Student(1,"one","zhao"),new Student(2,"one","qian"),new Student(3,"two","sun"));


Map<Student,Long> result2=list1.stream()
     .collect(
    	Collectors.groupingBy(Function.identity(),Collectors.counting()));


```


### 3.2、按照某一个属性分组

#### 3.2.1、通过某一个属性将学生分组到List中 `Map<String, List<Student>>`  



```java

public Map<String, List<Student>> groupingByToList(List<Student> students){
   return students.stream()
           .collect(Collectors.groupingBy(Student::getFrom)); //默认是toList
        //.collect(Collectors.groupingBy(student -> student.getFrom(),toList()));
}


```

#### 3.2.2、通过某一个属性将学生分组到Set中 `Map<String, Set<Student>>`



```java
public Map<String, Set<Student>> groupingByToSet(List<Student> students){
        return students.stream()
                .collect(groupingBy(Student::getFrom, toSet()));
}
    
```



#### 3.2.3、通过某一个属性将学生分组到Set中。.`Map<String, Set<Student>>`,并返回一个TreeMap



```java
 public Map<String, Set<Student>> groupingByToTreeMap(List<Student> students){
        Objects.requireNonNull(students, "The parameter cannot be empty");
        return students.stream()
                .collect(groupingBy(Student::getFrom, TreeMap::new, toSet()));
    }
    
```



#### 3.2.4、以某一个属性分组，并返回学生姓名的集合`Collectors.mapping`。 `Map<String, List<String>>`




```java

 public Map<String, List<String>> mappingTest(List<Student> students){
        return students.stream()
                .collect(
                 groupingBy(Student::getFrom, 
                            Collectors.mapping(Student::getName, toList())));
    }
    
```





## 4、集合元素拼接



### 4.1、Collectors.joining    

 **将字符串结果用逗号隔开**  

```java
public String joining(List<Student> students) {
		return students.stream()
				.map(Student::getName)
				.collect(Collectors.joining(", "));
	}
```



### 4.2、collectingAndThen() 收集之后继续做一些处理。 

> **第一个参数注意，一定是类似于colect后面收集集合对象的参数，而不能是item那种**

```java
@Test
public void testCollectingAndThan(){
    List<String> list2 = Arrays.asList("a", "b", "c");
    // Collectors.joining(",")的结果是：a,b,c  然后再将结果 x + "d"操作, 最终返回a,b,cd
    String str= Stream.of("a", "b", "c").
        collect(Collectors.collectingAndThen(Collectors.joining(","), x -> x + "d"));

}


```



## 5、元素聚合  



### 5.1、maxBy 、minBy  



```java
    @Test
    public void demo(){

        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        Optional<Long> max = list.stream().max((o1, o2)->o1.compareTo(o2));
        System.out.println(max);
    }
    List<Integer> list = Arrays.asList(1, 2, 3); 

    
    
    /**
     *
     *  返回身高最高的学生
     */
    public Student maxByTest(List<Student> students) {
        return students.stream()
                .collect(maxBy(Comparator.comparingDouble(Student::getHeight)))
                .get();
    }

    Integer maxValue = list.stream().
        collect(Collectors.collectingAndThen(
            Collectors.maxBy((a, b) -> a - b), Optional::get));

    
    /**
     * 
     * 返回体重最轻的学生。
     */
    public Student minByTest(List<Student> students) {
        return students.stream()
                .collect(minBy(Comparator.comparingDouble(Student::getWeight)))
                .get();
        
    }

    Integer minValue = 	list.stream().
        collect(Collectors.collectingAndThen(
            Collectors.minBy((a, b) -> a - b), Optional::get));

}

```





### 5.2、`Collectors.averagingDouble`,平均值 

```java
return students.stream()
           .collect(Collectors.averagingDouble(student -> student.getHeight()));
```



### 5.3、reduce求和  



```java
int age = list.stream().reduce(0, Integer::sum);
		

Integer sumValue = list.stream().collect(Collectors.summingInt(item -> item));
```





### 5.4、mapping   

> **映射：先对集合中的元素进行映射，然后再对映射的结果使用Collectors操作**



```java
Stream.of("a", "b", "c").collect(
        Collectors.mapping(x -> x.toUpperCase(), Collectors.joining(",")));
    
// A,B,C
```





## 6、项目使用 



### 6.1、去重 

#### 6.1.1、单一属性去重

```java
List<Person> unique = persons.stream().collect(
            Collectors.collectingAndThen(
                    Collectors.toCollection(
                    () -> new TreeSet<>(Comparator.comparing(Person::getName))), 
                    ArrayList::new)
);

```



#### 6.1.2、多个属性去重 



```java
// 根据name,sex两个属性去重
List<Person> unique = persons.stream().collect(
           Collectors. collectingAndThen(
                    Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(
                            					o -> o.getName() + ";" + o.getSex()))), 
               			ArrayList::new)
);

```






## [代码下载](https://github.com/HealerJean/ProjectUtils/tree/master/src/com/hlj/java8)



​    

如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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

