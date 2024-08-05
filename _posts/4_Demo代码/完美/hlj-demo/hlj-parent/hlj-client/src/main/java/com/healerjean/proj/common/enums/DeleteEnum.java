package com.healerjean.proj.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * StatusEnum
 *
 * @author zhangyujin
 * @date 2024/2/21
 */
@AllArgsConstructor
@Getter
public enum DeleteEnum implements BaseEnum {

    VALID("valid", "生效"),
    TRASH("trash", "废弃"),
    ;

    /**
     * code
     */
    private final String code;

    /**
     * desc
     */
    private final String desc;


}
