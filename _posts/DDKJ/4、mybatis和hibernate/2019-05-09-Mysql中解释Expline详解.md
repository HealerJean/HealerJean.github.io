---
title: Mysql中解释Expline详解
date: 2019-05-08 03:33:00
tags: 
- Database
category: 
- Database
description: Mysql中解释Expline详解
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言 mysql优化器在数据量不同的情况下，也会到结果产生影响

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



**优化神器 Explain 使用分析**      

```sql
create table d001_index(
  id bigint(16) unsigned NOT NULL  AUTO_INCREMENT primary key ,
  name    varchar(128) DEFAULT NULL,
  age    bigint(20)   DEFAULT '0',
  country varchar(50)  DEFAULT NULL,
  a  int(11) default 0 ,
  b  int(11) default 0 ,
  c  int(11) default 0 ,
  d  int(11) default 0,
  unique index idx_name (name),
  index idx_a_b_c_d(a,b,c,d)
);

INSERT INTO `hlj-mysql`.d001_index (id, name, age, country, a, b, c, d) VALUES (1, 'zhangyj', 25, 'chine', 1, 2, 3, 4);
INSERT INTO `hlj-mysql`.d001_index (id, name, age, country, a, b, c, d) VALUES (2, 'healerjean', 24, 'china', 2, 3, 4, 5);
INSERT INTO `hlj-mysql`.d001_index (id, name, age, country, a, b, c, d) VALUES (3, 'n', 22, 'a', 2, 4, 5, 6);
INSERT INTO `hlj-mysql`.d001_index (id, name, age, country, a, b, c, d) VALUES (4, 'k', 2, 'b', 3, 5, 6, 8);


CREATE TABLE `d001_index_order_info`
(
  `id`           bigint(16) unsigned NOT NULL AUTO_INCREMENT,
  `ref_user_id`  bigint(16) unsigned NOT NULL COMMENT '用户名',
  `product_name` varchar(50)         NOT NULL DEFAULT '' COMMENT '产品名称',
  `price`        decimal(19, 4)               DEFAULT '0.0000' COMMENT '价格',
  `num`          int(11)                      DEFAULT '0' COMMENT '数量',
  `param`        varchar(10)                  DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_uid_proname_price_num` (`ref_user_id`, `product_name`, `price`, `num`)
) COMMENT ='订单表'
  

INSERT INTO d001_index_user_info (name, age,sex) VALUES ('a', 4,1);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('b', 5,2);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('c', 6,1);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('d', 7,2);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('e', 8,0);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('f', 9,1);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('g', 10,2);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('h', 10,1);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('i', 12,2);
INSERT INTO d001_index_user_info (name, age,sex) VALUES ('j', 12,1);


CREATE TABLE `d001_index_order_info`
(
  id           BIGINT(16) unsigned NOT NULL AUTO_INCREMENT,
  ref_user_id  BIGINT(16) unsigned NOT NULL comment '用户名',
  product_name VARCHAR(50)         NOT NULL DEFAULT '' comment '产品名称',
  price        decimal(19, 4)               DEFAULT 0.0000 comment '价格',
  num          int(11)                      default 0 comment '数量',
  PRIMARY KEY (`id`),
  KEY `idx_uid_proname_price_num` (ref_user_id, product_name, price,num)
) comment '索引 订单表';

INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (1, 'p1', 10.1,1);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (1, 'p2', 10.2,3);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (1, 'p3', 10.4,4);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (2, 'p1', 10.4,1);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (2, 'p4', 11,10);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (3, 'p6', 16,100);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (4, 'p7', 19,9);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (5, 'p2', 12,4);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (8, 'p9', 5,3);
INSERT INTO d001_index_order_info (ref_user_id, product_name, price,num)VALUES (9, 'p3', 7,1);

    
    
    

