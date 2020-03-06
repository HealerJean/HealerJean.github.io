package com.healerjean.proj.data.common.result;


import com.healerjean.proj.data.common.paging.Pagenation;

/**
 * 返回集合值分页结果
 *
 * @param <T>
 * @author YuYue
 */
public class PageListResult<T> extends ListResult<T> {

    private static final long serialVersionUID = -5229900490913912833L;

    public static <E> PageListResult<E> newPageListResult() {
        return new PageListResult<E>();
    }

    /**
     * @return 辅助分页查询结果
     */
    private Pagenation pagenation;

    public Pagenation getPagenation() {
        return pagenation;
    }

    public PageListResult<T> setPagenation(Pagenation pagenation) {
        this.pagenation = pagenation;
        return this;
    }
}
