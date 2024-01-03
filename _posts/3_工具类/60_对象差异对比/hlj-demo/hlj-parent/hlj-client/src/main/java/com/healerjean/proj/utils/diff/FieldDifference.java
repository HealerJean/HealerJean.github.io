package com.healerjean.proj.utils.diff;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

/**
 * FieldDifference
 *
 * @author zhangyujin
 * @date 2023/12/20
 */
@Getter
@Setter
public class FieldDifference {

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段类型
     */
    private Type type;

    /**
     * 快照值
     */
    private Object snapshotValue;

    /**
     * 当前值
     */
    private Object tracValue;

    /**
     * 差异类型
     */
    private DifferenceType differenceType;
}


