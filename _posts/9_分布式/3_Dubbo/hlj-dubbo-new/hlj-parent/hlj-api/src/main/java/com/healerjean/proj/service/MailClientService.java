package com.healerjean.proj.service;

/**
 * @author HealerJean
 * @ClassName MailClientService
 * @date 2020-11-03  18:09.
 * @Description
 */
public interface MailClientService {

    /**
     * 发送邮件
     * @param msg
     */
    void sentMail(String msg);
}
