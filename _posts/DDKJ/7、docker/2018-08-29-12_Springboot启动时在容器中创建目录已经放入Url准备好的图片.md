---
title: Springboot启动时在容器中创建目录已经放入Url准备好的图片
date: 2018-08-29 03:33:00
tags: 
- Docker
category: 
- Docker
description: Springboot启动时在容器中创建目录已经放入Url准备好的图片
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言



```
package com.duodian.youhui.admin.config;

import com.duodian.youhui.admin.constants.WeChatMessageParams;
import com.duodian.youhui.admin.constants.WechatMenuParams;
import com.duodian.youhui.dao.db.wechat.WeChatBusinessNoRepository;
import com.duodian.youhui.entity.db.wechat.WeChatBusinessNo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/8/17  下午5:18.
 */
@Component
public class PostConstructConfig {




    @Value("${docker_imagesDirectory}")
    private String docker_imagesDirectory ;

    @Value("${docker_erweimaEmptDirectory}")
    private String docker_erweimaEmptDirectory ;


    @Resource
    private WeChatBusinessNoRepository weChatBusinessNoRepository;


    @PostConstruct
    private void init()  throws Exception {


        //为docker容器中创建目录
        File imagesDirectory = new File(docker_imagesDirectory);
        File erweimaEmptDirectory = new File(docker_erweimaEmptDirectory);

        if(!imagesDirectory.exists()){
            imagesDirectory.mkdirs();
        }
        if(!erweimaEmptDirectory.exists()){
            erweimaEmptDirectory.mkdirs();
        }

        File shikeImage = new File(imagesDirectory,"shike.jpg");
        if(!shikeImage.exists()){
            FileOutputStream outputStreamShikeImage = new FileOutputStream(shikeImage);
            URL ushike = new URL("http://image.dangqugame.cn/admin/webproject/shike.jpg");
            BufferedImage imageShikeImage = ImageIO.read(ushike);
            ImageIO.write(imageShikeImage, "jpg", outputStreamShikeImage);

        }

        File contactmanager = new File(imagesDirectory, "contactmanager.jpg");
        if(!contactmanager.exists()) {
            FileOutputStream outputStreamContactmanager = new FileOutputStream(contactmanager);
            URL ucontactmanager = new URL("http://image.dangqugame.cn/admin/webproject/contactmanager.jpg");
            BufferedImage imageContactmanager = ImageIO.read(ucontactmanager);
            ImageIO.write(imageContactmanager, "jpg", outputStreamContactmanager);
        }
    }


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
		id: '1Aw9ckzqVmXx2KF3',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

