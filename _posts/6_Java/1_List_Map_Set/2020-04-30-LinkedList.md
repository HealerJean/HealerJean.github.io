---
title: LinkedList
date: 2020-04-30 03:33:00
tags: 
- Java
category: 
- Java
description: LinkedList
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、LinkedList

> `LinkedList`内部是一个链表的实现，一个节点除了保持自身的数据外，还持有前，后两个节点的引用。    
>
> 所以就数据存储上来说，它相比使用数组作为底层数据结构的`ArrayList`来说，会更加耗费空间。但也正因为这个特性，它删除，插入节点很快！



![1588739630747](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1588739630747.png)





## 1.1、类结构 

![1588739811250](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1588739811250.png)



```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{



```





### 1.1.1、`Node<E>`

```java
private static class Node<E> {
    //item用于保存数据
    E item;
    //next用于指向当前节点的下一个节点
    Node<E> next;
    //prve用于指向当前节点的前一个节点
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}

```



## 1.2、构造器

### 1.2.1、无参构造器 

```java
public LinkedList() {
}
```





## 1.3、实例变量 

```java
// 标识这个链表的长度，也就是元素的个数
transient int size = 0;

//指向链表第一个节点，头结点
transient Node<E> first;

//指向链表的最后一个节点，尾节点
transient Node<E> last;
```





## 1.4、`add(E e) `

> 默认从链表的末尾添加数据      
>
> 如果当前链表为空，也就是`last`为空，则j新节点赋值给`last`节点，并且将新节点赋值给`first`节点。    如果`last`不为空，则`last`的`next`指向新的节点





```java
public boolean add(E e) {
    linkLast(e);
    return true;
}
```



```java
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```



## 1.5、get 

1、检查元素索引是否越界        

2、链表查询  ，类似于2分法，距离链表头近，则从链表头`first节点`开始找，如果距离链表尾部近，则从链表尾部`last节点`开始找       



```java
public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
}
```



### 1.5.1、检查元素索引是否越界

```java

private void checkElementIndex(int index) {
    if (!isElementIndex(index))
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

private boolean isElementIndex(int index) {
    return index >= 0 && index < size;
}
```

### 1.5.2、遍历链表，定位元素

> 类似于2分法，距离链表头近，则从链表头`first节点`开始找，如果距离链表尾部近，则从链表尾部`last节点`开始找   

```java
Node<E> node(int index) {
    // assert isElementIndex(index);

    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```



## 1.6、`remove(int index)`

1、`checkElementIndex(index)`检查元素索引是否越界    

2、`node(index)`，定位元素   

3、删除元素，涉及到链表的`next`和`prev节点`的变化，很简单，距离看代码

```java
public E remove(int index) {
    checkElementIndex(index);
    return unlink(node(index));
}
```



```java
/**
     * Unlinks non-null node x.
     */
E unlink(Node<E> x) {
    // assert x != null;
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}
```





## 1.7、`set(int index, E element)`

1、检查元素索引是否越界   

2、查找索引位元素    

3、将元素的实例变量`item`替换为`element`

```java
public E set(int index, E element) {
    checkElementIndex(index);
    Node<E> x = node(index);
    E oldVal = x.item;
    x.item = element;
    return oldVal;
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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 
