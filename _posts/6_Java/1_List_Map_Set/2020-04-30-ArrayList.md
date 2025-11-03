---
title: ArrayList
date: 2020-04-30 03:33:00
tags: 
- Java
category: 
- Java
description: ArrayList
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`ArrayList`

> 底层的数据结构就是数组，数组元素类型为`Object`类型，即可以存放所有类型数据。    
>
> 我们对`ArrayList`类的实例的所有的操作底层都是基于数组的。下面我们来分析通过数组是如何保证库函数的正确实现的。  



## 1、类结构 

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
```



## 2、静态变量 

```java
//数组默认初始容量
private static final int DEFAULT_CAPACITY = 10;

//定义一个空的数组实例 -> 出现在需要用到空数组的地方，其中一处就是使用自定义初始容量构造方法时候如果你指定初始容量为0的时候就会返回。
private static final Object[] EMPTY_ELEMENTDATA = {};

//定义一个空数组，跟前面的区别就是这个空数组是用来判断ArrayList第一添加数据的时候要扩容多少。
//默认的构造器情况下返回这个空数组
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

//为什么减去8呢： 数组的对象头里有一个_length字段，记录数组长度，只需要去读_length字段就可以了，有些虚拟机在数组中保留了一些头信息。避免内存溢出！
// 最大数组容量 （ int 类整数的最大值是 2 的 31 次方 - 1 = 2147483648 - 1 = 2147483647）
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
```



## 3、实例变量 

```java
// 数据存的地方它的容量就是这个数组的长度，
//同时只要是使用默认构造器（DEFAULTCAPACITY_EMPTY_ELEMENTDATA ）
//第一次添加数据的时候容量扩容为DEFAULT_CAPACITY = 10
transient Object[] elementData;


