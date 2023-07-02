package com.healerjean.proj.utils.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

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


    /**
     * queryAll
     *
     * @param function     function
     * @param queryWrapper queryWrapper
     * @param pageSize     pageSize
     * @return {@link List<R>}
     */
    public static <Q, R, T> List<Future<List<T>>> queryAll(CompletionService<List<T>> executorService,
                                                           BiFunction<Page<R>, Q, IPage<R>> function,
                                                           Q queryWrapper,
                                                           int pageSize,
                                                           Function<List<R>, List<T>> coverFunction) {
        IPage<R> initPage = function.apply(new Page<>(1, 0), queryWrapper).setSize(pageSize);

        List<Future<List<T>>> result = new ArrayList<>();
        for (int i = 1; i <= initPage.getPages(); i++) {
            int finalI = i;
            Future<List<T>> future = executorService.submit(() -> {
                IPage<R> page = function.apply(new Page<>(finalI, pageSize), queryWrapper);
                return coverFunction.apply(page.getRecords());
            });
            result.add(future);
        }
        return result;
    }


}
