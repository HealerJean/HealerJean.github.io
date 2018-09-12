package com.duodian.youhui.admin.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Desc:各种加密解密
 * @Author HealerJean
 * @Date 2018/5/23  下午3:12.
 */

public class AesUtils {


    private static final String Loginkey = "jDFBsLGArexoYZv6";
    private static final byte[] Loginiv = "4960432827928592".getBytes();;
    private static final String Loginmode = "AES/CBC/PKCS5Padding";


    public static String LoginEncrypt( String strIn)  {
        try {
            SecretKeySpec skeySpec = getKey(Loginkey);
            Cipher cipher = Cipher.getInstance(Loginmode); //"算法/模式/补码方式"
            IvParameterSpec iv = new IvParameterSpec(Loginiv);////使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(strIn.getBytes());
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            return  null;
        }
    }

    public static String LoginDecrypt(String strIn) {
        try {
            SecretKeySpec skeySpec = getKey(Loginkey);
            Cipher cipher = Cipher.getInstance(Loginmode);
            IvParameterSpec iv = new IvParameterSpec(Loginiv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(strIn);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            return null;
        }
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;
    }


    /**
     * SHA1 加密
     * @param decript
     * @return
     */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }





    public static void main(String[] args) throws Exception {

//        String Code = "1";
//        String codE = AesUtils.LoginEncrypt(Code);
//        System.out.println("原文：" + Code);
//        System.out.println("密文：" + codE);
//        System.out.println("解密：" + AesUtils.LoginDecrypt( codE));


    }



}