// 实际元素大小，默认为0
private int size;
```



## 4、构造器

### 1）无参构造器

> 初始化一个空的数组

```java
public ArrayList() {
	this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```



### 2）指定大小的构造器

> 如果指定了大小，则直接初始化

```java
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}
```



## 5、`add(E e)`、`grow` 初始化和扩容  

1、一进来就首先确保 `elementData`数组有合适的大小，调用`ensureCapacityInternal(int minCapacity)`，`minCapacity`为数组最少需要的容量，也可以作为扩容阀值。执行2

2、**如果数组没有初始化，也就是说数组为空，比较默认数组的大小`DEFAULT_CAPACITY`(10)和这个最少需要的容量,选最大值，**这个比较个人理解认为没有意义。因为数组为空，肯定是初始化`minCapacity`肯定是1,**所以最少需要的容量就设置为`DEFAULT_CAPACITY`(10)  ，这就是初始化的数组长度**      

**3、判断是否需要扩容：****此时我们真正得到了最小需要的数组长度`minCapacity`，**调用`ensureExplicitCapacity`，`modCount++` 记录添加次数（revome的时候也会+1），**然后判断是否要扩容，如果不扩容就直接返回**      如果最小的容量是比当前数组的长度要大，就表示要扩容了 ，调用`grow(minCapacity)`  ,否则直接返回，然后数组中添加数据，**返回true，直接退出**     

**4、计算长度，开始扩容：**计算新的数组的长度`newCapacity` = 旧的数组的长度 + 旧的数组的长度的一半** **也就是等于1.5倍**， 即使这样我们也要比较下新数组长度`newCapacity`和目前数组需要的最少容量 `minCapacity`大小，比如我们初始化的时候，构造器初始化数组的长度为1，这个时候如果扩容的话，还是1。肯定不满足大小。所以要依靠我们目前真正需要的最小容量`minCapacity`来决定，此时 新的数组长度大小`newCapacity`就等于了 `minCapacity`      

**5、如果此时新的数组长度比我们和ArrayList默认的数组的最大长度（`Interger.MaxValue - 8` ）还要大，那岂不是，如果大于它，说明新的数组太大了，应该根据我们需要的最小容量`minCapacity`再选择合适新数组大小的大小**     

6、 调用`hugeCapacity`，如果`minCapacity`是负数，说明内存溢出了，抛出异常吧，还能咋办，肯定放不下的 ，  如果最小需要的数组长度 比ArrayList提供的默认值还要大，此时返回`Integer.MaxValue`，否则就返回默认的数组最大的值    

7、初始化或者扩容完毕。   

8、数组元素赋值`elementData[size++] = e;`，返回结果true



### 1）`add`

```java
public boolean add(E e) {
    
    //确保elementData数组有合适的大小,当容量不够时进行扩容，，表示需要size + 1个空间容量
    ensureCapacityInternal(size + 1);  
    
    //存储元素 
    elementData[size++] = e;
    return true;
}
```

```java
//minCapacity ：如果放入元素后，最少需要多少个容量
private void ensureCapacityInternal(int minCapacity) {
    // 判断是否是默认空数组，如果是的话，最少需要多少个容量（计算扩容阀值，）
    //如果数组元素小于等于 数组默认初始容量10 的的时候，阀值是10，否则阀值
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) { 
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity); 
    }

    ensureExplicitCapacity(minCapacity);
}
```

```java
/**
* 扩容检查
*/
private void ensureExplicitCapacity(int minCapacity) {
    // add/remove的时候会操作计算+1
    modCount++;
    
	//容量不够用了，要扩容 （同时包含初始化数组，空数组的时候elementData.length == 0，）
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```



### 2）扩容 

> 数组扩容才是按照当前容量的1.5倍进行扩容   => 新容量为 旧容量 + 旧容量的一半

```java
/**
 * 缓冲数组扩容以确保能够存储给定元素
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    //现有元素长度
    int oldCapacity = elementData.length;
    //新容量为 旧容量 + 旧容量的一半
    //如 10 + 5 = 15
    int newCapacity = oldCapacity + (oldCapacity >> 1);

    //最少需要的容量比 新计算出来的容量还要大。）我们这个时候要使用期望的最小容量作为新的容量
    //情况1：无参构造器，数组初始化，
    //情况2：指定容量为1
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    
    //如果通过上面计算出来的容量，比数组允许的最大容量还要大，那肯定不行的。所以要根据实际需要的最小容量来减少，调用hugeCapacity重新获取新的容量
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    
    // minCapacity is usually close to size, so this is a win:
    //最小扩容容量通常是接近size的，所以这是一场胜利
    //这么臭美的吗
    elementData = Arrays.copyOf(elementData, newCapacity);
}

```



```java
/**
 * 取得最大容量
 */
private static int hugeCapacity(int minCapacity) {
    //数组的长度已经超过Integer.MAX_VALUE ，再加1就会变成负数 溢出
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    //取最大容量 加入最少需要的容量比数组的最大容量还要大，Integer的最大值作为新的数组容量，否则就返回，数组最大容量
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
    MAX_ARRAY_SIZE;
}

```



## 6、`remove(int index)`

**1、判断是否索引位是否越界，数组移动元素**  

```java
public E remove(int index) {
    rangeCheck(index);

    modCount++;
    E oldValue = elementData(index);

    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work

    return oldValue;
}
```



```java
/**
* 数组小标肯定比数组元素要小
*/
private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```



```java
@Test
public  void RemoveIndexMethod() {
    List<String> stringList = new ArrayList<String>();
    stringList.add("String one");
    stringList.add("String two");
    stringList.add("String three");
    stringList.remove(0);
    System.out.println(stringList); //[String two, String three]


    List<Integer> integerList = new ArrayList<>();
    integerList.add(1);
    integerList.add(2);
    integerList.add(10000);
    integerList.remove(0);
    System.out.println(integerList); //[2, 10000]

    integerList.remove((Integer)2);
    System.out.println(integerList); //[10000]

    integerList.remove((Integer)10000);
    System.out.println(integerList); //[]

    // [String two, String three]
    // [2, 10000]
    // [10000]
    // []
}
```



## 7、`get(int index)`

> 检查是否越界，直接返回数组下标 

```java
  public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }

