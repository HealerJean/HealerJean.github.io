---
title: SpringBoot独立事物控制以及异常问题
date: 2018-06-28 03:33:00
tags: 
- Databases
category: 
- Databases
description: SpringBoot独立事物控制以及异常问题
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

再myfalse github中有spring控制的独立事物，这里是springBoot我们用下注解采用的独立事物


## 1、新开启一个事物

```
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public void keyTransactional(CouponItemGood couponItemGood) {

}
```

## 2、解释

###  REQUIRED，注意，这是默认值，也即不进行该参数配置等于配置成REQUIRED。

#### 2.1、REQUIRED的含义是，支持当前已经存在的事务，如果还没有事务，就创建一个新事务。

　　A调用B，假设调用aMethod前不存在任何事务，那么执行aMethod时会自动开启一个事务，而由aMethod调用bMethod时，由于事务已经存在，因此会使用已经存在的事务（也就是执行aMethod之前创建的那个事务）。
　　对于这样的配置，如果bMethod过程中发生异常需要回滚，那么aMethod中所进行的所有数据库操作也将同时被回滚，因为这两个方法使用了同一个事务。



#### 2.2、　REQUIRES_NEW的含义是，挂起当前事务，创建一个新事务，如果还没有事务，就简单地创建一个新事务。
　　
　　首先，REQUIRES_NEW会创建一个与原事务无关的新事务，尽管是由一个事务调用了另一个事务，但却没有父子关系。
　　如果bMethod的传播性是REQUIRES_NEW，而抛出了一个异常，则bMethod一定会被回滚，而如果aMethod捕获并处理了这个bMethod抛出的异常，那么aMethod仍有可能成功提交。当然，如果aMethod没有处理这个异常，那么aMethod也会被回滚。
　　如果aMethod在bMethod完成后出现了异常，那么bMethod已经提交而无法回滚，只有aMethod被回滚了。
　　




### 3、关于使用事物中嵌套REQUIRES_NEW 事物中出现的异常

比如使用了上面的注解出现了空指针异常，但是因为上面注解的关系异常会抛出为TransactionSystemException，正常情况下，如果REQUIRES_NEW 中出现了异常，自己的事物中可以捕获，而且在它事物外，也就是包含它的大事物中国也会进行二次捕获。也就是说会捕获两次异常





```
Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Transaction marked as rollbackOnlyException 


外面的大事物中进行捕获为TransactionSystemException
try {
    keyTransactional.keyTransactional(couponItemGood);
    log.info(couponItemGood.getId().toString()+"已经检测");
}catch (AppException e){
    log.info(e.getMessage())
    continue;
}catch (TransactionSystemException e){
    log.info("小当优惠券中没有优惠券信息导致的，无须处理-多个事物开启了异常");
}catch (Exception e){
}

```




　
　
<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'AAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

