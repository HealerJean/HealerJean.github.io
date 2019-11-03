package com.healerjean.proj.utils.encry;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA RSA加解密方法
 */
public class RSACodeUtil {

    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {

        byte[] keyBytes = Base64.decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 使用keystore对明文进行加密
     *
     * @param publicKeyString 公钥文件路径
     * @param plainText       明文
     * @return
     */
    public static String encrypt(String plainText, String publicKeyString) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyString));
        byte[] enBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.encode(enBytes);
    }

    /**
     * 使用私钥对明文密文进行解密
     *
     * @param privateKey
     * @param enStr
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String enStr) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] deBytes = cipher.doFinal(Base64.decode(enStr));
        return new String(deBytes, "UTF-8");
    }



}
