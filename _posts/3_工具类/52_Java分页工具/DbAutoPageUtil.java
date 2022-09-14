package com.jd.baoxian.merchant.route.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库批量查询工具类
 *
 * @author healerejean
 * @date 2022/8/25  18:53.
 */
@Slf4j
public class DbAutoPageUtil {

    /**
     * 数字1
     */
    private final static int ONE = 1;
    /**
     * 页数
     */
    private final static int FIRST_PAGE_NO = 1;

    /**
     * 分页大小
     */
    private final static int PAGE_SIZE = 10000;

    /**
     * 查询分页函数
     *
     * @param <Q> 查询query
     * @param <T> 返回结果
     */
    public interface SelectFunction<Q, T> {
        /**
         * mapper 分页查询
         *
         * @param q        查询query
         * @param offset   偏移量
         * @param pageSize 分页大小
         * @return
         */
        List<T> selectDataPage(Q q, long offset, long pageSize);
    }


    /**
     * 批量分页查询全部数据
     *
     * @param query    查询条件query
     * @param function mapper查询函数
     * @param <Q>      查询条件泛型
     * @param <T>      返回结果泛型
     * @return 全部数据结果
     */
    public static <Q, T> List<T> selectByQuery(Q query, SelectFunction<Q, T> function) {
        List<T> result = new ArrayList<>();
        int pageNo = FIRST_PAGE_NO;
        while (true) {
            long start = System.currentTimeMillis();
            int offset = (pageNo - ONE) * PAGE_SIZE;
            List<T> list = function.selectDataPage(query, offset, PAGE_SIZE);
            if (CollectionUtils.isEmpty(list)) {
                return result;
            }
            pageNo = pageNo + 1;
            result.addAll(list);
            log.info("[DbPageUtil#]selectByQuery method:{}, size:{}, cost:{}", function.getClass().getName(), result.size(), System.currentTimeMillis() - start);
        }
    }

}