```



## 1、解释参数含义


| 参数           | 含义 |
| ------------- | ---- |
| id            | 查询的标识符 |
| select_type   | SELECT 查询的类型.                                         |
| table         | 查询的是哪个表 |
| partitions    | 匹配的分区 |
| type          | 判断是什么扫描查询 比如：ALL,Index，Rank                   |
| possible_keys | 可能选用的索引 |
| key | 确切使用到的索引                                 |
| key_len       | 索引长度（**通过观察这个可以判断联合索引使用了几列，很有用**） |
| ref           | 哪个字段或常数与 key 一起被使用 |
| rows | 显示此查询一共扫描了多少行. 这个是一个估计值. |
| filtered | 表示此查询条件所过滤的数据的百分比 |
| extra | 额外的信息 |



### 1.1、select_type

#### 1.1.1、SIMPLE 简单查询

##### **解释：此查询不包含 UNION 查询或子查询**



```sql
SELECT * from d001_index_user_info ;
```

| id   | select_type | table                | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | extra |
| ---- | ----------- | -------------------- | ---------- | ---- | ------------- | ---- | ------- | ---- | ---- | -------- | ----- |
| 1    | SIMPLE      | d001_index_user_info |            | ALL  |               |      |         |      | 10   | 100      |       |

#### 1.1.2、UNION 联合查询

##### **解释：表示此查询是 UNION 的第二或随后的查询**

```sql
explain  	 select * from d001_index_user_info union select * from d001_index_user_info ;

```

1、`id = 1`  row是4行，说明第一个select查询最`PRIMARY` 外层查询    

2、`id = 2` row是2行，说明第二个select查询 是`UNION `联合查询

3、`id = 3` `UNION RESULT` 很明显为联合查询的结果

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>PRIMARY</td><td>d001_index_user_info</td><td>NULL</td><td>range</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>NULL</td><td>4</td><td>100</td><td>Using where</td></tr>
<tr><td>2</td><td>UNION</td><td>d001_index_user_info</td><td>NULL</td><td>range</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>NULL</td><td>2</td><td>100</td><td>Using where</td></tr>
<tr><td>NULL</td><td>UNION RESULT</td><td>&lt;union1,2&gt;</td><td>NULL</td><td>ALL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>Using temporary</td></tr></table>
</body>
</html>



#### 1.2.3、SUBQUERY       子查询



```sql
explain select *
        from d001_index_user_info
        where id > (select b.id from d001_index_user_info b where b.id = 5);
```

1、第一个 select 为最 `PRIMARY` 外层查询      

2、第二个 select 为 `SUBQUERY `子查询

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>PRIMARY</td><td>d001_index_user_info</td><td>NULL</td><td>range</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>NULL</td><td>5</td><td>100</td><td>Using where</td></tr>
<tr><td>2</td><td>SUBQUERY</td><td>b</td><td>NULL</td><td>const</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>const</td><td>1</td><td>100</td><td>Using index</td></tr></table>
</body>
</html>



#### 1.2.4、UNION RESULT 联合查询的结果

##### **解释：在 `1.2.2` 中介绍过了**

#### 1.2.5、PRIMARY`  最外层查询

##### **解释：在 `1.2.2 `和 `1.1.3`中介绍过** 



### 1.2、type 判断是全表还是索引扫描

##### **解释：它提供了判断查询是否高效的重要依据依据. 通过 `type` 字段, 我们判断此次查询是 `全表扫描` 还是 `索引扫描` 等.**



#### 1.2.1、性能比较

```
性能：ALL < index < range ~ index_merge < ref < eq_ref < const < system
```



#### 1.2.2、ALL 全表扫描

```sql
explain SELECT * from d001_index_user_info ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>ALL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>10</td><td>100</td><td>NULL</td></tr></table>
</body>
</html>

#### 1.2.3、index： 表示全索引扫描 （索引覆盖）和ALL类似

##### 解释 ：

1、`index`: 表示全索引扫描， 和 ALL 类型类似, 只不过 ALL 类型是全表扫描, **而 index 类型则仅仅扫描所有的索引, 而不扫描数据**.       其实就是讲 查询条件 写上索引的字段

2、`index` 类型通常出现在: **所要查询的数据直接在索引树中就可以获取到, 而不需要扫描数据.** **当是这种情况时, Extra 字段 会显示 `Using index`.**     

3、`index` 类型的查询虽然不是全表扫描, 但是它扫描了所有的索引, 因此比 ALL 类型的稍快.

```sql
explain SELECT name FROM  d001_index_user_info
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>index</td><td>NULL</td><td>idx_name</td><td>82</td><td>NULL</td><td>10</td><td>100</td><td>Using index</td></tr></table>
</body>
</html>


