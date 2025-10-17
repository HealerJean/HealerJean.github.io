package com.healerjean.proj.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * SampleUtils
 *
 * @author zhangyujin
 * @date 2023/7/28
 */
@Slf4j
public final class SampleUtils {


    /**
     * isNullOr0
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isNullOr0(Object obj) {
        if (Objects.isNull(obj)) {
            return true;
        }
        if (obj instanceof BigDecimal) {
            return Objects.equals(BigDecimal.ZERO, new BigDecimal(String.valueOf(obj)));
        }

        if (obj instanceof Long) {
            return Objects.equals(0L, Long.valueOf(obj.toString()));
        }

        if (obj instanceof Integer) {
            return Objects.equals(0, Integer.valueOf(obj.toString()));
        }
        throw new RuntimeException("非法比较,请补充用例");
    }


    /**
     * 判断全部相等
     *
     * @param obj  str
     * @param objs stars
     * @return {@link boolean}
     */
    public static boolean allEquals(Object obj, Object... objs) {
        return objs.length == Arrays.stream(objs).filter(item -> Objects.equals(obj, item)).count();
    }

    /**
     * 存在相等的
     *
     * @param obj  obj
     * @param objs stars
     * @return {@link boolean}
     */
    public static boolean existEquals(Object obj, Object... objs) {
        return Arrays.asList(objs).contains(obj);
    }


    /**
     * 判断是否是int
     *
     * @param str str
     * @return boolean
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.valueOf(str);
            return true;
        } catch (Exception e) {
            log.info("[SampleUtils#isInteger] str:{} 非int", str);
            return false;
        }
    }

    /**
     * 判断是否是long
     *
     * @param str str
     * @return {@link boolean}
     */
    public static boolean isLong(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.valueOf(str);
            return true;
        } catch (Exception e) {
            log.info("[SampleUtils#isLong] str:{} 非long", str);
            return false;
        }
    }


}
