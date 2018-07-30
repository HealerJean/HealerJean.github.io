---
title: 利用另一张表update更新字段_insert语句使用另一张表数据
date: 2018-07-15 03:33:00
tags: 
- Database
category: 
- Database
description: 利用另一张表update更新字段_insert语句使用另一张表数据
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

## 1、insert语句

### 1.1、Insert into
```
  语句形式为：Insert into Table2(field1,field2,...) select value1,value2,... from Table1
  
要求目标表Table2必须存在，由于目标表Table2已经存在，所以我们除了插入源表Table1的字段外，还可以插入常量。示例如下：
举例：Insert into Table2(a, c, d) select a,c,5 from Table1




INSERT INTO coupon_item_good_admin_user
( couponItemGoodId, couponAdzoneId, pubAdminId, pubAdminName, volume,
  cdate, udate, status)
  SELECT  c.id,c.couponAdzoneId,c.createAdminId,c.createAdminName,0, now(),now(),1
  from coupon_item_good c;




```

### 1.2、select into

```
SELECT vale1, value2 into Table2 from Table1

要求目标表Table2不存在，因为在插入时会自动创建表Table2，并将Table1中指定字段数据复制到Table2中。示例如下：
举例select a,c INTO Table2 from Table1

select ename,sal,job into pname,psal,pjob from emp where empno = eno;

```


## 2、update

```
UPDATE  coupon_item_good b set b.volume =
	(
	SELECT m.orderSize from
	  ( SELECT  COUNT(c.itemId) as orderSize, c.itemId  as itemId from coupon_taoke_data c  WHERE  c.orderStatus not in ('订单失效') and c.status = 1 GROUP  by c.itemId  )  m WHERE   m.itemId = b.itemId )  
WHERE  b.itemId IN (40762167681);  

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
		id: 'cx7pz7OTtfmZ7DVB',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

