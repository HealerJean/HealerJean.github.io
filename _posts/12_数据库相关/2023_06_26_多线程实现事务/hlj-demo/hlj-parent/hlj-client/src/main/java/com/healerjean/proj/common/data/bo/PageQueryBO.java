package com.healerjean.proj.common.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zhangyujin
 * @date 2023/6/14  14:14.
 */
@Accessors(chain = true)
@Data
public class PageQueryBO<T> implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3015419757742187536L;

    /**
     * 当前页数
     */
    private Long currPage;

    /**
     * 分页
     */
    private Long pageSize;

    /**
     * 是否进行 count 查询
     * 默认不需要
     */
    private Boolean searchCountFlag;


    /**
     * 查询数据
     */
    private T data;

    /**
     * 默认分页参数
     */
    public PageQueryBO() {
        this.currPage = 1L;
        this.pageSize = 10L;
        this.searchCountFlag = true;
    }
}
