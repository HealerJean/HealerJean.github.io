package com.healerjean.proj.common.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * OrderByDTO
 *
 * @author zhangyujin
 * @date 2023/6/14  17:39.
 */
@Accessors(chain = true)
@Data
public class OrderByDTO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8429234471653779225L;

    /**
     * 顺序
     */
    private Integer sortId;

    /**
     * 排序属性
     */
    private String property;
    /**
     * 排序方向 （ascFlag、orderFields 二选一）
     */
    private Boolean ascFlag;

    /**
     * 指定排序方向-自定义顺序 （ascFlag、orderFields 二选一）
     */
    private String orderFields;

    /**
     * 空在前 true 空在后 false
     */
    private Boolean nullBeforeFlag;

}