package com.healerjean.proj.controller;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PatternRegularUtils {

    /** 用户相关 */
    /** 用户名-正则表达式：英文字母开头，包含英文字母或数字或特殊字符(. _ -)，长度4-32位*/
    public static final String UserName = "^[a-zA-Z]{1}([a-zA-Z0-9]|[._-]){3,31}$";
    /** 邮箱-正则表达式 */
    public static final String Email = "^[A-Za-z0-9\\u4e00-\\u9fa5]{1}[A-Za-z0-9\\u4e00-\\u9fa5\\._-]+[A-Za-z0-9\\u4e00-\\u9fa5]{1}@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /** 手机-正则表达式 */
    public static final String Telephone = "^(\\s?)|([1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8})$";
    /** 支付密码规则-正则表达式 */
    public static final String PayPassWord = "\\d{6}";
    /** 密码规则-正则表达式 */
    public static final String PassWord = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$";
    /** QQ-正则表达式 */
    public static final String QQ = "[1-9][0-9]{4,14}";
    /** 统一社会信用代码-正则表达式 */
    public static final String SocialCreditCode = "^([159Y]{1})([1239]{1})([0-9ABCDEFGHJKLMNPQRTUWXY]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{9})([0-90-9ABCDEFGHJKLMNPQRTUWXY])$";


    /** 字符相关 */
    /** 全为英文 */
    public static final String ENGLISH = "[a-zA-Z]+";
    /** 全为英文 大写*/
    public static final String ENGLISH_UPPER = "^[A-Z]+$";
    /** 全为英文 小写*/
    public static final String ENGLISH_lLOW = "^[a-z]+$";
    /** 含有英文 */
    public static final String CONTAIN_ENGLISH = ".*[a-zA-z].*";
    /** 全为数字 */
    public static final String NUMBER = "[0-9]+";
    /** 含有数字 */
    public static final String CONTAIN_NUMBER  = ".*[0-9].*";
    /** 英文、数字 */
    public static final String ENGLISH_NUMBER = "[a-zA-Z0-9]+";
    /** 英文、数字、下划线 */
    public static final String ENGLISH_NUMBER_ = "^\\w+$";
    /** 全为中文  */
    public static final String CHINESE = "[\\u4e00-\\u9fa5]+";
    /** 中文、英文、数字 */
    public static final String CHINESE_ENGLISH_NUMBER = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";
    /** 中文、英文、数字、下划线*/
    public static final String CHINESE_ENGLISH_NUMBER_ = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";





    /**
     * 姓名（包含少数名族）
     */
    public static boolean checkChineseName(String name) {
        if(StringUtils.isBlank(name)){
            return false;
        }
        //少数名族 带点时前后汉字是[2，8]个
        if (name.contains(".") && name.matches("^[\\u4e00-\\u9fa5]+[.][\\u4e00-\\u9fa5]+$")) {
            return true;
        } else if (name.matches("^[\\u4e00-\\u9fa5]+$")) {
            return true;
        }
        return false;
    }



    /**
     * 判断密码强度
     */
    public static int checkPasswordStrength(String password) {
        if (password.length() < 6) {
            return 1;
        } else {
            String strongPattern = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\\d!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
            String inPattern = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
            String weakPattern = "^(?:\\d+|[a-zA-Z]+|[!@#$%^&*]+)$";
            if (Pattern.matches(strongPattern, password)) {
                //强
                return 3;
            } else if (Pattern.matches(inPattern, password)) {
                //中
                return 2;
            } else {
                //弱
                return 1;
            }
        }
    }



}