#### 1.2.4、range ：索引范围内查询 

**解释：   **

1、通过索引字段范围获取表中部分数据记录. 这个类型通常出现在 =, <>, >, >=, <, <=, IS NULL, <=>, BETWEEN, IN() 操作中     

**2、当 `type` 是 `range` 时, 那么 EXPLAIN 输出的 `ref` 字段为 NULL, 并且 `key_len` 字段是此次查询中使用到的索引的最长的那个**     



```sql
EXPLAIN SELECT * FROM  d001_index_user_info where id  > 1 ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>range</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>NULL</td><td>9</td><td>100</td><td>Using where</td></tr></table>
</body>
</html>



#### 1.2.5、INDEX_MERGE ：合并索引，使用多个单列索引搜索

```sql
EXPLAIN SELECT * FROM  d001_index_user_info where id  = 1  or name = 'b';
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>index_merge</td><td>PRIMARY,idx_name</td><td>PRIMARY,idx_name</td><td>8,82</td><td>NULL</td><td>2</td><td>100</td><td>Using union(PRIMARY,idx_name); Using where</td></tr></table>
</body>
</html>



#### 1.2.6、REF： 根据索引查找一个或多个值

```sql
EXPLAIN SELECT * FROM  d001_index_user_info where name  = 'a' ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>ref</td><td>idx_name</td><td>idx_name</td><td>82</td><td>const</td><td>1</td><td>100</td><td>NULL</td></tr></table>
</body>
</html>



#### 1.2.7、eq_ref ：连接join查询时，使用primary key 或 unique类型

```sql
EXPLAIN SELECT *
        FROM d001_index_user_info a
               join d001_index_order_info b on a.id = b.ref_user_id;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>b</td><td>NULL</td><td>index</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>225</td><td>NULL</td><td>1</td><td>100</td><td>Using index</td></tr>
<tr><td>1</td><td>SIMPLE</td><td>a</td><td>NULL</td><td>eq_ref</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>hlj-mysql.b.ref_user_id</td><td>1</td><td>100</td><td>Using where</td></tr></table>
</body>
</html>



#### 1.2.8、const  针对主键或唯一索引的等值查询扫描，只有一行

```sql
EXPLAIN SELECT * FROM d001_index_user_info  where  id =1 ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>const</td><td>PRIMARY</td><td>PRIMARY</td><td>8</td><td>const</td><td>1</td><td>100</td><td>NULL</td></tr></table>
</body>
</html>



#### 1.2.9、system ：表中仅仅有一条数据，这个是特殊的const查询





### 1.3、possible_keys ： 可能用到的索引 看1.4

### 1.4、key ：  确切用到的索引

##### 解释：表示 MySQL 在查询时, 能够使用到的索引.     

##### 注意, 即使有些索引在 `possible_keys` 中出现, 但是并不表示此索引会真正地被 MySQL 使用到. MySQL 在查询时具体使用了哪些索引, 由 `key` 字段决定.

```sql
下面这个条件中使用了 索引id 和 联合索引 ref_user_id   
实际上我们只使用了 id 进行查询的，所以 key是id ，possible_keys 是id和 ref_user_id

EXPLAIN select * from d001_index_order_info where id =1 and ref_user_id = 1 ;


```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>const</td><td>PRIMARY,idx_uid_proname_price_num</td><td>PRIMARY</td><td>8</td><td>const</td><td>1</td><td>100</td><td>NULL</td></tr></table>
</body>
</html>


### 1.5、key_len：使用索引字节长度

**解释： 这个字段可以评估联合索引是否完全被使用,** 其实通过上面的我们也可以验证下面的结果

#### 1.5.1、字符串

```
char(n): n 字节长度

varchar(n): 如果是 utf8 编码,    则是 3 n + 2字节; 
            如果是 utf8mb4 编码, 则是 4 n + 2 字节.
```



#### 1.5.2、数值类型:

```
TINYINT: 1字节

