---
title: Idea使用httpclient调用接口
date: 2019-04-18 03:33:00
tags: 
- SoftWare
category: 
- SoftWare
description: Idea使用httpclient调用接口
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



使用过swagger、觉得这个已经很方便了，再使用postman，觉得随便麻烦一些，但总算数据可以保存了。但是使用过idea的httclient之后，真的不想再使用他们了。

## 1、打开方式

![1555577367282](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1555577367282.png)



### 1.1、关于下面这种方式呢，不建议使用，因为用了下面的，其实就和postman没区别了

![1555577440223](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1555577440223.png)





## 2、正确的使用方式

![1555577486643](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1555577486643.png)

### 2.1、Post请求

#### 2.1.1、@RequestBody  

   @requestbody的含义是在当前对象获取整个http请求的body里面的所有数据，从@requestbody设计上来说，只获取一次就可以拿到请求body里面的所有数据，就不能够出现有多个@requestbody出现在controller的函数的形参列表当中，

---------------------


 不写的话，按照下面的正常调用就行了   但是如果写上了里面参数使用了required=true，一定不能让**DTO对象**为null，否则就会报错`org.springframework.http.converter.HttpMessageNotReadableException: Required request body is missing` 或者将它设置为**required=false**      

如果使用了它，传递的必须为json 



```java
@PostMapping("addData")
public String addData(@RequestBody(required = false) DataDTO dataDTO){

}

```



```http
POST http://localhost.admin/addData
Accept: */*
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8
Cookie: JSESSIONID=e1fd90bf-1148-4368-9fe9-018dcaf1aa0d
#请求参数,注意这里要空一行，否则就不会调用成功，因为如果和上面的参数紧挨着，就会被认为是参数的一种

{"typeKey":"country","dataKey":"china","dataValue":"中国"}

```



#### 2.2.1、非@RequestBody  请求，正常表单

```http
POST http://localhost.admin/api/sys/post?type=SingleLoan
Accept: */*
Cache-Control: no-cache
Cookie: scf-client_SID=24c0e5778aaf4902bc6b45c42b977f4a
Content-Type: multipart/form-data; boundary=WebAppBoundary

```



#### 2.2.2、文件上传



```java
public String upload(@RequestParam( required = false) MultipartFile file, SysFileDTO fileDTO ) throws Exception {

```



```http
# 4.1、文件上传
POST http://scf-client-web/api/sys/file/upload?type=SingleLoan
Accept: */*
Cache-Control: no-cache
Cookie: scf-client_SID=24c0e5778aaf4902bc6b45c42b977f4a
Content-Type: multipart/form-data; boundary=WebAppBoundary

# file 参数名，filename 文件名（我们可以自己取名字）
--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="filename.jpg"
Content-Type: image/jpeg

< C:\Users\HealerJean\Desktop\微信图片_20190830164253.jpg
--WebAppBoundary--

```



文件路径选择，然后点击 ，进行复制路径



![1569579338051](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569579338051.png)





### 2.2、Get 请求

#### 2.2.1、**GetMapping 不支持@RequestBody** 

但是我们Get请求如果使用了@RequestBody这种方式，在使用下面Json请求的方法也是可以调用成功的。但是前端正常的Get请求使用地址调用则不会成功。所以Get请求，直接写上对象就行了，不要写@RequestBody,，虽然能调通，但确实错误的代码

```java
@GetMapping("getDatas")
public String getDatas( DataDTO dataDTO) {
   
}
```



```http
GET http://localhost.admin/getDatas?typeKey=country&dataKey=1
Accept: */*
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8
Cookie: JSESSIONID=a6ad800b-15cf-4148-b659-a604e5a5f0fc

```





### 2.3、Delete请求

```java
@DeleteMapping( "deleteData/{id}")
public String deleteData(@PathVariable Long id){

}
```



```http
DELETE http://localhost.admin/delete/1
Accept: */*
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8
Cookie: JSESSIONID=e1fd90bf-1148-4368-9fe9-018dcaf1aa0d

```



### 2.4、PUT请求

```java
@PutMapping("updateData/{id}")
public String updateData(@PathVariable Long id @RequestBody(required = false)DictionaryTypeDTO typeDTO){

}
```



```http
PUT http://localhost.admin/api/sys/updateData/1
Accept: */*
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8
Cookie: JSESSIONID=e1fd90bf-1148-4368-9fe9-018dcaf1aa0d
#请求参数

{"typeKey":"country","typeDesc":"国际"}
```



#### 2.5、规范

+ **三个#号有效分隔Http请求，所以一般在注释上进行添加**

```http
############ 1、合同 #######################################
### 1.1、字典类型添加
POST http://localhost.admin/api/sys/dictType/add
Accept: */*
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8
Cookie: scf-manager_SID=e1fd90bf-1148-4368-9fe9-018dcaf1aa0d

{"typeKey":"sex","typeDesc":"性别"}
# 返回结果
#"{\"msg\":\"新增字典数据成功\",\"typeKey\":\"country\",\"typeDesc\":\"国家\",\"id\":null}"


### 1.2、字典类型删除
DELETE http://localhost.admin/api/sys/dictType/1
Accept: */*
Cache-Control: no-cache
Content-Type: application/json; charset=UTF-8
Cookie: scf-manager_SID=e1fd90bf-1148-4368-9fe9-018dcaf1aa0d
# 返回结果
# "{\"msg\":\"字典类型删除成功\"}"
```





## 3、PostMan



### 3.1、Cookie文件上传测试



![1569579681814](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569579681814.png)

![1569579673129](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1569579673129.png)





### 3.2、postman配置环境变量



#### 3.2.1、设置添加环境变量

![1570762848691](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1570762848691.png)



![1570762873858](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1570762873858.png)


#### 3.2.2、配置环境变量的值


![1570762887604](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1570762887604.png)



#### 3.2.3、变量访问{{ucenter_url}}



![1570762979110](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1570762979110.png)



​     



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
		id: '2ByNp1oKS8W0wauJ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

