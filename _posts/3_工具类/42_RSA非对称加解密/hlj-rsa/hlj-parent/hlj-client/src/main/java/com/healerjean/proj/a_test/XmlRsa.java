package com.healerjean.proj.a_test;

import java.util.Date;

import com.healerjean.proj.a_test.dto.BasicInfo;
import com.healerjean.proj.a_test.dto.BussinssData;
import com.healerjean.proj.a_test.dto.MessageDTO;
import com.healerjean.proj.util.rsa.RSAUtils;
import com.healerjean.proj.util.xml.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName XmlRsa
 * @date 2019-10-28  23:12.
 * @Description 公钥加密，解密私钥
 */
@Slf4j
public class XmlRsa {


    /**
     * 创建公钥私钥
     */
    @Test
    public void createRSA() {
        SecureRandom random = new SecureRandom();
        //这个数字不可以随便输入
        RSAUtils.getInstance().generatorKeyPair(4096, random);
    }

    @Test
    public void clientA() {

        String aPrivaetKey = "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";
        String dealPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAloJZU18QoWuI+j4LhLNBKJyiDU++dQodqOG6+l+05gYgUdjYNQwHalSW4Sx+koidwbp7Wj9WtnyuH/C1Rp0gsSiVi8nba70ztXsn+LoD2A6PD2vqS/Weyuwhqwi5xm5P4r2gAi2gT6H9tolesxamIBqdRmiv5KwGROJnsnwWKticARY+Q10nqKE3u6ZGGWjfPENLfb0aVUb4DkZOzslo7roVIF6/A+iBfAM/vV/9hfFd3BnU1ZS6DPBJ5yLgvlX/mLhTh8+if2apl2WV3UI6XPjzzSi8q4f0IWLw170AOx4dq7z7orH2uezfOeZqsCMf0SNp5675g1yuO4Vf2Tn34S0+88HVTzQrhHw1QgBgYXVkSumyTC7DmQ9nwQXBD1d0dltwZKmEq0h+zYgeKJ9eVSBjrwIx79JZ7MamOeePkh9k/tWKYOS+tk4gQ/htaBl2XpWtKeM+ye8B+GQYHayOSQ4QS8RKXaajDjEFAwXHSSu4bI38sJW61GMBjL2LfIR9z9nQyMOR0WJEKBr5ta8z+GIRwRGP+GQXaWyExSGP2g6DaoHy3C4dxFCvJ0X1eJhWi2kg4VettpMs60pjTfpwlQrzFAy7YAkoK4guCpaVRCwhJZYIlUkWiaE4vu47ZPd5707faWu2LWZOVuLLJWVmdLAhsJFbmSAy3G9OicbZbbcCAwEAAQ==";


        RSAUtils rsaUtils = RSAUtils.getInstance();
        BussinssData bussinssData = new BussinssData();
        bussinssData.setCompanyName("企业名称");
        bussinssData.setOrgCode("123456789");
        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setTransCode(UUID.randomUUID().toString().replace("-", ""));
        basicInfo.setTransType("T0001");
        basicInfo.setTransTime(new Date());

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBasicInfo(basicInfo);
        messageDTO.setBussinssData(bussinssData);


        String xmlOrigin = XmlUtils.toXml(messageDTO);
        log.info("数据原文：【 {} 】", xmlOrigin);

        String signedXmlOrigin = XmlUtils.toXml(bussinssData);
        log.info("签名原文：【 {} 】", signedXmlOrigin);

        // 1、 私钥生成签名（只对业务数据进行签名）
        String sign = rsaUtils.signByPriKey(signedXmlOrigin, aPrivaetKey, RSAUtils.SHA1withRSA);
        log.info("签名：【 {} 】", sign);

        basicInfo.setSignedMsg(sign);
        String encryptOriginXml = XmlUtils.toXml(messageDTO);
        log.info("加密原文：【 {} 】", encryptOriginXml);

        // 2、公钥加密
        String encryptContent = rsaUtils.encryptByPubKey(encryptOriginXml, dealPublicKey);
        log.info("加密报文：【 {} 】", encryptContent);

        //模拟调用别人接口
        String resEncryptContent = clientB(encryptContent);
        log.info("响应加密报文：【 {} 】", resEncryptContent);

        // 1、解密
        String decryptXml = rsaUtils.decryptByPriKey(resEncryptContent, aPrivaetKey);
        log.info("响应解密报文：【 {} 】", decryptXml);

        // 验证签名
        messageDTO = XmlUtils.toObject(decryptXml, MessageDTO.class);
        sign = messageDTO.getBasicInfo().getSignedMsg();
        log.info("响应签名：【 {} 】", sign);

        bussinssData = messageDTO.getBussinssData();
        signedXmlOrigin = XmlUtils.toXml(bussinssData);
        log.info("响应签名原文：【 {} 】", signedXmlOrigin);
        boolean check = rsaUtils.verifySignByPubKey(signedXmlOrigin, sign, dealPublicKey, RSAUtils.SHA1withRSA);
        if (!check) {
            throw new RuntimeException("响应签名验证失败");
        }
        log.info("----------success----------------");


    }


