package com.healerjean.proj.config.keycenter;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author HealerJean
 * @ClassName KeyCenterUtils
 * @date 2020/4/9 14:28
 * @Description 密钥中心工具类
 * 提供字符串加密和解密的功能
 * 当前实现基于Base64编码，可根据实际安全需求替换为更强的加密算法
 */
public class KeyCenterUtils {

    /**
     * 加密字符串
     * 将明文字符串转换为加密后的字符串
     *
     * @param plainText 待加密的明文字符串
     * @return 加密后的字符串
     * @throws RuntimeException 加密过程中发生异常时抛出
     */
    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        
        try {
            return Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("字符串加密失败", e);
        }
    }

    /**
     * 解密字符串
     * 将加密后的字符串转换为明文字符串
     *
     * @param encryptedText 待解密的字符串
     * @return 解密后的明文字符串
     * @throws RuntimeException 解密过程中发生异常时抛出
     */
    public static String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }
        
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("字符串解密失败", e);
        }
    }
}
