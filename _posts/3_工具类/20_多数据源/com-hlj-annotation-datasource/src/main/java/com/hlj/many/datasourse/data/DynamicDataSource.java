package com.hlj.many.datasourse.data;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description  获取动态数据源
 * @Date   2018/4/24 下午6:11.
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
