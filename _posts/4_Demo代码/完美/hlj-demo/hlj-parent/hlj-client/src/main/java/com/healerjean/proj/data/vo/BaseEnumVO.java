package com.healerjean.proj.data.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DictionaryDTO
 *
 * @author zhangyujin
 * @date 2024/2/21
 */
@Accessors(chain = true)
@Data
public class BaseEnumVO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1504794630853926135L;


    /**
     * 字典编码
     */
    private String code;

    /**
     * 字典描述
     */
    private String desc;



}
