---
title: SpringBoot文件上传下载
date: 2019-04-26 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot文件上传下载
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    

习惯了使用OSS傻瓜式上传，是不是都快忘记写原生的上传了，今天小米的项目中需要用一下，所以之类简单总结下 吧



| MultipartFile file 方法名字                   | 内容      |
| -------------------------- | --------- |
| file.getContentType()      | image/png |
| file.getOriginalFilename() | AAAA.png  |
| file.getName()             | file      |



## 1、上传



#### 1、FileUtils.copyInputStreamToFile



```java
File localFile = new File(tempFile,fileName);
FileUtils.copyInputStreamToFile(file.getInputStream(),localFile);

```

#### 2、file.transferTo

```java
我使用这种方式报错了，没有深入研究，网上说是jar包问题

<dependency>
    	<groupId>commons-fileupload</groupId>
    	<artifactId>commons-fileupload</artifactId>
    	<version>1.3.1</version>
</dependency>



File localFile = new File(tempFile,fileName);
file.transferTo(localFile);
```

#### 3、正常读取

```java
byte[] bytes = file.getBytes();
 
File localFile = new File(tempFile,fileName);

BufferedOutputStream stream = new BufferedOutputStream(
    new FileOutputStream(localFile));

stream.write(bytes);




BufferedOutputStream out = new BufferedOutputStream(
    new FileOutputStream(localFile));
in = file.getInputStream();
int num = 0;
byte[] b = new byte[1024];
while((num = in.read(b)) != -1) {
    out.write(b, 0, num);
}

```




```java
 @Override
    public String upload(MultipartFile file){

        String name = UUID.randomUUID().toString().replaceAll("-", "");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String localTime = df.format(dateTime);

        //文件存储最终目录
        File tempFile = new File("/User/healerjean/Desktop"+localTime);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }
        
        String fileName = file.getOriginalFilename();
        fileName = name+fileName.substring(fileName.lastIndexOf(".") );
        
        File localFile = new File(tempFile,fileName);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),localFile);
            
        
            //2、 file.transferTo(localFile);
            
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
      return fileName;
    }
```



## 2、下载

```java
@GetMapping("/{id}")
public void downLoad(HttpServletResponse response,String folder) {
    try {
        InputStream inputStream = new FileInputStream(new File(folder));
        OutputStream outputStream = response.getOutputStream();

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=test.txt");

        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
    } catch (Exception e) {

    }
}

```










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
		id: 'aGWxQES4Uivrm0On',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

