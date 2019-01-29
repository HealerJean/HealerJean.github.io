package com.duodian.youhui.admin.utils;

import net.bytebuddy.utility.JavaType;

import java.math.BigDecimal;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/29  下午4:03.
 * 类描述：比较工具类
 * 1、如果有一个参数为空，则返回 -2 ,没有匹配的类型返回2
 * 2、大小 返回 1、  = 返回0、 小于 -1
 *
 */
public class CompareUtil {


    private static final Integer INTEGER_0 = 0 ;
    private static final Long LONG_0 = 0L ;
    private static final BigDecimal BIGDECIMAL_0 = BigDecimal.ZERO;


    public static  Integer compare(Class classType , Object value, Object otherVale){
        if(value==null||otherVale==null){
            return -2;
        }
        if(classType.equals(Long.class)){

            return Long.valueOf(value.toString()).compareTo(Long.valueOf(otherVale.toString()));

        }else if(classType.equals(BigDecimal.class)){

            return new BigDecimal(value.toString()).compareTo(new BigDecimal(otherVale.toString()));

        }else if(classType.equals(Integer.class)){

            return Integer.valueOf(value.toString()).compareTo(Integer.valueOf(otherVale.toString()));
        }

        return 2 ;
    }

}
