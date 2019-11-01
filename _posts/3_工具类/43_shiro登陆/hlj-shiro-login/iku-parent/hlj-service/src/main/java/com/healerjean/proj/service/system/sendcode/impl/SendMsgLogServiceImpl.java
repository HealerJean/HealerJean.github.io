package com.healerjean.proj.service.system.sendcode.impl;

import com.healerjean.proj.data.manager.system.SysEmailLogManager;
import com.healerjean.proj.data.pojo.system.SysEmailLog;
import com.healerjean.proj.service.system.sendcode.SendMsgLogService;
import com.healerjean.proj.service.system.sendcode.dto.SendMailDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SendMsgLogServiceImpl
 * @date 2019/6/4  18:04.
 * @Description 发现消息日志记录
 */
@Service
@Slf4j
public class SendMsgLogServiceImpl implements SendMsgLogService {


    @Autowired
    private SysEmailLogManager SysEmailLogManager;


    /**
     * 保存邮件发送日志
     *
     * @param sendMailDTO
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveSendMailLog(SendMailDTO sendMailDTO) {
        SysEmailLog sysEmailLog = new SysEmailLog();
        sysEmailLog.setType(sendMailDTO.getType());
        sysEmailLog.setSubject(sendMailDTO.getSubject());
        sysEmailLog.setSendEmail(sendMailDTO.getFromMail());
        sysEmailLog.setReceiveMails(StringUtils.join(sendMailDTO.getReceiveMails(), ","));
        sysEmailLog.setContent(sendMailDTO.getContent());
        sysEmailLog.setStatus(sendMailDTO.getStatus());
        sysEmailLog.setMsg(sendMailDTO.getMsg());
        SysEmailLogManager.save(sysEmailLog);
    }


}
