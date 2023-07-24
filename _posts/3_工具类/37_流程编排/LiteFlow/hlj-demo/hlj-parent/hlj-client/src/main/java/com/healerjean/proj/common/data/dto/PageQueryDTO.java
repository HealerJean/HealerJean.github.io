package com.healerjean.proj.common.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zhangyujin
 * @date 2023/6/14  14:26.
 */
@Accessors(chain = true)
@Data
public class PageQueryDTO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6352246288500328255L;

    /**
     * 当前页数
     */
    private Long currPage;

    /**
     * 分页
     */
    private Long pageSize;


    /**
     * 默认分页参数
     */
    public PageQueryDTO() {
        this.currPage = 1L;
        this.pageSize = 10L;
    }
}
