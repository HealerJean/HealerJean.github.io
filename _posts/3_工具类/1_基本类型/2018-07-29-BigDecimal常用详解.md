---
title: BigDecimal常用详解
date: 2018-07-29 03:33:00
tags: 
- Utils
category: 
- Utils
description: BigDecimal常用详解
---


**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)    



## 1、基本介绍 



**1、BigDecimal("3.14") 一定不要传入浮点型，这样会有精度损失，计算结果会出现意想不到的结果**        

**2、 BigDecimal除法，必须在divide方法中传递第二个参数，定义精确到小数点后几位，否则在结果是无限循环小数时，就会抛出以上异常。具体看后面的解释 **

```java
 java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
```





## 2、运算 

### 2.1、`add`加法



```java
BigDecimal a = new BigDecimal(10) ;
BigDecimal b = new BigDecimal(5) ;
BigDecimal result = a.add(b); // 15 
```



### 2.2、`subtrac`t减法

```java
BigDecimal a = new BigDecimal(10) ;
BigDecimal b = new BigDecimal(5) ;
BigDecimal result = a.subtract(b); //5
```



### 2.3、`multiply`乘法

```java
BigDecimal a = new BigDecimal(10) ;
BigDecimal b = new BigDecimal(5) ;
BigDecimal result = a.multiply(b); //50
```



### 2.4、`divide`除法    

#### 2.4.1、正常整除     

- **如果确定结果是正常整除的，那么不设置舍入模式`RoundingMode`也是可以的**   




```
BigDecimal a = new BigDecimal(10) ;
BigDecimal b = new BigDecimal(5) ;
BigDecimal result = a.divide(b); //2
```



#### 2.4.2、整除结果为无线不循环小数       

- **如果结果是无线不循环小数，不整除的情况下，结果是无限循环小数时，就会抛出异常。**



```java
BigDecimal a = new BigDecimal(1) ;
BigDecimal b = new BigDecimal(3) ;
BigDecimal result = a.divide(b); //抛出异常  

java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
```



#### 2.4.3、除法使用说明  

**为了避免出现上述情况，一般的建议是divide 方法中根据自身需要配置相应的舍入模式** 



```java
//解决方案     
BigDecimal a = new BigDecimal(1) ;
BigDecimal b = new BigDecimal(3) ;
BigDecimal result = a.divide(b, BigDecimal.ROUND_HALF_UP); //四舍五入
System.out.println(result); //0 不传scale ，scale默认是0 

BigDecimal result = a.divide(b, 2,  BigDecimal.ROUND_HALF_UP); //四舍五入
System.out.println(result); //0.33  
    
```



## 2、输出对应格式的数字   

### 2.1、0 

```java
比实际数字的位数多，不足的地方用0补上。
    new DecimalFormat("00.00").format(3.14)  //结果：03.14
    new DecimalFormat("0.000").format(3.14)  //结果： 3.140
    new DecimalFormat("00.000").format(3.14)  //结果：03.140
    比实际数字的位数少：整数部分不改动，小数部分，四舍五入
    new DecimalFormat("0.000").format(13.146)  //结果：13.146
    new DecimalFormat("00.00").format(13.146)  //结果：13.15
    new DecimalFormat("0.00").format(13.146)  //结果：13.15
```



### 2.2、# 

```java
比实际数字的位数多，不变。
    new DecimalFormat("##.##").format(3.14)  //结果：3.14
    new DecimalFormat("#.###").format(3.14)  //结果： 3.14
    new DecimalFormat("##.###").format(3.14)  //结果：3.14
比实际数字的位数少：整数部分不改动，小数部分，四舍五入
    new DecimalFormat("#.###").format(13.146)  //结果：13.146
    new DecimalFormat("##.##").format(13.146)  //结果：13.15
    new DecimalFormat("#.##").format(13.146)  //结果：13.15
```



### 2.3、# 、0 



```java
  
DecimalFormat FORMAT = new DecimalFormat("#,##0.00");
new DecimalFormat("#.##").format(71015000009826 )  

71,015,000,009,826.00
        
```



## 3、舍入模式  

### 3.1、UP   （个人理解：舍弃位前面 + 1）

> 解释：始终对非零舍弃部分前面的数字加 1。     
>
> 注意，此舍入模式始终不会减少计算值的绝对值。




![1574419571504](D:\study\HealerJean.github.io\blogImages\1574419571504.png)



   

```java
/**
* UP （个人理解：舍弃位前面 + 1）
* 解释：始终对非零舍弃部分前面的数字加 1。
* 注意，此舍入模式始终不会减少计算值的绝对值。
*/
@Test
public void UP() {
    BigDecimal result = null ;
    result = new BigDecimal("11.115").setScale(2, RoundingMode.UP) ;//11.12
    result = new BigDecimal("11.114").setScale(2, RoundingMode.UP); //11.12

    result = new BigDecimal("-11.115").setScale(2, RoundingMode.UP); //-11.12
    result = new BigDecimal("-11.114").setScale(2, RoundingMode.UP); //-11.12
    System.out.println(result);
}
```

