package com.hlj.util.Z025_utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName RandomNumberUtils
 * @date 2020/4/15  12:30.
 * @Description 随机数工具
 */
public class RandomNumberUtils {


    /**
     * 唯一uuid
     */
    public static String generate(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 指定长度字符串 => 字符(包含大小写) + 数字
     */
    public static String generate(int count){
        return RandomStringUtils.random(count,true,true);
    }

    /**
     * 指定长度字符串 => 字符(包含大小写)
     */
    public static String generateAlpha(int count){
        return RandomStringUtils.random(count, true, false);
    }

    /**
     * 指定长度字符串 => 数字
     */
    public static String generateDigitString(int length){
        return RandomStringUtils.randomNumeric(length);
    }



}
