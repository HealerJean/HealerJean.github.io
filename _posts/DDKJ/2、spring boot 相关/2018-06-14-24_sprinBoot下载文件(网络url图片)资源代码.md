---
title: SpringBoot下载文件资源代码
date: 2018-06-14 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot下载文件资源代码
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

下载文件资源很简单，一开始学习Java web的时候，相信我们绝大多数人都学习过吧，所以这里直接上代码了。

#### 下载成功，主要是给response放入一个outputstream

```java

@GetMapping("downUrlImage")
public void downUrlImage(String url, HttpServletResponse response){
    //设置文件路径
    response.setContentType("application/force-download");// 设置强制下载不打开
    response.addHeader("Content-Disposition", "attachment;fileName="+UUIDGenerator.generate() +"jpg");// 设置文件名
    try {
        URL urlStr = new URL(url);
        BufferedImage bufferedImage = ImageIO.read(urlStr);

        ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
    } catch (MalformedURLException e) {
        log.info(e.getMessage());
    } catch (IOException e) {
        log.info(e.getMessage());
    }catch (Exception e){
        log.info(e.getMessage());
    }

}

```


测试 ：
[http://localhost:8080/duodian/coupon/downUrlImage?url=http%3A%2F%2Flocalhost%3A8080%2Fduodian%2Fcoupon%2FdownUrlImage%3Furl%3Dhttp%253A%252F%252Fimg2.imgtn.bdimg.com%252Fit%252Fu%253D3588772980%252C2454248748%2526fm%253D27%2526gp%253D0.jpg](http://localhost:8080/duodian/coupon/downUrlImage?url=http%3A%2F%2Flocalhost%3A8080%2Fduodian%2Fcoupon%2FdownUrlImage%3Furl%3Dhttp%253A%252F%252Fimg2.imgtn.bdimg.com%252Fit%252Fu%253D3588772980%252C2454248748%2526fm%253D27%2526gp%253D0.jpg)





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
		id: '5uEdExlL8zfGVngY',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

