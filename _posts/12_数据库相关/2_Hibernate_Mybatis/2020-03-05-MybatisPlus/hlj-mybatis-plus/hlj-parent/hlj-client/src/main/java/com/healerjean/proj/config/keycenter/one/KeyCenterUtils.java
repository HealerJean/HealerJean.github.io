package com.healerjean.proj.config.keycenter.one;

import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * @author HealerJean
 * @ClassName AES
 * @date 2020/4/9  14:28.
 * @Description
 */
@Service
public class KeyCenterUtils {

    /**
     * 自己写加密逻辑
     */
    public  String encrypt(String src) {
        try {
            String result = Base64.getEncoder().encodeToString(src.getBytes("UTF-8"));
            return result;
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
            String result = new String(asBytes, "UTF-8");
            return result;
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

}
