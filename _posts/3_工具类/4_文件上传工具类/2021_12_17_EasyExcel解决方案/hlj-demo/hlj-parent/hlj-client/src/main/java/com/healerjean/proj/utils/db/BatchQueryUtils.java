package com.healerjean.proj.utils.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
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
public final class BatchQueryUtils {

    /**
     * 大数据量-db分页查询全部
     *
     * @param function     function
     * @param queryWrapper queryWrapper
     * @param pageSize     pageSize
     * @return {@link List<R>}
     */
    public static <Q, R> List<R> queryDbAllByLimit(BiFunction<Page<R>, Q, IPage<R>> function,
                                                   Q queryWrapper,
                                                   long pageSize) {
        IPage<R> initPage = function.apply(new Page<>(1, 1, true), queryWrapper).setSize(pageSize);
        List<R> result = new ArrayList<>();
        for (int i = 1; i <= initPage.getPages(); i++) {
            IPage<R> page = function.apply(new Page<>(i, pageSize, false), queryWrapper);
            if (CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            result.addAll(page.getRecords());
        }
        return result;
    }


    /**
     * 大数据量-分页查询全部
     *
     * @param function function
     * @param Q        1
     * @param pageSize pageSize
     * @return {@link List<R>}
     */

    public static <Q, R> List<R> queryAllByLimit(Function<PageQueryBO<Q>, PageBO<R>> function,
                                                 Q q,
                                                 long pageSize) {
        PageQueryBO<Q> pageQuery = new PageQueryBO<>(1L, 1L);
        pageQuery.setData(q);
        Long totalPage = function.apply(pageQuery).getTotalPage();
        List<R> result = new ArrayList<>();
        for (long i = 1; i <= totalPage; i++) {
            pageQuery.setPageSize(pageSize);
            pageQuery.setCurrPage(i);
            PageBO<R> pageBo = function.apply(pageQuery);
            if (CollectionUtils.isEmpty(pageBo.getList())) {
                break;
            }
            result.addAll(pageBo.getList());
        }
        return result;
    }


    /**
     * 大数据量-IdSize查询全部
     *
     * @param function function
     * @param query    query
     * @param minMax   minMax
     * @return {@link List<R>}
     */
    public static <Q, R> List<R> queryAllByIdSize(BiFunction<IdQueryBO, Q, List<R>> function,
                                                  Q query,
                                                  IdQueryBO minMax) {
        List<R> result = Lists.newArrayList();
        Long minId = minMax.getMinId();
        Long size = minMax.getSize();
        while (minId != null) {
            IdQueryBO idQueryBO = new IdQueryBO(minId, size);
            List<R> dbList = function.apply(idQueryBO, query);
            if (CollectionUtils.isEmpty(dbList)) {
                break;
            }
            minId = idQueryBO.getMaxId();
            result.addAll(dbList);
        }
        return result;
    }


    /**
     * 大数据量-Id区间查询全部
     *
     * @param function function
     * @param query    query
     * @param minMax   minMax
     * @return {@link List<R>}
     */
    public static <Q, R> List<R> queryAllByIdSub(BiFunction<IdQueryBO, Q, List<R>> function,
                                                 Q query,
                                                 IdQueryBO minMax) {
        List<R> result = Lists.newArrayList();
        Long minId = minMax.getMinId();
        Long maxId = minMax.getMaxId();
        Long size = minMax.getSize();
        for (long i = minId; i <= maxId; i = i + size) {
            long endId = Math.min(i + size, maxId);
            boolean maxEqualFlag = endId == maxId;
            IdQueryBO idQueryBO = new IdQueryBO(true, i, maxEqualFlag, endId, size);
            List<R> dbList = function.apply(idQueryBO, query);
            if (CollectionUtils.isEmpty(dbList)) {
                break;
            }
            result.addAll(dbList);
            if (maxEqualFlag) {
                break;
            }
        }
        return result;
    }


    /**
     * 大数据量-线程池limit查询
     *
     * @param executorService executorService
     * @param function        function
     * @param q               q
     * @param pageSize        pageSize
     */
    public static <Q, T> List<Future<List<T>>> queryAllByPoolLimit(CompletionService<List<T>> executorService,
                                                                   Function<PageQueryBO<Q>, PageBO<T>> function,
                                                                   Q q,
                                                                   long pageSize) {
        PageQueryBO<Q> pageQuery = new PageQueryBO<>(1L, 1L);
        pageQuery.setData(q);
        Long totalPage = function.apply(pageQuery).getTotalPage();
        List<Future<List<T>>> result = new ArrayList<>();
        for (long i = 1; i <= totalPage; i++) {
            PageQueryBO<Q> poolPageQuery = new PageQueryBO<>(i, pageSize);
            poolPageQuery.setData(q);
            Future<List<T>> future = executorService.submit(() -> {
                PageBO<T> pageBo = function.apply(poolPageQuery);
                return pageBo.getList();
            });
            result.add(future);
        }
        return result;
    }


    /**
     * 大数据量-线程池Id区间查询
     *
     * @param executorService 线程池
     * @param function        分页函数
     * @param query           查询条件
     * @param minMax          minMax 最小Id和最大Id
     * @param coverFunction   coverFunction 对象转化
     */
    public static <Q, R, T> List<Future<List<T>>> queryAllByPoolIdSub(CompletionService<List<T>> executorService,
                                                                      BiFunction<IdQueryBO, Q, List<R>> function,
                                                                      Q query,
                                                                      IdQueryBO minMax,
                                                                      Function<List<R>, List<T>> coverFunction) {
        Long minId = minMax.getMinId();
        Long maxId = minMax.getMaxId();
        Long size = minMax.getSize();
        List<Future<List<T>>> result = new ArrayList<>();
        for (long i = minId; i <= maxId; i = i + size) {
            long endId = Math.min(i + size, maxId);
            boolean maxEqualFlag = endId == maxId;
            IdQueryBO idQueryBO = new IdQueryBO(true, i, maxEqualFlag, endId, size);
            Future<List<T>> future = executorService.submit(() -> {
                List<R> list = function.apply(idQueryBO, query);
                return coverFunction.apply(list);
            });
            result.add(future);
            if (maxEqualFlag) {
                break;
            }
        }
        return result;
    }

}
