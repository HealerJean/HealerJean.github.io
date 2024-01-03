package com.healerjean.proj.utils.diff;

/**
 * DifferenceType
 *
 * @author zhangyujin
 * @date 2023/12/20
 */
public enum DifferenceType {
    /**
     * 新增
     */
    ADDED(),

    /**
     * 删除
     */
    REMOVED(),

    /**
     * 修改
     */
    MODIFIED(),

    /**
     * 无变化
     */
    UNTOUCHED()
}
