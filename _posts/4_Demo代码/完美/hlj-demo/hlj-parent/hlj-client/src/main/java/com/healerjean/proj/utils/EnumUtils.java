package com.healerjean.proj.utils;

import com.healerjean.proj.cache.EnumCache;
import com.healerjean.proj.common.enums.BaseEnum;
import com.healerjean.proj.data.vo.EnumLabelDTO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * EnumUtils
 *
 * @author zhangyujin
 * @date 2024/7/22
 */
public class EnumUtils {


    /**
     * getEnumLabelByCode
     *
     * @param clazz clazz
     * @param code  code
     * @return {@link EnumLabelDTO}
     */
    public static  <E extends Enum> EnumLabelDTO getLabelByCode(Class<E> clazz, String code) {
        E e = EnumCache.findByValue(clazz, code);
        if (Objects.isNull(e)) {
            return null;
        }
        boolean flag = e instanceof BaseEnum;
        if (Boolean.FALSE.equals(flag)) {
            return null;
        }
        BaseEnum baseEnum = (BaseEnum) e;
        return baseEnum.toBaseDto();
    }


    /**
     * getEnumLabelByCode
     *
     * @param clazz clazz
     * @param name  name
     * @return {@link EnumLabelDTO}
     */
    public static  <E extends Enum> EnumLabelDTO getLabelByName(Class<E> clazz, String name) {
        E e = EnumCache.findByName(clazz, name);
        if (Objects.isNull(e)) {
            return null;
        }
        boolean flag = e instanceof BaseEnum;
        if (Boolean.FALSE.equals(flag)) {
            return null;
        }
        BaseEnum baseEnum = (BaseEnum) e;
        return baseEnum.toBaseDto();
    }


    public static List<EnumLabelDTO> getlabelValues(String simpleClassName) {
        Class<? extends Enum> clazz = EnumCache.findClassByName(simpleClassName);
        if (Objects.isNull(clazz)) {
            return Collections.emptyList();
        }

        EnumSet enumSet = EnumSet.allOf(clazz);
        return (List<EnumLabelDTO>) enumSet.stream().map(e -> {
            BaseEnum baseEnum = (BaseEnum) e;
            return baseEnum.toBaseDto();
        }).collect(Collectors.toList());
    }



}
