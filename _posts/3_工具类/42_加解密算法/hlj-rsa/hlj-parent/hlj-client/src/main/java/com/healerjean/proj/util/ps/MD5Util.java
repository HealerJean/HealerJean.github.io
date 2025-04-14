package com.healerjean.proj.util.ps;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class MD5Util {


    public static String encode(String origin) {
        return encode(origin, null);
    }

    public static String encode(String origin, String charsetname) {
        if (StringUtils.isBlank(origin)) {
            return null;
        }
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null) {
                resultString = byteArrayToHexString(md.digest(origin.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(origin.getBytes(charsetname)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return resultString;
    }



    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }



    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};



}
