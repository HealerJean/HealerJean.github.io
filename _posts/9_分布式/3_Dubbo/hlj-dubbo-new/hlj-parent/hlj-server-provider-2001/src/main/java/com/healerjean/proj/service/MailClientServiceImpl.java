package com.healerjean.proj.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author HealerJean
 * @ClassName MailClientServiceImpl
 * @date 2020-11-03  18:09.
 * @Description
 */
@Slf4j
@DubboService(version = "0.0.1", group = "163")
public class MailClientServiceImpl  implements MailClientService{

    /**
     * 发送邮件
     * @param msg
     */
    @Override
    public void sentMail(String msg) {
         log.info("发送的邮件信息为：{}", msg);
    }
}
