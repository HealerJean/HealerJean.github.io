package com.fintech.scf.utils;

import com.fintech.scf.service.system.sendMsg.dto.SendMailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName MailUtils
 * @Date 2019/8/13  17:38.
 * @Description 发送邮件工具类
 */
@Service
@Slf4j
public class MailUtils {

    @Value("${mail.transport.protocol}")
    private String mailTransPortProtocol;

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;

    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${mail.fromName}")
    private String fromName;


    /**
     * 发送邮件
     */
    public void sendMail(SendMailDTO sendMailDTO) {
        log.info("=========发送邮件开始=========");
        try {
            sendMailDTO.setFromMail(mailFrom);
            Session session = getSession();
            MimeMessage message = new MimeMessage(session);
            createMimeMessage(message, sendMailDTO);
            Transport transport = session.getTransport();
            transport.connect(mailSmtpHost, mailFrom, mailPassword);
            // transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            log.info("=========发送邮件内容结束=========");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private Session getSession() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", mailTransPortProtocol);
        props.setProperty("mail.smtp.host", mailSmtpHost);
        props.setProperty("mail.smtp.auth", mailSmtpAuth);
        return Session.getInstance(props);
    }


    public MimeMessage createMimeMessage(MimeMessage mimeMessage, SendMailDTO sendMailDTO) throws MessagingException, UnsupportedEncodingException {
        log.info("=========创建邮件内容开始=========");
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        //个人发邮箱必须指明来源和小米发送邮件不同，小米发送不需要指定
        messageHelper.setFrom(sendMailDTO.getFromMail(), fromName);
        messageHelper.setTo(sendMailDTO.getReceiveMails());
        messageHelper.setSubject(sendMailDTO.getSubject());
        messageHelper.setText(sendMailDTO.getContent(), true);


        /**
         * 发送附件，添加到附件
         */
        Map<String, Object> attachment = sendMailDTO.getAttachment();
        if (attachment != null) {
            for (String key : attachment.keySet()) {
                messageHelper.addAttachment(key, (FileSystemResource) attachment.get(key));
            }
        }

        /**
         * 链接中的图片
         */
        Map<String, Object> imageAttachment = sendMailDTO.getImageAttachment();
        if (imageAttachment != null) {
            for (String key : imageAttachment.keySet()) {
                messageHelper.addInline(key, (FileSystemResource) imageAttachment.get(key));
            }
        }

        return mimeMessage;
    }


}
