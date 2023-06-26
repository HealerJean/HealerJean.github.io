package com.jd.merchant.business.platform.core.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.BiFunction;

/**
 * MybatisPlusQueryUtils
 *
 * @author zhangyujin
 * @date 2023/6/25$  12:05$
 */
public final class MybatisBatchUtils {

 /**
     * queryAll
     *
     * @param function function
     * @param q        q
     * @param pageSize pageSize
     * @return {@link List<R>}
     */
    public static <Q, R> List<R> queryAll(Function<Q, List<R>> function, Q q, int pageSize) {
        List<R> dbList = Lists.newArrayList();
        PageInfo<R> pageInfo;
        int pageNow = 1;
        while (true) {
            PageHelper.startPage(pageNow, pageSize);
            pageInfo = new PageInfo<>(function.apply(q));
            if (CollectionUtils.isEmpty(pageInfo.getList())) {
                break;
            }
            dbList.addAll(pageInfo.getList());
            if (pageInfo.getList().size() < pageSize) {
                break;
            }
            PageHelper.startPage(pageNow + 1, pageSize);
        }
        return dbList;
    }


    /**
     * queryAll
     *
     * @param function     function
     * @param queryWrapper queryWrapper
     * @param pageSize     pageSize
     * @return {@link List<R>}
     */

    public static <Q, R> List<R> queryAll(BiFunction<Page<R>, Q, IPage<R>> function,
                                          Q queryWrapper,
                                          int pageSize) {
        List<R> dbList = Lists.newArrayList();
        int pageNow = 1;
        Page<R> pageReq = new Page<>(pageNow, pageSize);
        while (true) {
            IPage<R> page = function.apply(pageReq, queryWrapper);
            if (CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            dbList.addAll(page.getRecords());
            if (page.getRecords().size() < pageSize) {
                break;
            }
            pageReq = new Page<>(pageNow + 1, pageSize);
        }
        return dbList;
    }



}
