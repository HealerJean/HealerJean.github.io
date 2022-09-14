package com.jd.merchant.sign.rpc.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/11/5  9:10 上午.
 * @description java分页工具
 */
public class JavaPageUtils {

    public static <T> PageDto<T> toPageDto(List<T> list, int pageNow, int pageSize) {
        if (list == null || list.isEmpty()) {
            return new PageDto<>(null, pageNow, pageSize, 0, 0);
        }

        int totalCount = list.size();
        int pageCount;
        if (totalCount % pageSize == 0) {
            pageCount = totalCount / pageSize;
        } else {
            pageCount = totalCount / pageSize + 1;
        }

        if (pageNow > pageCount) {
            return new PageDto<>(null, pageNow, pageSize, totalCount, pageCount);
        }

        int startIndex = (pageNow - 1) * pageSize;
        // 结束索引
        int endIndex = pageNow == pageCount ? startIndex + pageSize : totalCount;
        List<T> pageList = list.subList(startIndex, endIndex);
        return new PageDto<>(pageList, pageNow, pageSize, totalCount, pageCount);
    }


    @Data
    @Accessors(chain = true)
    public static class PageDto<T> {
        private List<T> datas;
        /**
         * 当前页码数
         */
        private Integer pageNow;
        /**
         * 每页显示的记录数
         */
        private Integer pageSize;
        /**
         * 总记录数
         */
        private Integer totalCount;
        /**
         * 一共多少页
         */
        private Integer pageCount;

        public PageDto(List<T> datas, Integer pageNow, Integer pageSize, Integer totalCount, Integer pageCount) {
            this.datas = datas;
            this.pageNow = pageNow;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.pageCount = pageCount;
        }

        private PageDto(List<T> datas) {
            this.datas = datas;
        }

        private PageDto() {
        }
    }

}