    public String clientB(String encryptContent) {
        String aPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA0N6hPKO+05+ImOazyelOCe5d6UzahFNWD3BW3qBJDOaWgiMvT6G3uQuTEpprKZWP48/vZJ1uMYLXJhxGa5iMoFSB8JS+Mlc5WAnJEPwpop3Env9D5AyG5gLjtEa0KfKRph6R/PLttlN1n6UVnM/s0PpZY2MYO4RDd/xEAkgfStIs5lvfu3SehYDlfiNO/L5zL0u4FvM9mvNXEiEWK9PH3Bm9D72vebHhWGzJGfMed6OK96h47KcAiRY6hktNKEXwM5DzXF3jSRwnFl5ipmy/xkL4ffqlz0EsPvYUCumzOWmzED4vJEfW3brBVrZgVAi/KoC6OeVyA0MvD8AqoklpBLtZlk88QZPvqYy/g00U9DsCDzeTZ/CNdelFDtbYZOB3x93dnKn+N664t58cp/6X25CE0MCoJvWHS+tNFVVeZ7c3YB1Lp5NHmKFPuH0CfNcR3Wb462qm1nHMd6/LSMwDORb8ex0SR34QUpREhhhm1/xM/x1P9ZLWxs+3B1rSn5EGp96br3sJEOgsiK75o8Eq3ST7HuoQcMCp0nYVIxu8RkkJtCENBDQSsdQz5sGGBNbHWypXfVTz+U7Y6ybeCLhc3XKGMx6AWU5D0vRbAOoegnvKjChYKQfDgY4i39imMlvVQBeyibu361qbUpO9XVcPGEwWBiWiiMFVIrLa1kBqmgUCAwEAAQ==";
        String dealPrivaetKey = "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";

        log.info("----------------------------------------------------------------------");
        log.info("接收加密报文：【 {} 】", encryptContent);

        //1、私钥解密
        RSAUtils rsaUtils = RSAUtils.getInstance();
        String decryptXml = rsaUtils.decryptByPriKey(encryptContent, dealPrivaetKey);
        log.info("解密报文：【 {} 】", decryptXml);

        // 验证签名
        MessageDTO messageDTO = XmlUtils.toObject(decryptXml, MessageDTO.class);
        String sign = messageDTO.getBasicInfo().getSignedMsg();
        log.info("签名：【 {} 】", sign);
        BussinssData bussinssData = messageDTO.getBussinssData();
        String signedXmlOrigin = XmlUtils.toXml(bussinssData);
        log.info("签名原文：【 {} 】", signedXmlOrigin);
        boolean check = rsaUtils.verifySignByPubKey(signedXmlOrigin, sign, aPublicKey, RSAUtils.SHA1withRSA);
        if (!check) {
            throw new RuntimeException("验签失败");
        }

        //业务处理
        BasicInfo basicInfo = messageDTO.getBasicInfo();
        basicInfo.setRetCode("200");
        basicInfo.setRetMsg("Success");
        bussinssData.setCompanyId(1L);
        signedXmlOrigin = XmlUtils.toXml(bussinssData);

        // 1、 私钥生成签名（只对业务数据进行签名）
        sign = rsaUtils.signByPriKey(signedXmlOrigin, dealPrivaetKey, RSAUtils.SHA1withRSA);
        log.info("返回签名：【 {} 】", sign);
        basicInfo.setSignedMsg(sign);
        String encryptOriginXml = XmlUtils.toXml(messageDTO);
        log.info("返回加密原文：【 {} 】", encryptOriginXml);

        // 2、公钥加密
        encryptContent = rsaUtils.encryptByPubKey(encryptOriginXml, aPublicKey);
        log.info("返回加密报文：【 {} 】", encryptContent);

        log.info("----------------------------------------------------------------------");
        return encryptContent;
    }


}
