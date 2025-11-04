package com.healerjean.proj.common.data.bo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * PageBO
 *
 * @author zhangyujin
 * @date 2023/6/14  11:54.
 */
@Data
public class PageBO<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1411218404349777400L;

    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 每页记录数
     */
    private Long pageSize;
    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 当前页数
     */
    private Long currPage;

    /**
     * 列表数据
     */
    private List<T> list;


    /**
     * 默认
     */
    private PageBO() {
    }

    /**
     * 构造器
     *
     * @param totalCount 条数
     * @param pageSize   size
     * @param totalPage  current
     * @param currPage   cur
     * @param list       list
     */
    public PageBO(Long totalCount, Long pageSize, Long totalPage, Long currPage, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.currPage = currPage;
        this.list = list;
    }


    /**
     * 无数据
     */
    public static <T> PageBO<T> none(Long pageSize, Long currenPage) {
        return new PageBO<>(0L, pageSize, 0L, currenPage, Collections.emptyList());
    }



    /**
     * 无数据
     */
    public static <T> PageBO<T> page(Page<?> page, List<T> list) {
        return new PageBO<>(page.getTotal(), page.getSize(), page.getPages(), page.getCurrent(), list);
    }
}
