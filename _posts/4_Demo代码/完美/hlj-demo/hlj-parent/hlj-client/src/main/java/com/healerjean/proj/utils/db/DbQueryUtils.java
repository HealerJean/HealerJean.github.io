package com.healerjean.proj.utils.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.healerjean.proj.common.data.bo.OrderByBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 数据库查询工具
 *
 * @author zhangyujin
 * @date 2025/2/13
 */
public class DbQueryUtils {

    /**
     * orderBy
     *
     * @param queryWrapper queryWrapper
     * @param orderByList  orderByList
     */

    public static <T> void orderBy(QueryWrapper<T> queryWrapper, List<OrderByBO> orderByList) {
        if (CollectionUtils.isEmpty(orderByList)) {
            return;
        }
        orderByList.sort(Comparator.comparing(OrderByBO::getSortId));
        orderByList.forEach(item -> {

            // 1、自定义排序
            if (StringUtils.isNotBlank(item.getOrderFields())){
                String orderBySql = String.format("field(%s, %s) ", item.getProperty(), item.getOrderFields());
                queryWrapper.orderBy(true, true, orderBySql);
                return;
            }

            // 2、系统默认排序
            if (Objects.isNull(item.getNullBeforeFlag())) {
                queryWrapper.orderBy(Boolean.TRUE, item.getAscFlag(), item.getProperty());
                return;
            }

            String property = item.getProperty();
            // 升序、null在前
            if (Boolean.TRUE.equals(item.getAscFlag()) && Boolean.TRUE.equals(item.getNullBeforeFlag())) {
                queryWrapper.orderBy(Boolean.TRUE, item.getAscFlag(), item.getProperty());
                return;
            }

            // 升序、null在后
            if (Boolean.TRUE.equals(item.getAscFlag()) && Boolean.FALSE.equals(item.getNullBeforeFlag())) {
                String orderBySql = String.format("case when %s is null then 1 else 0 end, %s ", property, property);
                queryWrapper.orderBy(true, item.getAscFlag(), orderBySql);
                return;
            }

            // 降序、null在前
            if (Boolean.FALSE.equals(item.getAscFlag()) && Boolean.TRUE.equals(item.getNullBeforeFlag())) {
                String orderBySql = String.format("case when %s is null then 0 else 1 end, %s ", property, property);
                queryWrapper.orderBy(true, item.getAscFlag(), orderBySql);
                return;
            }

            // 降序、null在后
            if (Boolean.FALSE.equals(item.getAscFlag()) && Boolean.FALSE.equals(item.getNullBeforeFlag())) {
                queryWrapper.orderBy(Boolean.TRUE, item.getAscFlag(), item.getProperty());
            }

        });
    }
}
