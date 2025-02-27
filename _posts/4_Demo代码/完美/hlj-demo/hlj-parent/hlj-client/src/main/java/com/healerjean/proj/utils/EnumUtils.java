package com.healerjean.proj.utils;

import com.healerjean.proj.cache.EnumCache;
import com.healerjean.proj.common.data.vo.EnumLabelVO;
import com.healerjean.proj.common.enums.BaseEnum;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
     * @return {@link EnumLabelVO}
     */
    public static  <E extends Enum> EnumLabelVO getLabelByCode(Class<E> clazz, String code) {
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
     * @return {@link EnumLabelVO}
     */
    public static  <E extends Enum> EnumLabelVO getLabelByName(Class<E> clazz, String name) {
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


    /**
     * getEnumLabelByCode
     *
     * @param className className
     * @return {@link EnumLabelVO}
     */
    public static List<EnumLabelVO> getLabelByClazz(String className) {
        List<Enum> enums = EnumCache.findClassByName(className);
        if (CollectionUtils.isEmpty(enums)){
            return Collections.emptyList();
        }
        return enums.stream().map(item -> {
            BaseEnum baseEnum = (BaseEnum) item;
            return baseEnum.toBaseDto();
        }).collect(Collectors.toList());
    }


}