```



## 8、`set(int index, E e)`

> 检查是否越界，替换然后返回替换的元素即可

```java
public E set(int index, E e) {
    rangeCheck(index);
    checkForComodification();
    E oldValue = ArrayList.this.elementData(offset + index);
    ArrayList.this.elementData[offset + index] = e;
    return oldValue;
}
```





## 9、`for`的遍历问题

> `ArrayList`、`LinkedList`、`HashMap`中都有一个字段叫`modCount`。    

- **普通 `for` 循环正向遍历的问题**：
  - 当你删除索引 `i` 处的元素时，该位置之后的所有元素都会向前移动一位。
  - 此时，循环变量 `i` 会执行 `i++`，导致**跳过了原本在 `i+1` 位置的元素**。
  - 例如：`[A, B, C, D]`，当 `i=1` 时删除 "B"，列表变为 `[A, C, D]`，此时 "C" 在索引1处。但 `i++` 后 `i=2`，直接检查 "D"，跳过了 "C"。
- **增强 `for` 循环（ `foreach` ）的问题**：
  - `Java` 集合类（如 `ArrayList`, `LinkedList`）实现了 **fail-fast 机制**。
  - 当你使用迭代器（`foreach` 语法糖背后就是迭代器）遍历集合时，会记录一个 `modCount`（修改次数）。
  - 如果在遍历过程中，直接调用集合的 `add()` 或 `remove()` 方法修改了集合，`modCount` 会改变。
  - 迭代器在下一次调用 `next()` 时会检查 `expectedModCount` 是否等于当前的 `modCount`，如果不等，就抛出 `ConcurrentModificationException`。



### 1）`modCount`用途

> 该字段被`Iterator`以及`ListIterator`的实现类所使用，如果该值被意外更改，`Iterator`或者`ListIterator` 将抛出`ConcurrentModificationException`异常    

- `modCount`：**在 `ArrayList`中有个成员变量 `modCount`，继承于`AbstractArrAayList`，每对 `List`对象修改一次，也就每次`add`或者`remove`它的值都会加1.**          

- `expectedModCount`：**`Itr`类里有一个成员变量`expectedModCount`，它的值为创建 `Iterator` 对象的时候`List`的`modCount`值。**         

  - 用此 **`expectedModCount `**变量来检验在迭代过程中`List`对象是否被修改了，如果被修改了则抛出 `java.util.ConcurrentModificationException `异常。       

  - 在每次调用`Itr `对象的 `next`() 或者 `remove `方法的时候都会调用 `checkForComodification`() 方法进行一次检验，     


  - `checkForComodification`() 方法中做的工作就是比较 `expectedModCount`  和 `modCount` 的值是否相等，**如果不相等， 就认为还有其他对象正在对当前的List进行操作，那个就会抛出`ConcurrentModificationException`异常**

    

    

### 2）错误举例

#### a、使用普通for循环正向遍历并删除元素

```java
/**
 * 问题：删除元素后，后续元素索引前移，但循环变量i递增，导致跳过下一个元素。
 */
public static void wrongWay_ForwardLoop() {
    System.out.println("=== 错误方式1：普通for循环正向遍历删除 ===");
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));


    for (int i = 0; i < list.size(); i++) {
        String item = list.get(i);
        System.out.println("检查元素: " + item + " (索引: " + i + ")");
        if ("B".equals(item)) {
            System.out.println("删除元素: " + item);
            list.remove(i); // 删除后，"C" 移动到索引1，但i会++，跳过"C"
            System.out.println("删除后列表: " + list);
        }
    }
    System.out.println("最终结果: " + list);
    System.out.println("注意：如果要删除'C'，它可能会被跳过！\n");
}


=== 错误方式1：普通for循环正向遍历删除 ===
检查元素: A (索引: 0)
检查元素: B (索引: 1)
删除元素: B
删除后列表: [A, C, D]
检查元素: D (索引: 2)
最终结果: [A, C, D]
注意：如果要删除'C'，它可能会被跳过！
```

#### b、增强 `for` 循环（foreach）删除

```java
/**
 * 问题：触发fail-fast机制，抛出ConcurrentModificationException。
 */
