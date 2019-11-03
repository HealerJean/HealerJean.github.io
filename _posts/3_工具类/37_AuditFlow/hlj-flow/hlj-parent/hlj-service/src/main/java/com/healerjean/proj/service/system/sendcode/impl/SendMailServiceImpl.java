package com.healerjean.proj.service.system.sendcode.impl;

import com.healerjean.proj.data.manager.system.SysTemplateManager;
import com.healerjean.proj.data.pojo.system.SysTemplate;
import com.healerjean.proj.data.pojo.system.SysTemplateQuery;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.BusinessEnum;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.service.system.sendcode.SendMailService;
import com.healerjean.proj.service.system.sendcode.SendMsgLogService;
import com.healerjean.proj.service.system.sendcode.dto.SendMailDTO;
import com.healerjean.proj.service.system.sendcode.dto.SendMailTemplateDTO;
import com.healerjean.proj.utils.FreeMarkerUtil;
import com.healerjean.proj.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SendMailServiceImpl
 * @date 2019/5/10  12:53.
 * @Description
 */
@Service
@Slf4j
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private SysTemplateManager sysTemplateManager;

    @Autowired
    private SendMsgLogService sendMsgLogService;

    /**
     * 发送邮件
     * 1、普通邮件：必填 邮件业务类型type、 接受者邮箱receiveMails、主题subject 、内容 content
     * 2、模板邮件：必填 模板名称（这里与邮件日志的type划等号,后面会赋值给type）  接受者邮箱receiveMails、主题subject  参数map
     */
    @Override
    public void sendMail(SendMailDTO sendMailDTO, LoginUserDTO loginUserDTO) {
        log.info("=========开始发送邮件=========");
        //校验邮件内容
        validateMail(sendMailDTO);
        try {
            mailUtils.sendMail(sendMailDTO);
            sendMailDTO.setStatus(StatusEnum.生效.code);
            sendMailDTO.setMsg(StatusEnum.生效.desc);
        } catch (Exception e) {
            log.error("=========邮件发送异常=========",e);
            sendMailDTO.setStatus(StatusEnum.废弃.code);
            sendMailDTO.setMsg(e.getMessage());
            // throw new ScfException(SystemEnum.ResponseEnum.MAIL_ERROR);
        }finally {
           sendMsgLogService.saveSendMailLog(sendMailDTO);
        }
    }


    /**
     * 校验 邮件信息
     *
     * @param sendMailDTO
     * @throws Exception
     */
    public void validateMail(SendMailDTO sendMailDTO) {

        if (sendMailDTO == null) {
            log.info("=========邮件数据为 NULL=========");
            throw new ParameterErrorException("邮件数据为 NULL！");
        }


        if (sendMailDTO.getReceiveMails() == null || sendMailDTO.getReceiveMails().length == 0) {
            log.info("=========邮件接收人为空=========");
            throw new ParameterErrorException("邮件接收人为空！");
        }

        if (StringUtils.isEmpty(sendMailDTO.getSubject())) {
            log.info("=========邮件主题为空=========");
            throw new ParameterErrorException("邮件主题为空！");
        }


        //如果是模板文件
        if (sendMailDTO instanceof SendMailTemplateDTO) {
            SendMailTemplateDTO sendMailTemplateDTO = (SendMailTemplateDTO) sendMailDTO;
            if (StringUtils.isBlank(sendMailTemplateDTO.getTemplateName())) {
                log.info("=========邮件模板名字为空=========");
                throw new ParameterErrorException("邮件模板名字为空！");
            }

            SysTemplateQuery query = new SysTemplateQuery();
            query.setName(sendMailTemplateDTO.getTemplateName());
            query.setType(BusinessEnum.TemplateTypeEnum.邮件.code);
            SysTemplate template = sysTemplateManager.findByQueryContion(query);
            if(template==null){
                log.error("=========邮件模板不存在=========");
                throw new ParameterErrorException("邮件模板不存在！");
            }
            String content = FreeMarkerUtil.stringTemplate(template.getContent(),sendMailTemplateDTO.getMap());
            sendMailTemplateDTO.setContent(content);
            sendMailTemplateDTO.setType(template.getName());

        } else {
            if (StringUtils.isEmpty(sendMailDTO.getContent())) {
                log.info("=========邮件内容为空=========");
                throw new ParameterErrorException("邮件内容为空！");
            }
            if (StringUtils.isEmpty(sendMailDTO.getType())) {
                log.info("=========邮件业务为空=========");
                throw new ParameterErrorException("邮件业务为空！");
            }
        }

    }
}
