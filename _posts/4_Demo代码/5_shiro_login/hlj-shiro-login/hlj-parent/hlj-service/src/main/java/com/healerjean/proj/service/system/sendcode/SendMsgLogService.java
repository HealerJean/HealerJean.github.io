package com.healerjean.proj.service.system.sendcode;


import com.healerjean.proj.service.system.sendcode.dto.SendMailDTO;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SendMsgLogService
 * @date 2019/6/4  18:03.
 * @Description
 */
public interface SendMsgLogService {

    /**
     * 保存邮件发送日志
     */
    void saveSendMailLog(SendMailDTO sendMailDTO);

}
