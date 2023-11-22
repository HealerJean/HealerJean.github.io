package com.jdd.baoxian.core.trade.merchant.service.utils;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述: 内存分页
 *
 * @author zhangyujin
 * @date 2023/6/5 13:25
 */
public final class MemoryPageUtil {


    /**
     * 内存分页
     *
     * @param pageSize    总条数
     * @param currentPage 当期页
     * @param list        全量list
     * @return memory
     */
    public static <T> MemoryPageResult<T> page(Integer pageSize, Integer currentPage, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new MemoryPageResult<>(0, pageSize, 0, currentPage, Collections.emptyList());
        }
        //默认为1
        currentPage = currentPage == null || currentPage < 1 ? 1 : currentPage;
        Integer totalCount = list.size();
        int totalPage = (totalCount + pageSize - 1) / pageSize;
        List<T> pageList = list.stream().skip((long) (currentPage - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        return new MemoryPageResult<>(totalCount, pageSize, totalPage, currentPage, pageList);
    }

    /**
     * 内存分页结果
     */
    @Data
    public static class MemoryPageResult<T> {
        /**
         * 总记录数
         */
        private Integer totalCount;
        /**
         * 每页记录数
         */
        private Integer pageSize;
        /**
         * 总页数
         */
        private Integer totalPage;
        /**
         * 当前页数
         */
        private Integer currPage;
        /**
         * 列表数据
         */
        private List<T> list;

        /**
         * 构造器
         *
         * @param totalCount 条数
         * @param pageSize   size
         * @param totalPage  current
         * @param currPage   cur
         * @param list       list
         */
        public MemoryPageResult(Integer totalCount, Integer pageSize, Integer totalPage, Integer currPage, List<T> list) {
            this.totalCount = totalCount;
            this.pageSize = pageSize;
            this.totalPage = totalPage;
            this.currPage = currPage;
            this.list = list;
        }
    }
}
