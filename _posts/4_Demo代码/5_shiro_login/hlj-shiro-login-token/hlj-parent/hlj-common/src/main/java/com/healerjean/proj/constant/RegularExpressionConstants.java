package com.healerjean.proj.constant;

/**
 * @ClassName RegularExpressionConstants
 * @Author TD
 * @Date 2019/5/22 10:48
 * @Description 正则表达式匹配
 */
public class RegularExpressionConstants {

    /**
     * 用户名-正则表达式：英文字母开头，包含英文字母或数字或特殊字符(. _ -)，长度4-32位
     */
    public static final String USERNAME = "^[a-zA-Z]{1}([a-zA-Z0-9]|[._-]){3,31}$";
    /**
     * 邮箱-正则表达式
     */
    public static final String EMAIL = "^[A-Za-z0-9\\u4e00-\\u9fa5]{1}[A-Za-z0-9\\u4e00-\\u9fa5\\._-]+[A-Za-z0-9\\u4e00-\\u9fa5]{1}@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 手机-正则表达式
     */
    public static final String TELEPHONE = "^(\\s?)|([1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8})$";
    /**
     * 密码-正则表达式
     */
    public static final String PASSWORD = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$";

}
