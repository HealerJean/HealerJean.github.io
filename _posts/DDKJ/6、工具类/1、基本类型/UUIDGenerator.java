package com.duodian.admore.core.helper;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Created by j.sh on 2015/6/30.
 */
public class UUIDGenerator {

    private UUIDGenerator(){
    }

    /**
     * 唯一uuid
     * @return
     */
    public static String generate(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 随机字符数字大小写混合字符串，不保证唯一
     * @param count
     * @return
     */
    public static String generate(int count){
        return RandomStringUtils.random(count,true,true);
    }

    /**
     * 随机字符大小写混合字符串，不保证唯一
     * @param count
     * @return
     */
    public static String generateAlpha(int count){
        return RandomStringUtils.randomAlphabetic(count);
    }

    public static String generateYm(int count){
        return DateHelper.convertDate2String(new Date(),DateHelper.YYYYMM) + UUIDGenerator.generate(count);
    }

    public static String generateDateYm(Date point,int count){
        return DateHelper.convertDate2String(point,DateHelper.YYYYMM) + UUIDGenerator.generate(count);
    }

    /**
     * 获取一个hexString
     * 当前毫秒数乘以1000再加上1000以内随机数的16进制。不保证唯一
     * 用于appId生成
     * @return
     */
    public static String generateHexString(){
        return Long.toHexString(new Date().getTime() * 1000 + new Double(Math.random() * 1000).longValue());
    }

    public static void main(String[] args) {
        System.out.println(generateAlpha(16));
    }
}
