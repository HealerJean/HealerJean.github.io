package com.healerjean.proj.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * RandomUtils
 *
 * @author zhangyujin
 * @date 2023/7/28
 */
public final class RandomUtils {


    /**
     * 唯一uuid
     */
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 指定长度字符串 => 字符(包含大小写) + 数字
     */
    public static String generate(int count) {
        return RandomStringUtils.random(count, true, true);
    }

    /**
     * 指定长度字符串 => 字符(包含大小写) 无数字
     */
    public static String generateAlpha(int count) {
        return RandomStringUtils.random(count, true, false);
    }

    /**
     * 指定长度字符串 => 数字
     */
    public static String generateDigitString(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

}
