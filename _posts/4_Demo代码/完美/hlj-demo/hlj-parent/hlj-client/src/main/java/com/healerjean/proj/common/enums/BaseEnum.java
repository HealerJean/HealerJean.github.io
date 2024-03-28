package com.healerjean.proj.common.enums;

import com.healerjean.proj.data.vo.BaseEnumVO;

import java.util.Objects;

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
     * @param baseEnum baseEnum
     * @return DictionaryVO
     */
    default BaseEnumVO toBaseVO(BaseEnum baseEnum) {
        if (Objects.isNull(baseEnum)) {
            return null;
        }
        return new BaseEnumVO().setCode(baseEnum.getCode()).setDesc(baseEnum.getDesc());
    }


}
