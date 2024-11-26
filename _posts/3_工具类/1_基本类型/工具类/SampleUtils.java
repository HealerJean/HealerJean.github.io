package com.healerjean.proj.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
     * 全部相等
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
     * 所有属性都不为null
     *
     * @param objs stars
     * @return {@link boolean}
     */
    public static boolean allNotNull(Object... objs) {
        if (Objects.isNull(objs)) {
            return false;
        }
        return Arrays.stream(objs).allMatch(Objects::nonNull);
    }

    /**
     * 所有属性存在null
     *
     * @param objs stars
     * @return {@link boolean}
     */
    public static boolean existNotNull(Object... objs) {
        if (Objects.isNull(objs)) {
            return false;
        }
        return Arrays.stream(objs).anyMatch(Objects::nonNull);
    }


    /**
     * 所有属性存在null
     *
     * @param objs stars
     * @return {@link boolean}
     */
    public static boolean existNull(Object... objs) {
        if (Objects.isNull(objs)) {
            return false;
        }
        return Arrays.stream(objs).anyMatch(Objects::isNull);
    }




    @Test
    public void test() {
        System.out.println(allEquals("123", "123", "123"));
        // true
        System.out.println(allEquals("123", "456", "123"));
        // false

        System.out.println(existEquals("123", "123", "45"));
        // true
        System.out.println(existEquals("123", "456", "678"));
        // false

        System.out.println(allNotNull("23423", "2234"));
        // true
        System.out.println(allNotNull(null, "2234"));
        // false

        System.out.println(existNull(null, "2234"));
        // true
        System.out.println(existNull("23423", "2234"));
        // false

        System.out.println(existNotNull(null, "2234"));
        // true
        System.out.println(existNotNull(null, null));
        // false

    }

}
