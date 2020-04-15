package com.hlj.util.Z025_utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author HealerJean
 * @ClassName EqualsUtils
 * @date 2019-08-04  19:47.
 * @Description
 */
public class EqualsUtils {

    /**
     * 全部相等
     */
    public final static boolean allEquals(String str, String... strs) {
        Long count = Arrays.stream(strs).filter(item -> StringUtils.equals(str, item)).count();
        if (Long.valueOf(strs.length).equals(count)) {
            return true;
        }
        return false;
    }

    /**
     * 存在相等的
     */
    public final static boolean existEquals(String str, String... strs) {
        Long count = Arrays.stream(strs).filter(item -> StringUtils.equals(str, item)).count();
        if (count > 0){
            return true;
        }
        return false;
    }

    public final static boolean allEquals( BigDecimal decimal, BigDecimal... decimals) {
        Long count = Arrays.stream(decimals).filter(item -> decimal.compareTo(item) == 0).count();
        if (Long.valueOf(decimals.length).equals(count)) {
            return true;
        }
        return false;
    }
    public final static boolean existEquals( BigDecimal decimal, BigDecimal... decimals) {
        Long count = Arrays.stream(decimals).filter(item -> decimal.compareTo(item) == 0).count();
        if (count > 0){
            return true;
        }
        return false;
    }


    public final static boolean allEquals( Integer integer, Integer... integers) {
        Long count = Arrays.stream(integers).filter(item -> integer.compareTo(item) == 0).count();
        if (Long.valueOf(integers.length).equals(count)) {
            return true;
        }
        return false;
    }
    public final static boolean existEquals( Integer integer, Integer... integers) {
        Long count = Arrays.stream(integers).filter(item -> integer.compareTo(item) == 0).count();
        if (count > 0){
            return true;
        }
        return false;
    }


}
