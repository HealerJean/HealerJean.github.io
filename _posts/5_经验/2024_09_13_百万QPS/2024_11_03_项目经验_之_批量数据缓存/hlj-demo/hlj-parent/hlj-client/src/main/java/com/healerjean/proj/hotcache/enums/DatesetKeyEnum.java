package com.healerjean.proj.hotcache.enums;

/**
 * DatesetKeyEnum
 *
 * @author zhangyujin
 * @date 2025/11/4
 */

import com.healerjean.proj.hotcache.model.UserTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * DatesetKeyEnum
 *
 * @author zhangyujin
 * @date 2025-11-04 03:11:52
 */
@AllArgsConstructor
@Getter
public enum DatesetKeyEnum {
    USER_TAG("user_tag", UserTag.class, "用户标签"),
    ;

    /**
     * code
     */
    private final String code;

    private final Class<?> clazz;
    /**
     * desc
     */
    private final String desc;


    /**
     * getHotCacheKeyEnumByCode
     *
     * @param code code
     * @return {@link DatesetKeyEnum}
     */
    public static DatesetKeyEnum getDatesetKeyEnum(String code) {
        return Arrays.stream(DatesetKeyEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }
}