​      

### 3.2、DOWN (个人理解 ：舍弃位直接删除)  



> 解释 ：向零方向舍入 （即截尾）。          
>
> 注意，此舍入模式始终不会增加计算值的绝对值。  



![1574421575122](D:\study\HealerJean.github.io\blogImages\1574421575122.png)



```java
/**
* DOWN  (个人理解 ：舍弃位直接删除)
* 解释 ：向零方向舍入 （即截尾）。
* 注意，此舍入模式始终不会增加计算值的绝对值。
*/
@Test
public void DOWN() {
    BigDecimal result = null ;
    result =new BigDecimal("11.115").setScale(2, RoundingMode.DOWN); //11.11
    result =new BigDecimal("11.114").setScale(2, RoundingMode.DOWN); //11.11

    result =new BigDecimal("-11.115").setScale(2, RoundingMode.DOWN); //-11.11
    result =new BigDecimal("-11.114").setScale(2, RoundingMode.DOWN); //-11.11
    System.out.println(result);
}
```



### 3.3、CEILING   (正数 UP，负数 DOWM ) 



> 定义：向正无限大方向舍入。       
>
> 解释：如果结果为正，则舍入行为类似于  RoundingMode.UP          
>
> ​          如果结果为负， 则舍入行为类似于  RoundingMode.DOWN      
>
> 注意，此舍入模式始终不会减少计算值。   



![1574421590915](D:\study\HealerJean.github.io\blogImages\1574421590915.png)



```java
/**
* CEILING  (正数 UP、负数 DOWM )
* 定义：向正无限大方向舍入。
* 解释：如果结果为正，则舍入行为类似于 RoundingMode.UP；
	   如果结果为负，则舍入行为类似于 RoundingMode.DOWN。
* 注意，此舍入模式始终不会减少计算值。
*/
@Test
public void CEILING() {
    BigDecimal result = null ;
    result =(new BigDecimal("11.115").setScale(2, RoundingMode.CEILING)); //11.12
    result =(new BigDecimal("11.114").setScale(2, RoundingMode.CEILING)); //11.12

    result =(new BigDecimal("-11.115").setScale(2, RoundingMode.CEILING)); //-11.11
    result =(new BigDecimal("-11.114").setScale(2, RoundingMode.CEILING)); //-11.11
    System.out.println(result);
}
```



### 3.4、FLOOR （正数 DOWM，负数 UP）  




> 定义：向负无限大方向舍入。      
>
> 解释 ：如果结果为正，则舍入行为类似于 RoundingMode.DOWN；   
>
> ​            如果结果为负，则舍入行为类似于RoundingMode.UP     
>
> 注意，此舍入模式始终不会增加计算值。  



![1574421618590](D:\study\HealerJean.github.io\blogImages\1574421618590.png)   




```java
/**
* FLOOR （正数 DOWM，负数 UP）
* 定义：向负无限大方向舍入。
* 解释 ：如果结果为正，则舍入行为类似于 RoundingMode.DOWN；
		如果结果为负，则舍入行为类似于RoundingMode.UP
* 注意，此舍入模式始终不会增加计算值。
*/
@Test
public void FLOOR() {
    BigDecimal result = null ;
    result =(new BigDecimal("11.115").setScale(2, RoundingMode.FLOOR)); //11.11
    result =(new BigDecimal("11.114").setScale(2, RoundingMode.FLOOR)); //11.11

    result =(new BigDecimal("-11.115").setScale(2, RoundingMode.FLOOR)); //-11.12
    result =(new BigDecimal("-11.114").setScale(2, RoundingMode.FLOOR)); //-11.12
    System.out.println(result);
}
```



### 3.5、HALF_UP （四舍五入，负数先取绝对值再五舍六入，再负数）



> 定义：向最接近的数字方向舍入，如果与两个相邻数字的距离相等，则向上舍入。   
>
> 解释 ：如果被舍弃部分 >= 0.5，则舍入行为同 RoundingMode.UP；   
>
> ​                                                       否则舍入行为同RoundingMode.DOWN。    



```java
/**
* HALF_UP （四舍五入，负数先取绝对值再五舍六入再负数）
* 定义：向最接近的数字方向舍入，如果与两个相邻数字的距离相等，则向上舍入。。
* 解释 ：如果被舍弃部分 >= 0.5，则舍入行为同 RoundingMode.UP；
							否则舍入行为同RoundingMode.DOWN。
*/
@Test
public void HALF_UP() {
    BigDecimal result = null ;
    result =(new BigDecimal("11.115").setScale(2, RoundingMode.HALF_UP)); //11.12
    result =(new BigDecimal("11.114").setScale(2, RoundingMode.HALF_UP)); //11.11

    result =(new BigDecimal("-11.115").setScale(2, RoundingMode.HALF_UP)); //-11.12
    result =(new BigDecimal("-11.114").setScale(2, RoundingMode.HALF_UP)); //-11.11
    System.out.println(result);
}
```





