---
title: Type和class以及比较是相等关系
date: 2019-01-22 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Type和class以及比较是相等关系
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             



Type是Class的父接口。   

Class是Type的子类。

## instanceof

instanceof操作符用于判断一个引用类型所引用的对象是否是一个类的实例。


```java
  public  void instanceOf(Object object) {

        /**
         *  ==
         */
        if(Long.class == object.getClass()){
            System.out.println("Long.class == object.getClass()");
        }

        /**
         * equals
         */
        if(Long.class.equals(object.getClass()) ){
            System.out.println("Long.class.equals(object.getClass()) ");
        }

        /**
         * instance Of
         */
        if(object instanceof  Long){
            System.out.println("object instanceof  Long");
        }
    } 
```

## == 和 equals比较的实际的Class类，没有考虑继承，instanceof 和 isInstance考虑继承




```java

 
class  Father{}
class  Son extends  Father{}


public void fatherAndSon(){
    Father son = new Son(); //和 Son son = new Son(); 一样
    Father father = new Father();

    //son.getClass() ==  father.getClass() :false
    System.out.println("son.getClass() ==  father.getClass() :" +(son.getClass() == father.getClass()) );

    //son.getClass().equals(father.getClass()) :false
    System.out.println("son.getClass().equals(father.getClass()) :" +(son.getClass().equals(father.getClass())) );

    //son  instanceof Father :true
    System.out.println("son  instanceof Father :" +(son  instanceof Father ));

    //father  instanceof Son :false   //父类不能  instanceof  子类
    System.out.println("father  instanceof Son :" +(father  instanceof Son ));

    //father  instanceof Father :true
    System.out.println("father  instanceof Father :" +(father  instanceof Father ));

    //son.getClass().isInstance(father):false  //子类不能 isInstance 父亲
  System.out.println("son.getClass().isInstance(father):"+son.getClass().isInstance(father));

    //father.getClass().isInstance(son):true
  System.out.println("father.getClass().isInstance(son):"+father.getClass().isInstance(son));

    //son.getClass().isInstance(son)true
  System.out.println("son.getClass().isInstance(son)"+son.getClass().isInstance(son));

}

@Test
public void testFatherAndSon(){
    fatherAndSon();
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
		id: 'yigs24wC3Y1Jud9K',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

