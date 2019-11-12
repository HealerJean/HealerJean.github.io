---
title: Type和class以及比较是相等关系
date: 2019-01-22 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Type和class以及比较是相等关系
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

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




<br/><br/><br/>
<font color="red"> 感兴趣的，欢迎添加博主微信， </font><br/>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |




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

