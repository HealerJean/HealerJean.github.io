package com.duodian.youhui.admin.utils;


import org.apache.commons.codec.binary.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public final class EmptyUtils<T> {

   private static final Integer INTEGER_0 = 0 ;
   private static final Long LONG_0 = 0L ;
   private static final BigDecimal BIGDECIMAL_0 = BigDecimal.ZERO;


    public final static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
    public final static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public final static boolean isEmpty(String[] array) {
        if(array==null||array.length==0){
            return true ;
        }else {
            for (int i = 0; i <array.length ; i++) {
                if(array[i]!=null){
                    return false ;
                }
            }
            return true ;
        }
    }

    public final static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public final static boolean isNullAnd0(BigDecimal decimal){
        return    (decimal==null||BIGDECIMAL_0.compareTo(decimal)==0)?true:false;
    }

    public final static boolean isNullAnd0(Integer integer){
        return    (integer==null||INTEGER_0.compareTo(integer)==0)?true:false;
    }
    public final static boolean isNullAnd0(Long l){
        return    (l==null||LONG_0.compareTo(l)==0)?true:false;
    }

    public final static  boolean isNotNullAndEquals(String str,String existStr){
            return (str!=null&&existStr!=null&& StringUtils.equals(existStr, str))?true:false;
    }
    public final static  boolean isNotNullAndEquals(BigDecimal decimal,BigDecimal existDecimal){
        return (decimal!=null&&existDecimal!=null&& existDecimal.compareTo(decimal)==0)?true:false;
    }
    public final static  boolean isNotNullAndEquals(Integer integer,Integer existInteger){
        return (integer!=null&&existInteger!=null&& existInteger.compareTo(integer)==0)?true:false;
    }
    public final static  boolean isNotNullAndEquals(Long l,Long existLong){
        return (l!=null&&existLong!=null&& existLong.compareTo(l)==0)?true:false;
    }




}
