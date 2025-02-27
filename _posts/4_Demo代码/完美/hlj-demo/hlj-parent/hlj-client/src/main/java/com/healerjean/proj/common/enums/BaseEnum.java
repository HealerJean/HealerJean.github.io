package com.healerjean.proj.common.enums;

import com.healerjean.proj.common.data.vo.EnumLabelVO;

/**
 * BaseEnum
 *
 * @author zhangyujin
 * @date 2024/2/21
 */
public interface BaseEnum {

    /**
     * code
     */
    String getCode();

    /**
     * 描述
     */
    String getDesc();


    /**
     * 枚举名称
     */
    String name();

    /**
     * getName
     *
     * @return {@link String}
     */
    default String getName() {
        return name();
    }


    /**
     * match
     *
     * @param code code
     * @return {@link boolean}
     */
    default boolean matchCode(String code) {
        if (code == null) {
            return false;
        }
        return code.equals(getCode());
    }

    /**
     * match
     *
     * @param name name
     * @return {@link boolean}
     */
    default boolean matchName(String name) {
        if (name == null) {
            return false;
        }
        return name.equals(getName());
    }

    /**
     * baseEnum
     */
    default EnumLabelVO toBaseDto() {
        return new EnumLabelVO().setCode(this.getCode()).setDesc(this.getDesc()).setName(this.getName());
    }


}