### 3.6 、HALF_DOWN （五舍六入，负数先取绝对值再五舍六入，再负数）    



> 定义：向最接近的数字方向舍入，如果与两个相邻数字的距离相等，则向下舍入。   
>
> 解释 ：如果被舍弃部分 > 0.5，则舍入行为同 RoundingMode.UP；   
>
> ​                                                    否则舍入行为同RoundingMode.DOWN。   
>
> 注意，此舍入模式就是通常讲的五舍六入。

```java
/**
* HALF_DOWN （五舍六入，负数先取绝对值再五舍六入再负数）
* 定义：向最接近的数字方向舍入，如果与两个相邻数字的距离相等，则向下舍入。
* 解释 ：如果被舍弃部分 > 0.5， 则舍入行为同 RoundingMode.UP；
							否则舍入行为同RoundingMode.DOWN。
* 注意，此舍入模式就是通常讲的五舍六入。
*/
@Test
public void HALF_DOWN() {
    BigDecimal result = null ;
    result =(new BigDecimal("11.116").setScale(2, RoundingMode.HALF_DOWN)); //11.12
    result =(new BigDecimal("11.115").setScale(2, RoundingMode.HALF_DOWN)); //11.11
    result =(new BigDecimal("11.114").setScale(2, RoundingMode.HALF_DOWN)); //11.11

    result =(new BigDecimal("-11.116").setScale(2, RoundingMode.HALF_DOWN)); //-11.12
    result =(new BigDecimal("-11.115").setScale(2, RoundingMode.HALF_DOWN)); //-11.11
    result =(new BigDecimal("-11.114").setScale(2, RoundingMode.HALF_DOWN)); //-11.11
    System.out.println(result);
}
```

### 3.7、HALF_EVEN  （银行家舍入法）    



> 1. 舍弃位的数字小于5时，该数字舍去；  
> 2. 舍弃位的数字大于5时，则进位；  
> 3. 舍弃位的数字等于5时，   
>    1. 若5的后面还有不为“0”的任何数，则此时无论5的前面是奇数还是偶数，均应进位。    
>    2.  如果5后面为全部为“0”，要看5前面的数字，若是奇数则进位，若是偶数则将5舍掉；


```java
/**
* HALF_EVEN
*  1. 舍弃位的数字小于5时，该数字舍去；
*  2. 舍弃位的数字大于5时，则进位；
*  3. 舍弃位的数字等于5时，若5的后面还有不为“0”的任何数，则此时无论5的前面是奇数还是偶数，均应进位。
*                     如果5后面为全部为“0”，要看5前面的数字，若是奇数则进位，若是偶数则将5舍掉；
 */
@Test
public void HALF_EVEN() {
    BigDecimal result = null ;
    //舍弃位的数字小于5时，该数字舍去；
    result =(new BigDecimal("1.114").setScale(2, RoundingMode.HALF_EVEN));// 1.11
    //舍弃位的数字大于5时，则进位；
    result =(new BigDecimal("1.116").setScale(2, RoundingMode.HALF_EVEN)); //1.12
    //若5的后面还有不为“0”的任何数，则此时无论5的前面是奇数还是偶数，均应进位。
    result =(new BigDecimal("1.1151").setScale(2, RoundingMode.HALF_EVEN)); //1.12
    result =(new BigDecimal("1.1251").setScale(2, RoundingMode.HALF_EVEN)); //1.13
    // 如果5后面为全部为“0”，要看5前面的数字，若是奇数则进位
    result =(new BigDecimal("1.1150").setScale(2, RoundingMode.HALF_EVEN)); // 1.12
    // 如果5后面为全部为“0” 要看5前面的数字,若是偶数则将5舍掉；
    result =(new BigDecimal("1.1250").setScale(2, RoundingMode.HALF_EVEN)); //1.12
}
```



### 3.8、UNNECESSARY (用于断言请求的操作具有精确结果，不发生舍入) 




> 定义：用于断言请求的操作具有精确结果，因此不发生舍入。   
>
> 解释：计算结果是精确的，不需要舍入，否则抛出 ArithmeticException。   


```java
/**
* UNNECESSARY
* 定义：用于断言请求的操作具有精确结果，因此不发生舍入。
* 解释：计算结果是精确的，不需要舍入，否则抛出 ArithmeticException。
*/
@Test
public void UNNECESSARY (){
    BigDecimal result = null ;
    result =(new BigDecimal("1.11").setScale(2, RoundingMode.UNNECESSARY));
    //1.1 可以当做1.10 ，所以后面写2没毛病
    result =(new BigDecimal("1.1").setScale(2, RoundingMode.UNNECESSARY));

    //setScale 以后默认是RoundingMode.UNNECESSARY
   result =(new BigDecimal("1.111").setScale(3));
    
    //下面这个抛出异常 java.lang.ArithmeticException: Rounding necessary
    result =(new BigDecimal("1.111").setScale(2, RoundingMode.UNNECESSARY));
    

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
		id: '5SLVDigF6XqbONPH',
    });
    gitalk.render('gitalk-container');
</script> 
