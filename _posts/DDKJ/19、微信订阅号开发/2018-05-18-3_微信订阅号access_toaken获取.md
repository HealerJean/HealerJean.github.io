---
title: 3、微信订阅号access_toaken获取
date: 2018-05-18 03:33:00
tags: 
- WeChat
category: 
- WeChat
description: 微信订阅号access_toaken获取、
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

## access_toaken能干什么

自定义菜单，客户回复消息，图文上传都需要用到它。非常重要，但是它的使用是有次数的，每天只能使用2000次，所以我们要非常小心，因为它两个小时才过期，所以，只要我们重复使用它，还是够用的啦


![WX20180606-160021@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180606-160021@2x.png)


## 1、解决access_toaken失效问题

### 1.1、使用缓存（快）

### 1.2、使用数据库（慢）博主这里暂时没有缓存库，所以只能倒霉的用到数据库了，哎悲哀

#### 1.2.1、实体


```
/**
 * @Desc: 为了防止调用测试过多
 * @Author HealerJean
 * @Date 2018/6/1  下午3:04.
 */
@Data
@Entity
@Table(name = "wechat_access_toaken")
@Accessors(chain = true)
public class WechatAccessToaken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToaken; //防止调用过多 这里使用数据库进行添加

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DEFAULT TIMESTI")
    private Date cdate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date udate;



}



create table wechat_access_toaken(
id BIGINT(20) not null auto_increment,
accessToaken varchar(1000) default null,
cdate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
udate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY key (id));

```


## 2、获取access_toaken工具类


这里其实很重点了，这里获取access_toaken的时候，使用的是static方法，所以对于使用的服务层注入是不可以直接注入的，所以需要用到其他的一些简单的手段


```
@Resource
private WechatAccessToakenMapper wechatAccessToakenMapper;

@Resource
private WechatAccessToakenRepository wechatAccessToakenRepository;


//静态的方便直接调用
private static   AccessToakeUtil accessToakeUtil;

@PostConstruct
public void init() {
    accessToakeUtil = this;
    accessToakeUtil.wechatAccessToakenRepository = this.wechatAccessToakenRepository;
    accessToakeUtil.wechatAccessToakenMapper = this.wechatAccessToakenMapper;

}

```


```
package com.duodian.youhui.admin.utils;

import com.duodian.youhui.admin.constants.WeChatMessageParams;
import com.duodian.youhui.admin.utils.SdkHttpHelper;
import com.duodian.youhui.dao.db.utils.WechatAccessToakenRepository;
import com.duodian.youhui.dao.mybatis.utils.WechatAccessToakenMapper;
import com.duodian.youhui.data.http.HttpBackBean;
import com.duodian.youhui.entity.db.utils.WechatAccessToaken;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Desc: 获取access_toaken
 * @Author HealerJean
 * @Date 2018/5/25  上午11:34.
 */
@Slf4j
@Service
public class AccessToakeUtil {

    @Resource
    private WechatAccessToakenMapper wechatAccessToakenMapper;

    @Resource
    private WechatAccessToakenRepository wechatAccessToakenRepository;


    //静态的方便直接调用
    private static   AccessToakeUtil accessToakeUtil;

    @PostConstruct
    public void init() {
        accessToakeUtil = this;
        accessToakeUtil.wechatAccessToakenRepository = this.wechatAccessToakenRepository;
        accessToakeUtil.wechatAccessToakenMapper = this.wechatAccessToakenMapper;

    }

    /**
     * @Desc: 获取access_toaken
     * @Date:  2018/5/24 下午6:50.
     */

    public  static String getAccessToaken(){

        WechatAccessToaken wechatAccessToaken =accessToakeUtil.getWechatAccessToaken();

        if(wechatAccessToaken!=null){ //先判断数据库中有没有
            return wechatAccessToaken.getAccessToaken();
        }

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ WeChatMessageParams.WECHAT_APPID + "&secret=" + WeChatMessageParams.WECHAT_APPSECRET;

        HttpBackBean httpBackBean = SdkHttpHelper.handleGet(url,null,null,SdkHttpHelper.OVERTIME);

        log.info("获取 accessToken 返回结果"+httpBackBean.getResult());
        String accessToken = JSONObject.fromObject(httpBackBean.getResult()).getString("access_token");
        if(accessToken!=null){
            accessToakeUtil.saveWechatAccessToaken(accessToken); //向数据库中保存accessToaken
            return  accessToken;
        }else {
            log.error("获取accessToken失败");
        }

        return null;
    }

    private WechatAccessToaken getWechatAccessToaken(){
        return accessToakeUtil.wechatAccessToakenMapper.findOnlyToday();
    }

    private WechatAccessToaken saveWechatAccessToaken(String accessToaken){
        accessToakeUtil.wechatAccessToakenRepository.deleteAll(); //保证只有一个数据
        WechatAccessToaken wechatAccessToaken = new WechatAccessToaken();
        wechatAccessToaken.setAccessToaken(accessToaken);
        return accessToakeUtil.wechatAccessToakenRepository.save(wechatAccessToaken);
    }
}


```


## 3、查询7000秒内的access_是否存在于数据库中


```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.duodian.youhui.dao.mybatis.utils.WechatAccessToakenMapper">


    <select id="findOnlyToday"  resultType="com.duodian.youhui.entity.db.utils.WechatAccessToaken">
       <![CDATA[ SELECT  * from  wechat_access_toaken w WHERE TIMESTAMPDIFF(SECOND ,w.cdate,now())  < 7000 ]]>
    </select>

</mapper>

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
		id: 'mnENWH8gj9KQHslg',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

