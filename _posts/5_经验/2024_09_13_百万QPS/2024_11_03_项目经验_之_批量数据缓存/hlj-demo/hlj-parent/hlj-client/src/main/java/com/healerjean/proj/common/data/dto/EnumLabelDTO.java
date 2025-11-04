package com.healerjean.proj.common.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * EnumLabelDTO
 *
 * @author zhangyujin
 * @date 2024/7/22
 */
@Accessors(chain = true)
@Data
public class EnumLabelDTO implements Serializable {


    /**
     *serialVersionUID
     */
    private static final long serialVersionUID = -7879775138034484824L;

    /**
     * name
     */
    private String name;

    /**
     * code
     */
    private String code;

    /**
     * desc
     */
    private String desc;
}
