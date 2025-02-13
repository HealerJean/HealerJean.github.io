package com.healerjean.proj.common.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * OrderByBO
 *
 * @author zhangyujin
 * @date 2023/6/14  17:40.
 */
@Accessors(chain = true)
@Data
public class OrderByBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 160148478613404512L;

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