package com.duodian.admore.core.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bianjsh on 2014/12/1.
 */
public class PasswordHelper {
    private PasswordHelper(){
    }

    private static final String algorithm = "SHA";

    /*
     */
    public static String encodePassword(String password){
        byte[] unEncode = null;
        try {
            unEncode = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("it's impossible!");
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("it's impossible!");
        }
        md.reset();
        md.update(unEncode);

        byte[] encodedPassword = md.digest();
        StringBuilder buf = new StringBuilder();
        for(byte b : encodedPassword){
            if ((b & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(b & 0xff, 16));
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        System.out.println(PasswordHelper.encodePassword("zhangddkj123"));
    }
}
