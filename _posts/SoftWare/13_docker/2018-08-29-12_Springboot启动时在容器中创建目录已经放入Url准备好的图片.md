---
title: Springboot启动时在容器中创建目录已经放入Url准备好的图片
date: 2018-08-29 03:33:00
tags: 
- Docker
category: 
- Docker
description: Springboot启动时在容器中创建目录已经放入Url准备好的图片
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           





```java
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
		id: '1Aw9ckzqVmXx2KF3',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