SMALLINT: 2字节

MEDIUMINT: 3字节

INT: 4字节

BIGINT: 8字节
```

#### 1.5.3、时间类型

```
DATE: 3字节

TIMESTAMP: 4字节

DATETIME: 8字节
```



### 1.6、rows ：  显示此查询一共扫描了多少行. 这个是一个估计值.



### 1.7、Extra 额外信息

##### 解释 ： 优化器会在索引存在的情况下，通过符合 RANGE 范围的条数和总数的比例来选择是使用索引还是进行全表遍历



```
Using filesort ：排序 不能通过索引达到排序效果

using index ：使用覆盖索引的时候就会出现

using where：在查找使用索引的情况下，需要回表去查询所需的数据

using index condition：查找使用了索引，但是需要回表查询数据

using index & using where：查找使用了索引，但是需要的数据都在索引列中能找到，所以不需要回表查询数据

以上四点就能看出它们之前的区别，或许有部分人都存在疑惑 using index & using where 和using index condition那个比较好，从上面的的解释中就能看出是前者比较好，毕竟不需要回表查询数据，效率上应该比较快的
```

查询的列未被索引覆盖 ：也就是说，查询的列不是全部都是索引列    

回表：表示即使使用索引筛选了，但是查询的字段不是全部都是索引列  



#### 1.7.1、NULL：：查询的列未被索引覆盖，查询条件使用了索引精确查找 =（最左原则）

##### 解释：  意味着用到了索引，但是查询字段未被索引覆盖，必须通过“回表”来实现，不是纯粹地用到了索引，也不是完全没用到索引，Extra中为NULL(没有信息)

```sql
explain SELECT *  from d001_index_order_info  WHERE ref_user_id = 1 ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ref</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>const</td><td>3</td><td>100</td><td>NULL</td></tr></table>
</body>
</html>


#### 1.7.2、Using where：

##### 1.7.2.1、查询条件不是索引  (即使包含了索引)（最左原则）

```
explain SELECT * from d001_index_order_info where param = '1' ;

explain SELECT product_name from d001_index_order_info where param = '1' ;

explain SELECT * from d001_index_order_info where product_name = '1' ;

explain SELECT ref_user_id from d001_index_order_info where param = '1' ;

```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ALL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>10</td><td>10</td><td>Using where</td></tr></table>
</body>
</html>


```sql
explain SELECT ref_user_id from d001_index_order_info where ref_user_id = 1 and param = '1' ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ref</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>const</td><td>3</td><td>10</td><td>Using where</td></tr></table>
</body>
</html>




##### 1.7.2.2、查询的列没有被索引覆盖，查询条件索引按照条件查找(不是精确=查找，否则就会变成 null)

```sql
explain SELECT * from d001_index_order_info where ref_user_id >  1 ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ALL</td><td>idx_uid_proname_price_num</td><td>NULL</td><td>NULL</td><td>NULL</td><td>10</td><td>70</td><td>Using where</td></tr></table>
</body>
</html>



#### 1.7.3、Using index 查询的列被索引覆盖

覆盖索引：就是select的数据列只用从索引中就能够取得，不必从数据表中读取，换句话说查询列要被所使用的索引覆盖。     

**解释：表示查询在索引树中就可查找所需数据, 不用扫描表数据文件, 往往说明性能不错，比ALL快，上面select_type中 1.2.3有相关的解释**



```sql
explain SELECT name FROM  d001_index_user_info
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_user_info</td><td>NULL</td><td>index</td><td>NULL</td><td>idx_name</td><td>82</td><td>NULL</td><td>10</td><td>100</td><td>Using index</td></tr></table>
</body>
</html>


#### 1.7.4、Using where, Using index ：

##### 1.7.4.1、查询的列被索引覆盖，但是查询条件包含不是索引（即使包含了索引）

```sql

explain SELECT ref_user_id  from d001_index_order_info  WHERE product_name = 1 ;

