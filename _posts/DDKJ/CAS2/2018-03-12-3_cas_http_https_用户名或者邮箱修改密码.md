---
title: 3、cas_http_https_用户名或者邮箱修改密码
date: 2018-03-12 14:36:00
tags: 
- CAS
category: 
- CAS
description: cas_http_https_用户名或者邮箱修改密码
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
-->


## 前言，准备

cas自定义通过邮箱修改密码，这里的验证，点击邮箱链接之后还会输入密保问题。本文章有http和https两种验证邮箱。不必着急往后看。证书无非是SSL设置为true或者false；

## 1、在sso-server的resource下放入创建好的证书

![WX20180312-122829](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-122829.png)

## 2、cas这里的邮箱验证，数据表创建

这里的cas邮箱验证，是通过给用户发邮件，然后用户跳转到服务端，通过密保问题进行修改的。所以需要创建密保问题。这里是我测试的是通过用户名查找邮箱修改密码的。一会我们再测试一遍通过邮箱登录。修改密码

这里还有添加多个密保问题的，一会我们在通过截图观察
```
use casnew;
/*
密码重置问题表
*/
CREATE TABLE SYS_USER_QUESTION (
  USERNAME VARCHAR(30) NOT NULL,
  QUESTION VARCHAR(200) NOT NULL,
  ANSWER     VARCHAR(100) NOT NULL
);

---问题数据
INSERT INTO SYS_USER_QUESTION VALUES ('admin', '你的年龄是？',  '24');
INSERT INTO SYS_USER_QUESTION VALUES ('HealerJean', '我的名字是？',  'HealerJean');
INSERT INTO SYS_USER_QUESTION VALUES ('zhangsan', '我在哪里工作？',  'guangzhou');


```

## 3、sso-server开始配置邮箱登录

### 1、首先我们需要一个邮箱，并且开启smpt验证。这里我用了QQ邮箱，有需要的朋友们自己弄哦，因为需要密码所以我就测试完本例就会关闭
---
### 2、开启证书验证 

```
## ssl证书
server.ssl.enabled=true
server.ssl.key-store=classpath:tomcat.keystore
server.ssl.key-store-password=123456
server.ssl.keyAlias=passport.sso.com

```
**证书验证开启之后，下面的这个红色将会消失。

![WX20180312-153905@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-153905@2x.png)




### 3、数据库验证和配置mail以及邮箱问题查询，我采用的是MD5写死直接验证

关键点：

```
#允许内置密码管理，配置了true，点击页面的重置密码才会有反应，跳转。也就是才有这个功能
cas.authn.pm.enabled=true

#新密码必须匹配表达式
cas.authn.pm.policyPattern=\\d{3,10}
```

具体配置：

