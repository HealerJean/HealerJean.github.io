---
title: SpringBoot如何解决循环依赖问题
date: 2020-06-22 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot如何解决循环依赖问题
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、Spring中循环依赖场景有：   

（1）构造器的循环依赖    （无法解决）

（2）field属性的循环依赖。



```java
@Slf4j
@Service
public class AService {


    public AService(BService bService){
        log.info(bService.toString());
    }
}
```



```java
@Service
@Slf4j
public class BService {

    public BService(AService aService){
      log.info(aService.toString());
    }
}
```





# 2、怎么检测是否存在循环依赖



检测循环依赖相对比较容易，Bean在创建的时候可以给该Bean打标，如果递归调用回来发现正在创建中的话，即说明了循环依赖了。





或者控制台报错





```
Description:

The dependencies of some of the beans in the application context form a cycle:

┌─────┐
|  AService defined in file [/Users/healerjean/Desktop/Now/HealerJean.github.io/_posts/7_SpringBoot/2020_06_04_SpringBoot原理/hlj-spring-boot/spring-annotation/target/classes/com/healerjean/proj/service/AService.class]
↑     ↓
|  BService defined in file [/Users/healerjean/Desktop/Now/HealerJean.github.io/_posts/7_SpringBoot/2020_06_04_SpringBoot原理/hlj-spring-boot/spring-annotation/target/classes/com/healerjean/proj/service/BService.class]
└─────┘

```







# 3、Spring如何解决循环依赖

> Spring的循环依赖的理论依据其实是基于Java的引用传递，当我们获取到对象的引用时，对象的field或则属性是可以延后设置的(但是构造器必须是在获取引用之前)。





Spring的单例对象的初始化主要分为三步：  

1、createBeanInstance：实例化，其实也就是调用对象的构造方法实例化对象   

2、populateBean：填充属性，这一步主要是多bean的依赖属性进行填充   

3、initializeBean：调用spring xml中的init 方法。



从上面讲述的单例bean初始化步骤我们可以知道，循环依赖主要发生在第一、第二部。也就是构造器循环依赖和field循环依赖。   

那么我们要解决循环引用也应该从初始化过程着手，对于单例来说，在Spring容器整个生命周期内，有且只有一个对象，所以很容易想到这个对象应该存在Cache中，Spring为了解决单例的循环依赖问题，使用了**三级缓存**。   

```java
/** Cache of singleton objects: bean name --> bean instance */

singletonFactories ： 单例对象工厂的cache
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

/** Cache of singleton factories: bean name --> ObjectFactory */
earlySingletonObjects ：提前暴光的单例对象的Cache
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);

/** Cache of early singleton objects: bean name --> bean instance */
singletonObjects：单例对象的cache
private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

```



`Object sharedInstance = getSingleton(beanName);`    

如注释所说，首先回去找在容器中是不是已经存在该单例。具体在哪找我们在前面的文章中已经说得很清楚了。看一下getSingleton()方法   



分析`getSingleton()`的整个过程，   

1、Spring首先从一级缓存`singletonObjects`中获取。   

2、如果获取不到 并且对象正在创建中，就再从二级缓存`earlySingletonObjects`中获取。       

3、如果还是获取不到且允许`singletonFactories`通过`getObject()`获取，就从三级缓存`singletonFactory.getObject()`(三级缓存)获取，如果获取到了则：从`singletonFactories`中移除，并放入`earlySingletonObjects`中。其实也就是从三级缓存移动到了二级缓存。



```java
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);


// DefaultSingletonBeanRegistry类
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
  // 由于scope是singleton，所以先从缓存中取单例对象的实例，如果取到直接返回，没有取到加载bean
  Object singletonObject = this.singletonObjects.get(beanName);

  // 当想要获取的bean没有被加载，并且也没有正在被创建的时候，主动去加载bean
  //isSingletonCurrentlyInCreation()判断当前单例bean是否正在创建中，也就是没有初始化完成(比如A的构造器依赖了B对象所以得先去创建B对象， 或则在A的populateBean过程中依赖了B对象，得先去创建B对象，这时的A就是处于创建中的状态。
  if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
    // 锁住单例缓存区加载bean
    synchronized (this.singletonObjects) {
      // singletonObjects ，earlySingletonObjects ，singletonFactories是一个单例实例的三种存在状态
      // 再去earlySingletonObjects中去找
      singletonObject = this.earlySingletonObjects.get(beanName);
      //allowEarlyReference 是否允许从singletonFactories中通过getObject拿到对象
      if (singletonObject == null && allowEarlyReference) {
        // 去singletonFactories中去找对象的实例
        ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
        if (singletonFactory != null) {
          singletonObject = singletonFactory.getObject();
          this.earlySingletonObjects.put(beanName, singletonObject);
          this.singletonFactories.remove(beanName);
        }
      }
    }
  }
  return singletonObject;
}
```







从上面三级缓存的分析，我们可以知道，Spring解决循环依赖的诀窍就在于`singletonFactories`这个三级cache。这个cache的类型是ObjectFactory，定义如下：   

　在`DefaultSingletonBeanRegistry`类中的`singletonObjects`属性就是存singleton bean的地方。    

```java
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}



protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
    Assert.notNull(singletonFactory, "Singleton factory must not be null");
    synchronized (this.singletonObjects) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }
}
```



这里就是解决循环依赖的关键，这段代码发生在`createBeanInstance`之后，也就是说单例对象此时已经被创建出来(调用了构造器)。这个对象已经被生产出来了，虽然还不完美（还没有进行初始化的第二步和第三步），但是已经能被人认出来了（根据对象引用能定位到堆中的对象），所以Spring此时将这个对象提前曝光出来让大家认识，让大家使用。



这样做有什么好处呢？让我们来分析一下“A的某个field或者setter依赖了B的实例对象，同时B的某个field或者setter依赖了A的实例对象”这种循环依赖的情况。A首先完成了初始化的第一步，并且将自己提前曝光到singletonFactories中，此时进行初始化的第二步，发现自己依赖对象B，此时就尝试去get(B)，发现B还没有被create，所以走create流程，B在初始化第一步的时候发现自己依赖了对象A，于是尝试get(A)，尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全)，尝试二级缓存earlySingletonObjects（也没有），尝试三级缓存singletonFactories，由于A通过ObjectFactory将自己提前曝光了，所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全，但是总比没有好呀)，B拿到A对象后顺利完成了初始化阶段1、2、3，完全初始化之后将自己放入到一级缓存singletonObjects中。此时返回A中，A此时能拿到B的对象顺利完成自己的初始化阶段2、3，**最终A也完成了初始化，进去了一级缓存singletonObjects中，而且更加幸运的是，由于B拿到了A的对象引用，所以B现在hold住的A对象完成了初始化。**



知道了这个原理时候，肯定就知道为啥Spring不能解决“A的构造方法中依赖了B的实例对象，同时B的构造方法中依赖了A的实例对象”这类问题了！因为加入`singletonFactories`三级缓存的前提是执行了构造器，所以构造器的循环依赖没法解决。













































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