```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>index</td><td>NULL</td><td>idx_uid_proname_price_num</td><td>225</td><td>NULL</td><td>10</td><td>10</td><td>Using where; Using index</td></tr></table>
</body>
</html>

```sql
下面这个注意只使用到了ref_user_id 作为索引，最左优先原则
explain SELECT ref_user_id  from d001_index_order_info  WHERE ref_user_id > 2  and product_name = 1 ; 
```


<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>range</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>NULL</td><td>5</td><td>10</td><td>Using where; Using index</td></tr></table>
</body>
</html>



##### 1.7.4.2、查询的列被索引覆盖，但是查询条件索引按照条件查找(不是精确=查找，否则就会变成 using index)

```sql

explain SELECT ref_user_id from d001_index_order_info where ref_user_id >  1 ;

```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>range</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>NULL</td><td>7</td><td>100</td><td>Using where; Using index</td></tr></table>
</body>
</html>




#### 1.7.5、Using index condition 查询的列未被索引覆盖，但是查询条件是索引的一个范围

```sql
explain SELECT * from d001_index_order_info where ref_user_id <  9  and  ref_user_id > 3 ;

```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>range</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>NULL</td><td>3</td><td>100</td><td>Using index condition</td></tr></table>
</body>
</html>

```sql
explain select * from d001_index_order_info  where ref_user_id = 1  order by  product_name ;

```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ref</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>const</td><td>3</td><td>100</td><td>Using index condition</td></tr></table>
</body>
</html>



#### 1.7.6、Using filesort 

##### 解释：表示我们的排序 不能通过索引达到排序效果，一般有 `Using filesort`, 都建议优化去掉, 因为这样的查询 CPU 资源消耗大.

##### 原因：Mysql对于排序记录的大太多了，而且Myslq优化器会根据查询的总数据，也会造成一定的影响，

由于 Using filesort是使用算法在 内存中进行排序，MySQL对于排序的记录的大小也是有做限制：max_length_for_sort_data，默认为1024

```sql
show variables like '%max_length_for_sort_data%';

+--------------------------+-------+
| Variable_name | Value |
+--------------------------+-------+
| max_length_for_sort_data | 1024 |
+--------------------------+-------+

```

```sql
关于这个 使用了索引的排序，但是却没有使用，由于 Using filesort是使用算法在 内存中进行排序，MySQL对于排序的记录的大小也是有做限制：而且mysql会根据记录数进行自动优化选择，当数据量大的时候情况可能就会不一样
explain select * from d001_index_order_info order by  ref_user_id ;

explain select * from d001_index_order_info  order by  param ;

```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ALL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>NULL</td><td>10</td><td>100</td><td>Using filesort</td></tr></table>
</body>
</html>




##### 1.7.6.1、情况1、

```sql
//这里的 limit是10 或者没有limit的时候 都会出现 Using filesort 这是由于mysql优化器产出的结果

explain select * from d001_index_order_info  where ref_user_id > 1  order by  ref_user_id 

explain select * from d001_index_order_info  where ref_user_id > 1  order by  ref_user_id  LIMIT 10  ;


```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ALL</td><td>idx_uid_proname_price_num</td><td>NULL</td><td>NULL</td><td>NULL</td><td>10</td><td>70</td><td>Using where; Using filesort</td></tr></table>
</body>
</html>

```sql
explain select * from d001_index_order_info  where ref_user_id > 1  order by  ref_user_id  LIMIT 1  ;

explain select * from d001_index_order_info  where ref_user_id > 5  order by  ref_user_id
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>range</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>NULL</td><td>7</td><td>100</td><td>Using index condition</td></tr></table>
</body>
</html>

```sql
explain select * from d001_index_order_info  where ref_user_id = 1  order by  product_name ;
```

<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>
<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>select_type</th><th>table</th><th>partitions</th><th>type</th><th>possible_keys</th><th>key</th><th>key_len</th><th>ref</th><th>rows</th><th>filtered</th><th>Extra</th></tr>
<tr><td>1</td><td>SIMPLE</td><td>d001_index_order_info</td><td>NULL</td><td>ref</td><td>idx_uid_proname_price_num</td><td>idx_uid_proname_price_num</td><td>8</td><td>const</td><td>3</td><td>100</td><td>Using index condition</td></tr></table>
</body>
</html>







<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

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
		id: 'dvUC7YOy8tcWS39r',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