---
```
#  7.1、MD5 密码直接校验,用于邮箱修改密码

#Query Database Authentication 数据库查询校验用户名开始
#查询账号密码sql，必须包含密码字段，
# 这里可以使用用户名或者邮箱来进行验证，根据数据字段如果是用户名则为username 邮箱则为email
cas.authn.jdbc.query[0].sql=select * from sys_user where username=?
#指定上面的sql查询字段名（必须）
cas.authn.jdbc.query[0].fieldPassword=password
#指定过期字段，1为过期，若过期不可用
cas.authn.jdbc.query[0].fieldExpired=expired
#为不可用字段段，1为不可用，需要修改密码
cas.authn.jdbc.query[0].fieldDisabled=disabled
#数据库方言hibernate的知识
cas.authn.jdbc.query[0].dialect=org.hibernate.dialect.MySQL5Dialect
#数据库驱动
cas.authn.jdbc.query[0].driverClass=com.mysql.jdbc.Driver
#数据库连接
cas.authn.jdbc.query[0].url=jdbc:mysql://localhost:3306/casnew?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true
#数据库用户名
cas.authn.jdbc.query[0].user=root
#数据库密码
cas.authn.jdbc.query[0].password=123456
#默认加密策略，通过encodingAlgorithm来指定算法，默认NONE不加密,可以自定义密码认证，比如ddkj就是写了一个自定义的认证
cas.authn.jdbc.query[0].passwordEncoder.type=DEFAULT
cas.authn.jdbc.query[0].passwordEncoder.characterEncoding=UTF-8
cas.authn.jdbc.query[0].passwordEncoder.encodingAlgorithm=MD5
#Query Database Authentication 数据库查询校验用户名结束



# 7.2、密码管理 开始
#允许内置密码管理，配置了true，点击重置密码才会跳转。也就是才有这个功能
cas.authn.pm.enabled=true
#另外：由于已经发布的代码默认提交是false，导致不能修改密码成功请添加以下配置
cas.authn.pm.jdbc.autocommit=true
#重置信息 https://apereo.github.io/cas/5.1.x/installation/Configuration-Properties.html#password-management
cas.authn.pm.reset.from=${spring.mail.username}
#发送邮件标题
cas.authn.pm.reset.subject=SSO DEMO 重置密码
#邮件内容，必须要有%s，因为会生成一个连接并且带了token，否则无法打开链接，当然这个链接也和cas.server.prefix有关系
cas.authn.pm.reset.text=打开以下链接重置您的密码（SSO-DEMO）: %s
#token失效分钟数
cas.authn.pm.reset.expirationMinutes=10
cas.authn.pm.reset.emailAttribute=mail
#是否开启问题回答
cas.authn.pm.reset.securityQuestionsEnabled=true
#新密码必须匹配表达式
cas.authn.pm.policyPattern=\\d{3,10}

#发送邮件
spring.mail.host=smtp.qq.com
spring.mail.port=465
#邮箱用户名
spring.mail.username=1318830916@qq.com
#邮箱授权码
spring.mail.password=jxfxaomhvoqmihae

#mjwompxtqeaziaeg
spring.mail.testConnection=false
spring.mail.properties.mail.smtp.auth=true
#必须ssl，差点被误导了，这里的ssl和我们的证书没有关系，只是mail内部的事情
spring.mail.properties.mail.smtp.ssl.enable=true

#邮箱查找
#根据用户名查找邮箱
cas.authn.pm.jdbc.sqlFindEmail=select email from sys_user where username=?
#根据用户名查找问题
cas.authn.pm.jdbc.sqlSecurityQuestions=select question, answer from sys_user_question where username=?
#修改密码
cas.authn.pm.jdbc.sqlChangePassword=update sys_user set password=? where username=?
cas.authn.pm.jdbc.url=${cas.authn.jdbc.query[0].url}
cas.authn.pm.jdbc.user=${cas.authn.jdbc.query[0].user}
cas.authn.pm.jdbc.password=${cas.authn.jdbc.query[0].password}
cas.authn.pm.jdbc.dialect=${cas.authn.jdbc.query[0].dialect}
cas.authn.pm.jdbc.driverClass=${cas.authn.jdbc.query[0].driverClass}
#密码修改加密规则，这个必须要和原始密码加密规则一致
cas.authn.pm.jdbc.passwordEncoder.type=${cas.authn.jdbc.query[0].passwordEncoder.type}
cas.authn.pm.jdbc.passwordEncoder.characterEncoding=${cas.authn.jdbc.query[0].passwordEncoder.characterEncoding}
cas.authn.pm.jdbc.passwordEncoder.encodingAlgorithm=${cas.authn.jdbc.query[0].passwordEncoder.encodingAlgorithm}
#cas.authn.pm.jdbc.passwordEncoder.secret=${cas.authn.jdbc.query[0].passwordEncoder.secret}
#密码管理 结束

```
## 4、至此，基本上就算完成了。启动sso-server开始测试吧。哈哈

**在进入下面的修改密码页面的时候，如果控制台出现下面的问题，无须理会**

![WX20180312-140832](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-140832.png)


1、输入用户名：HealerJean
 

![WX20180312-133304@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-133304@2x.png)

提示发送成功。

![WX20180312-133537@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-133537@2x.png)

2、到邮箱中开始确认，但是发现域名并不是我们提供的https域名password.sso.com，而是cas给我们提供的举例域名，这个肯定是点不开的楼

![WX20180312-133940@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-133940@2x.png)


## 5、修改邮箱中的链接的域名为我们自己证书中配置的域名。

这个时候，我们才正儿八经用到的etc/config/cas.properties中的内容，修改成如下：

![WX20180312-134235](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-134235.png)


