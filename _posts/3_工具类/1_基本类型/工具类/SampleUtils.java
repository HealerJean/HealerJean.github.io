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



}
