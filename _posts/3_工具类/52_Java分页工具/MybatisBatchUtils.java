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
     * queryAllByLimit
     *
     * @param dbPageFunction     function
     * @param queryWrapper queryWrapper
     * @param pageSize     pageSize
     * @return {@link List<R>}
     */

    public static <Q, R> List<R> queryAllByLimit(BiFunction<Page<R>, Q, IPage<R>> dbPageFunction,
                                                 Q queryWrapper,
                                                 long pageSize) {

        IPage<R> initPage = dbPageFunction.apply(new Page<>(1, 0, true), queryWrapper).setSize(pageSize);
        List<R> result = new ArrayList<>();
        for (int i = 1; i <= initPage.getPages(); i++) {
            IPage<R> page = dbPageFunction.apply(new Page<>(i, pageSize, false), queryWrapper);
            if (CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            result.addAll(page.getRecords());
        }
        return result;
    }


    /**
     * queryAllByIdSize
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
     * queryAllByPoolLimit
     *
     * @param executorService 线程池
     * @param function        分页函数
     * @param queryWrapper    查询条件
     * @param pageSize        pageSize 分页大小
     * @param coverFunction   coverFunction 对象转化
     * @return {@link List< Future< List<T>>>}
     */
    public static <Q, R, T> List<Future<List<T>>> queryAllByPoolLimit(CompletionService<List<T>> executorService,
                                                                      BiFunction<Page<R>, Q, IPage<R>> function,
                                                                      Q queryWrapper,
                                                                      int pageSize,
                                                                      Function<List<R>, List<T>> coverFunction) {

        IPage<R> initPage = function.apply(new Page<>(1, 0, true), queryWrapper).setSize(pageSize);

        List<Future<List<T>>> result = new ArrayList<>();
        for (int i = 1; i <= initPage.getPages(); i++) {
            int finalI = i;
            Future<List<T>> future = executorService.submit(() -> {
                IPage<R> page = function.apply(new Page<>(finalI, pageSize, false), queryWrapper);
                return coverFunction.apply(page.getRecords());
            });
            result.add(future);
        }
        return result;
    }


    /**
     * queryAllByPoolIdSub
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
