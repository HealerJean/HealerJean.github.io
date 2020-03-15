package com.healerjean.proj.service.system.verifyCode.impl;

import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.dto.user.VerifyCodeDTO;
import com.healerjean.proj.enums.BusinessEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.service.system.cache.CacheService;
import com.healerjean.proj.service.system.security.SecurityService;
import com.healerjean.proj.service.system.sendcode.SendMailService;
import com.healerjean.proj.service.system.sendcode.dto.SendMailTemplateDTO;
import com.healerjean.proj.service.system.verifyCode.VerifyCodeService;
import com.healerjean.proj.utils.validate.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName VerifyCodeServiceImpl
 * @Author TD
 * @Date 2019/6/3 14:37
 * @Description 发送验证码的实现类
 */
@Service
@Slf4j
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private static final int WIDTH = 96;
    private static final int HEIGHT = 33;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private SecurityService securityService;

    /**
     * 验证码有效时间300秒
     */
    public static final int CAPTCHA_EXPIRE = 300;

    /**
     * 生成验证码
     */
    @Override
    public byte[] generateCaptcha(VerifyCodeDTO verifyCodeDTO, LoginUserDTO loginUserDTO) {
        String validate = ValidateUtils.validate(verifyCodeDTO);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.error("生成验证码参数错误，错误信息：{}", validate);
            throw new ParameterErrorException(validate);
        }
        //根据type来判断是那种验证码
        BusinessEnum.VerifyCodeTypeEnum type = verifyCodeDTO.getType();
        String verifyCode = null;
        String key = null;
        String ip = verifyCodeDTO.getIp();
        byte[] buf = null;
        switch (type) {
            case 图片验证码: {
                char[] rands = generateCheckCode(4);
                verifyCode = new String(rands);
                BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = image.getGraphics();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                drawBackground(graphics);
                drawRands(graphics, rands);
                graphics.dispose();
                try {
                    ImageIO.write(image, "JPEG", bos);
                } catch (IOException e) {
                    throw new BusinessException("图片验证码生成失败");
                }
                buf = bos.toByteArray();
                break;
            }

            case 注册邮箱验证码: {
                //发送注册校验邮件
                char[] rands = generateCheckCode(6);
                verifyCode = new String(rands);
                key = verifyCodeDTO.getEmail();
                //一天只能发10次
                securityService.checkMsgSecurit(ip, key, type.code);
                SendMailTemplateDTO sendMailDTO = new SendMailTemplateDTO();
                sendMailDTO.setTemplateName(BusinessEnum.TempleNameEnum.邮箱验证.code);
                sendMailDTO.setReceiveMails(new String[]{key});
                sendMailDTO.setSubject(BusinessEnum.TempleNameEnum.邮箱验证.desc);
                Map map = new HashMap(2);
                map.put(CommonConstants.TEMPLE_VERIFY_CODE, verifyCode);
                sendMailDTO.setMap(map);
                sendMailService.sendMail(sendMailDTO, loginUserDTO);
                //一天只能发10次 累加
                securityService.doMsgSecurit(ip, key, type.code);
                break;
            }
            case 找回密码邮箱验证码: {
                //发送注册校验邮件
                char[] rands = generateCheckCode(6);
                verifyCode = new String(rands);
                key = verifyCodeDTO.getEmail();
                securityService.checkMsgSecurit(ip, key, type.code);
                SendMailTemplateDTO sendMailDTO = new SendMailTemplateDTO();
                sendMailDTO.setTemplateName(BusinessEnum.TempleNameEnum.找回密码邮箱验证.code);
                sendMailDTO.setReceiveMails(new String[]{key});
                sendMailDTO.setSubject(BusinessEnum.TempleNameEnum.找回密码邮箱验证.desc);
                Map map = new HashMap(2);
                map.put(CommonConstants.TEMPLE_VERIFY_CODE, verifyCode);
                sendMailDTO.setMap(map);
                sendMailService.sendMail(sendMailDTO, loginUserDTO);
                securityService.doMsgSecurit(ip, key, type.code);
                break;
            }
            default:
                throw new ParameterErrorException("验证码类型不存在");
        }
        //验证码需要三要素，clientID，sid，IP
        StringBuffer sb = new StringBuffer();
        sb.append(CommonConstants.REDIS_HLJ).append(":")
                .append(verifyCodeDTO.getSystemCode()).append(":")
                .append(CommonConstants.REDIS_VERIFY_CODE).append(":")
                .append(verifyCodeDTO.getType().code).append(":")
                .append(verifyCodeDTO.getIp()).append("_")
                .append(verifyCodeDTO.getRandomId());
        if (StringUtils.isNotBlank(key)) {
            sb.append("_").append(key);
        }
        cacheService.set(sb.toString(), verifyCode, CAPTCHA_EXPIRE, TimeUnit.SECONDS);
        log.info("验证码产生成功，ip：{}；类型：{}，值：{}", verifyCodeDTO.getIp(), verifyCodeDTO.getType().code, verifyCode);
        return buf;
    }


    /**
     * 校验验证码，图片验证码校验一次失效一次
     */
    @Override
    public boolean verify(VerifyCodeDTO verifyCodeDTO, String verifyCode) {
        if (StringUtils.isBlank(verifyCode)) {
            return false;
        }
        BusinessEnum.VerifyCodeTypeEnum type = verifyCodeDTO.getType();
        StringBuffer sb = new StringBuffer();
        String key = null;
        switch (type) {
            case 图片验证码: {
                break;
            }
            case 注册邮箱验证码: {
                key = verifyCodeDTO.getEmail();
                break;
            }
            case 找回密码邮箱验证码: {
                key = verifyCodeDTO.getEmail();
                break;
            }
            default:
                break;
        }
        sb.append(CommonConstants.REDIS_HLJ).append(":")
                .append(verifyCodeDTO.getSystemCode()).append(":")
                .append(CommonConstants.REDIS_VERIFY_CODE).append(":")
                .append(verifyCodeDTO.getType().code).append(":")
                .append(verifyCodeDTO.getIp()).append("_")
                .append(verifyCodeDTO.getRandomId());
        if (StringUtils.isNotBlank(key)) {
            sb.append("_").append(key);
        }
        String string = sb.toString();
        Object o = cacheService.get(string);
        if (BusinessEnum.VerifyCodeTypeEnum.图片验证码.code.equals(type.code)) {
            cacheService.delete(string);
        }
        if (o == null) {
            return false;
        }
        return o.toString().equalsIgnoreCase(verifyCode);
    }


    /**
     * 删除验证码
     */
    @Override
    public void deleteVerifyCode(VerifyCodeDTO verifyCodeDTO) {
        BusinessEnum.VerifyCodeTypeEnum type = verifyCodeDTO.getType();
        StringBuffer sb = new StringBuffer();
        String key = null;
        switch (type) {
            case 图片验证码: {
                break;
            }
            case 注册邮箱验证码: {
                key = verifyCodeDTO.getEmail();
                break;
            }
            case 找回密码邮箱验证码: {
                key = verifyCodeDTO.getEmail();
                break;
            }
            default:
                break;
        }
        sb.append(CommonConstants.REDIS_HLJ).append(":")
                .append(verifyCodeDTO.getSystemCode()).append(":")
                .append(CommonConstants.REDIS_VERIFY_CODE).append(":")
                .append(verifyCodeDTO.getType().code).append(":")
                .append(verifyCodeDTO.getIp()).append("_")
                .append(verifyCodeDTO.getRandomId());
        if (StringUtils.isNotBlank(key)) {
            sb.append("_").append(key);
        }
        String string = sb.toString();
        cacheService.delete(string);
    }


    private char[] generateCheckCode(int count) {

        String chars = "123456789qwertyupasdfghjkzxcvbnm";
        char[] rands = new char[count];
        for (int i = 0; i < count; i++) {
            SecureRandom sr = new SecureRandom();
            int rand = (int) (sr.nextDouble() * chars.length());
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

    private void drawRands(Graphics g, char[] rands) {

        g.setColor(Color.BLACK);
        g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 20));
        g.drawString(String.valueOf(rands[0]), 3, 17);
        g.drawString(String.valueOf(rands[1]), 20, 15);
        g.drawString(String.valueOf(rands[2]), 40, 20);
        g.drawString(String.valueOf(rands[3]), 60, 16);
    }

    private void drawBackground(Graphics g) {

        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < 100; i++) {
            SecureRandom sr = new SecureRandom();
            int x = (int) (sr.nextDouble() * WIDTH);
            int y = (int) (sr.nextDouble() * HEIGHT);
            int red = (int) (sr.nextDouble() * 255);
            int green = (int) (sr.nextDouble() * 255);
            int blue = (int) (sr.nextDouble() * 255);
            g.setColor(new Color(red, green, blue));
            g.drawOval(x, y, 1, 0);
        }
    }

}
