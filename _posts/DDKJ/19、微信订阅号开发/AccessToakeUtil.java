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