```
cas.server.name: https://passport.sso.com:8443
cas.server.prefix: https://passport.sso.com:8443/cas

cas.adminPagesSecurity.ip=127\.0\.0\.1

logging.config: file:/etc/cas/config/log4j2.xml
# cas.serviceRegistry.config.location: classpath:/services


```

## 6、开始启动测试吧，朋友们：同4中步奏。打开邮箱吧；

1、观察下面的，发现这次对喽，点击链接打开

![WX20180312-134412@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-134412@2x.png)

2、点开链接之后，我们会发现让我们输入密保问题的答案，输入答案HealerJean （为上面的数据库中的测试数据）

![WX20180312-134557@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-134557@2x.png)

小插曲，多个几个密保问题，我们为HealerJean 多加几个密保问题吧。

```
INSERT INTO SYS_USER_QUESTION VALUES ('HealerJean', '你喜欢的人是？',  'Liuli');
```

![WX20180312-135746@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-135746@2x.png)



3、开始输入新密码 123 ，这里的密码输入的规则，我们在配置文件中其实定义了，这里定义的是要求输入3到10位的数字，否则，就会提示不通过的

```
#新密码必须匹配表达式
cas.authn.pm.policyPattern=\\d{3,10}

```

![WX20180312-134703@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-134703@2x.png)

4、提示修改成功，可以登录使用了

![WX20180312-135018@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-135018@2x.png)


## 7、至此邮箱修改密码就算成功了，这次我们将登陆的用户的username变成邮箱email测试下

### 1、为问题数据表添加一列为email，并插入数据

```
/**
测试2、将登陆的用户名变成邮箱，已经修改密码按照自己填写的邮箱开始验证。
 */
ALTER TABLE SYS_USER_QUESTION ADD email VARCHAR(100) ;
UPDATE SYS_USER_QUESTION SET  email = 'mxzdhealer@gmail.com' WHERE  USERNAME = 'HealerJean';
UPDATE SYS_USER_QUESTION SET email = 'huang.wenbin@foxmail.com' WHERE USERNAME = 'admin';
UPDATE SYS_USER_QUESTION SET email = 'zhangsan@foxmail.com' WHERE USERNAME = 'zhangsan' ;

```

### 2、配置文件中修改登录的参数为email

