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



# 1、ArrayList

> 底层的数据结构就是数组，数组元素类型为Object类型，即可以存放所有类型数据。    
>
> 我们对ArrayList类的实例的所有的操作底层都是基于数组的。下面我们来分析通过数组是如何保证库函数的正确实现的。  



## 1.1、类结构 

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
```



## 1.2、静态变量 



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



## 1.3、实例变量 

```java
// 数据存的地方它的容量就是这个数组的长度，
//同时只要是使用默认构造器（DEFAULTCAPACITY_EMPTY_ELEMENTDATA ）
//第一次添加数据的时候容量扩容为DEFAULT_CAPACITY = 10
transient Object[] elementData;


// 实际元素大小，默认为0
private int size;

```



## 1.3、构造器

### 1.3.1、无参构造器

> 初始化一个空的数组

```java
public ArrayList() {
	this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```



### 1.3.2、指定大小的构造器

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



## 1.4、`add(E e)`、grow初始化和扩容  

1、一进来就首先确保elementData数组有合适的大小，调用`ensureCapacityInternal(int minCapacity)`，`minCapacity`为数组最少需要的容量，也可以作为扩容阀值。执行2

2、**如果数组没有初始化，也就是说数组为空，比较默认数组的大小`DEFAULT_CAPACITY`(10)和这个最少需要的容量,选最大值，**这个比较个人理解认为没有意义。因为数组为空，肯定是初始化`minCapacity`肯定是1,**所以最少需要的容量就设置为`DEFAULT_CAPACITY`(10)  ，这就是初始化的数组长度**      

**3、判断是否需要扩容：****此时我们真正得到了最小需要的数组长度`minCapacity`，**调用`ensureExplicitCapacity`，`modCount++` 记录添加次数（revome的时候也会+1），**然后判断是否要扩容，如果不扩容就直接返回**      如果最小的容量是比当前数组的长度要大，就表示要扩容了 ，调用`grow(minCapacity)`  ,否则直接返回，然后数组中添加数据，**返回true，直接退出**     

**4、计算长度，开始扩容：**计算新的数组的长度`newCapacity` = 旧的数组的长度 + 旧的数组的长度的一半** **也就是等于1.5倍**， 即使这样我们也要比较下新数组长度`newCapacity`和目前数组需要的最少容量 `minCapacity`大小，比如我们初始化的时候，构造器初始化数组的长度为1，这个时候如果扩容的话，还是1。肯定不满足大小。所以要依靠我们目前真正需要的最小容量`minCapacity`来决定，此时 新的数组长度大小`newCapacity`就等于了 `minCapacity`      

**5、如果此时新的数组长度比我们和ArrayList默认的数组的最大长度（`Interger.MaxValue - 8` ）还要大，那岂不是，如果大于它，说明新的数组太大了，应该根据我们需要的最小容量`minCapacity`再选择合适新数组大小的大小**     

6、 调用`hugeCapacity`，如果`minCapacity`是负数，说明内存溢出了，抛出异常吧，还能咋办，肯定放不下的 ，  如果最小需要的数组长度 比ArrayList提供的默认值还要大，此时返回`Integer.MaxValue`，否则就返回默认的数组最大的值    

7、初始化或者扩容完毕。   

8、数组元素赋值`elementData[size++] = e;`，返回结果true



### 1.4.1、add  

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



### 1.4.2、扩容 

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

    //最少需要的容量比 新计算出来的容量还要打。）我们这个时候要使用期望的最小容量作为新的容量
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



## 1.5、`remove(int index)`

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

## 1.6、`get(int index)`

> 检查是否越界，直接返回数组下标 

```java
  public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }

```



## 1.7、`set(int index, E e)`

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