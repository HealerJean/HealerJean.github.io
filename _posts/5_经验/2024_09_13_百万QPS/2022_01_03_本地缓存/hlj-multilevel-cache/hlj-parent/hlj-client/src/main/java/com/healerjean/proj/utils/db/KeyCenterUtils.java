package com.healerjean.proj.utils.db;

import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * KeyCenterUtils
 * @author zhangyujin
 * @date 2023/6/15  13:48
 */
@Service
public class KeyCenterUtils {

    /**
     * 自己写加密逻辑
     */
    public  String encrypt(String src) {
        try {
            return Base64.getEncoder().encodeToString(src.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 自己写解密逻辑
     */
    public  String decrypt(String src) {
        try {
            byte[] asBytes = Base64.getDecoder().decode(src);
            return new String(asBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

}