```


#  7.2.1、MD5 密码直接校验,用于邮箱修改密码。email登录

#Query Database Authentication 数据库查询校验用户名开始
#查询账号密码sql，必须包含密码字段，
# 这里可以使用用户名或者邮箱来进行验证，根据数据字段如果是用户名则为username 邮箱则为email
cas.authn.jdbc.query[0].sql=select * from sys_user where email=?
#指定上面的sql查询字段名（必须）
cas.authn.jdbc.query[0].fieldPassword=password
#指定过期字段，1为过期，若过期不可用
cas.authn.jdbc.query[0].fieldExpired=expired
#为不可用字段段，1为不可用，需要修改密码
cas.authn.jdbc.query[0].fieldDisabled=disabled
#数据库方言hibernate的知识
cas.authn.jdbc.query[0].dialect=org.hibernate.dialect.MySQL5Dialect
#数据库驱动
cas.authn.jdbc.query[0].driverClass=com.mysql.jdbc.Driver
#数据库连接
cas.authn.jdbc.query[0].url=jdbc:mysql://localhost:3306/casnew?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=true
#数据库用户名
cas.authn.jdbc.query[0].user=root
#数据库密码
cas.authn.jdbc.query[0].password=123456
#默认加密策略，通过encodingAlgorithm来指定算法，默认NONE不加密,可以自定义密码认证，比如ddkj就是写了一个自定义的认证
cas.authn.jdbc.query[0].passwordEncoder.type=DEFAULT
cas.authn.jdbc.query[0].passwordEncoder.characterEncoding=UTF-8
cas.authn.jdbc.query[0].passwordEncoder.encodingAlgorithm=MD5
#Query Database Authentication 数据库查询校验用户名结束



# 7.2.2、密码管理 开始 email登录
#允许内置密码管理，配置了true，点击重置密码才会跳转。也就是才有这个功能
cas.authn.pm.enabled=true
#另外：由于已经发布的代码默认提交是false，导致不能修改密码成功请添加以下配置
cas.authn.pm.jdbc.autocommit=true
#重置信息 https://apereo.github.io/cas/5.1.x/installation/Configuration-Properties.html#password-management
cas.authn.pm.reset.from=${spring.mail.username}
#发送邮件标题
cas.authn.pm.reset.subject=SSO DEMO 重置密码
#邮件内容，必须要有%s，因为会生成一个连接并且带了token，否则无法打开链接，当然这个链接也和cas.server.prefix有关系
cas.authn.pm.reset.text=打开以下链接重置您的密码（SSO-DEMO）: %s
#token失效分钟数
cas.authn.pm.reset.expirationMinutes=10
cas.authn.pm.reset.emailAttribute=mail
#是否开启问题回答
cas.authn.pm.reset.securityQuestionsEnabled=true
#新密码必须匹配表达式
cas.authn.pm.policyPattern=\\d{3,10}

#发送邮件
spring.mail.host=smtp.qq.com
spring.mail.port=465
#邮箱用户名
spring.mail.username=1318830916@qq.com
#邮箱授权码
spring.mail.password=jxfxaomhvoqmihae
spring.mail.testConnection=false
spring.mail.properties.mail.smtp.auth=true
#必须ssl，差点被误导了，这里的ssl和我们的证书没有关系，只是mail内部的事情
spring.mail.properties.mail.smtp.ssl.enable=true

#邮箱查找
#根据用户名查找邮箱
cas.authn.pm.jdbc.sqlFindEmail=select email from sys_user where email=?
#根据用户名查找问题
cas.authn.pm.jdbc.sqlSecurityQuestions=select question, answer from sys_user_question where email=?
#修改密码
cas.authn.pm.jdbc.sqlChangePassword=update sys_user set password=? where email=?
cas.authn.pm.jdbc.url=${cas.authn.jdbc.query[0].url}
cas.authn.pm.jdbc.user=${cas.authn.jdbc.query[0].user}
cas.authn.pm.jdbc.password=${cas.authn.jdbc.query[0].password}
cas.authn.pm.jdbc.dialect=${cas.authn.jdbc.query[0].dialect}
cas.authn.pm.jdbc.driverClass=${cas.authn.jdbc.query[0].driverClass}
#密码修改加密规则，这个必须要和原始密码加密规则一致
cas.authn.pm.jdbc.passwordEncoder.type=${cas.authn.jdbc.query[0].passwordEncoder.type}
cas.authn.pm.jdbc.passwordEncoder.characterEncoding=${cas.authn.jdbc.query[0].passwordEncoder.characterEncoding}
cas.authn.pm.jdbc.passwordEncoder.encodingAlgorithm=${cas.authn.jdbc.query[0].passwordEncoder.encodingAlgorithm}
#cas.authn.pm.jdbc.passwordEncoder.secret=${cas.authn.jdbc.query[0].passwordEncoder.secret}
#密码管理 结束

```
### 3、启动这个服务吧，开始测试；

#### 3.1、输入邮箱，成功发送

![WX20180312-142013@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-142013@2x.png)

#### 3.2、 mxzdhealer@gamail.com中打开邮件，点击链接，发现变成了email了，ok成功；

![WX20180312-142110@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180312-142110@2x.png)


## 8、http邮箱发送，测试成功

### 8.1、修改 etc/config/cas.properties 链接为http

```
cas.server.name: http://passport.sso.com:8443
cas.server.prefix: http://passport.sso.com:8443/cas

cas.adminPagesSecurity.ip=127\.0\.0\.1

logging.config: file:/etc/cas/config/log4j2.xml
# cas.serviceRegistry.config.location: classpath:/services

```

### 8.2、SSL 设置为false

```
## ssl证书
server.ssl.enabled=false
#server.ssl.key-store=classpath:tomcat.keystore
#server.ssl.key-store-password=123456
#server.ssl.keyAlias=passport.sso.com

```
### 8.3、启动即可，成功。
---
## [9、代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_12_3_cas%E9%82%AE%E7%AE%B1%E4%BF%AE%E6%94%B9%E5%AF%86%E7%A0%81/com-hlj-cas.zip)

---
---
---
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|



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
		id: 'pksf9KLM9M5waDCl',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