public static void wrongWay_ForeachLoop() {
    System.out.println("=== 错误方式2：增强for循环（foreach）删除 ===");
    List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
    System.out.println("原始列表: " + list);

    try {
        for (String item : list) {
            System.out.println("检查元素: " + item);
            if ("B".equals(item)) {
                System.out.println("尝试删除元素: " + item);
                list.remove(item); // ⚠️ 直接修改集合，破坏迭代器状态
            }
        }
    } catch (ConcurrentModificationException e) {
        System.out.println("❌ 捕获到异常: " + e.getClass().getSimpleName());
        System.out.println("   原因：增强for循环使用迭代器，直接修改集合会触发fail-fast机制。\n");
    }
}

=== 错误方式2：增强for循环（foreach）删除 ===
原始列表: [A, B, C, D]
检查元素: A
检查元素: B
尝试删除元素: B
❌ 捕获到异常: ConcurrentModificationException
   原因：增强for循环使用迭代器，直接修改集合会触发fail-fast机制。
```



### 3）正例

| 方法                                 | 是否推荐               | 说明                                   |
| :----------------------------------- | :--------------------- | :------------------------------------- |
| 增强 `for` 循环 + `list.remove()`    | 绝对避免               | 抛出 `ConcurrentModificationException` |
| 普通 `for` 循环正向遍历 + `remove()` | 不推荐                 | 会跳过元素，逻辑错误                   |
| **`Iterator` + `iterator.remove()`** | **强烈推荐**           | 最安全、最标准的方式                   |
| **普通for循环反向遍历**              | 推荐                   | 简单有效，适用于简单删除               |
| **`removeIf()`**                     | **强烈推荐 (Java 8+)** | 简洁、高效、函数式                     |
| 收集后 `removeAll()`                 | 推荐                   | 适合删除多个不连续元素                 |

#### a、使用 `Iterator` 的 `remove()` 方法

> 这是最推荐、最安全的方式。`Iterator` 提供了 `remove()` 方法，它知道如何安全地删除当前元素。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String item = iterator.next();
    if ("B".equals(item)) {
        iterator.remove(); 
    }
}
System.out.println(list); // 输出: [A, C, D]
```

#### b、普通 `for` 循环反向遍历

> 从列表末尾开始向前遍历。这样即使删除了某个元素，也不会影响前面还未遍历的元素的索引。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
for (int i = list.size() - 1; i >= 0; i--) {
    if ("B".equals(list.get(i))) {
        list.remove(i); 
    }
}
System.out.println(list); // 输出: [A, C, D]
```

#### c、使用 `removeIf()` 方法（`Java 8+`）

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
list.removeIf(item -> "B".equals(item)); 
System.out.println(list); // 输出: [A, C, D]
```



#### d、收集要删除的元素，最后统一删除

> 先遍历收集，再批量删除。适用于删除多个不同元素的场景。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
Set<String> toRemove = new HashSet<>();
for (String item : list) {
    if ("B".equals(item) || "C".equals(item)) {
        toRemove.add(item);
    }
}
list.removeAll(toRemove); 
System.out.println(list); // 输出: [A, D]
```







## 1.9、`remove(Object o)`

> 移除对象，只移除第一个重复的对象

```java
public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}


private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work
}
```



```java
@Test
	public void RemoveObjectMethod() {
		List<String> stringList = new ArrayList<String>();
		stringList.add("String one");
		stringList.add("String one");
		stringList.add("String two");

		stringList.remove("String one"); //只能移除第一个重复元素
		System.out.println(stringList.size()); //2
		System.out.println(stringList); //[String one, String two]

		// 2
		// [String one, String two]
	}
```



![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





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
		id: 'nIscrdzEH6QBJwXb',
    });
    gitalk.render('gitalk-container');
</script> 
