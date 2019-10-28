package com.healerjean.proj.utils.encry;


import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

/**
 * MD5加解密
 */
public class Md5Encrypt {

    private static final char HEXCHAR[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String digest(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("utf-8"));
            byte[] result = messageDigest.digest();
            return Md5Encrypt.byteToHex(result);
        } catch (Exception e) {

        }

        return null;
    }

    static String byteToHex(byte b[]) {
        int len = b.length;
        StringBuffer s = new StringBuffer();

        for (int i = 0; i < len; i++) {
            int c = ((int) b[i]) & 0xff;

            s.append(HEXCHAR[c >> 4 & 0xf]);
            s.append(HEXCHAR[c & 0xf]);
        }

        return s.toString();
    }

    /**
     * md5加密并转换为大写
     *
     * @param data
     * @return
     */
    public static String javaMD5Upper(String data) {

        try {
            return DigestUtils.md5Hex(data).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5加密
     *
     * @param data
     * @return
     */
    public static String javaMD5(String data) {

        try {
            return DigestUtils.md5Hex(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5Lower(String source, String algorithm) {
        if (source == null)
            return null;

        MessageDigest digest = null;
        try {
            if (algorithm == null) {
                digest = MessageDigest.getInstance("MD5");
            } else
                digest = MessageDigest.getInstance(algorithm);

            byte[] result = digest.digest(source.getBytes("UTF-8"));

            StringBuffer buffer = new StringBuffer();
            buffer.setLength(0);
            int length = result.length;
            for (int k = 0; k < length; ++k) {
                String hex = Integer.toHexString(result[k] & 0xFF);
                if (hex.length() < 2)
                    hex = "0" + hex;

                buffer.append(hex);
            }
            return buffer.toString();
        } catch (Exception e) {
            // logger.error("Get MD5 failure", e);.
            e.printStackTrace();
        }
        return null;
    }


}
