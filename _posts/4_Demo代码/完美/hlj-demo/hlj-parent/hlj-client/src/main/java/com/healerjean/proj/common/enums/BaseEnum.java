package com.healerjean.proj.common.enums;

import com.healerjean.proj.data.vo.EnumLabelDTO;

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
     * baseEnum
     *
     */
    default EnumLabelDTO toBaseDto() {
        return new EnumLabelDTO().setCode(this.getCode()).setDesc(this.getDesc());
    }


}
