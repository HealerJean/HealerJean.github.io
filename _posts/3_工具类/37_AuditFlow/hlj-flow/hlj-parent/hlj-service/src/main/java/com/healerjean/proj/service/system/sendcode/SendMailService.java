package com.healerjean.proj.service.system.sendcode;


import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.service.system.sendcode.dto.SendMailDTO;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SendMailService
 * @date 2019/5/10  9:37.
 * @Description 邮件服务
 */
public interface SendMailService {

    /**
     * 发送邮件
     * 1、普通邮件：必填 邮件业务类型type、 接受者邮箱receiveMails、主题subject 、内容 content
     * 2、模板邮件：必填 模板名称（这里与邮件日志的type划等号,后面会赋值给type）  接受者邮箱receiveMails、主题subject  参数map
     */
    void sendMail(SendMailDTO sendMailDTO, LoginUserDTO loginUserDTO);
}
