package com.healerjean.proj.data.bo;

import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * EsBaseQueryBO
 *
 * @author zhangyujin
 * @date 2024-04-26 04:04:12
 */
@Data
public class EsBaseQueryBO<B extends EsBaseBO> {

    /**
     * 查询实体
     */
    B baseEntity;

    /**
     * 是否使用es滚动查询
     */
    boolean isScroll = false;
    /**
     * 滚动id
     */
    String scrollId = null;

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * boolQueryBuilder
     */
    private BoolQueryBuilder boolQueryBuilder;

    /**
     * searchSourceBuilder
     */
    private SearchSourceBuilder searchSourceBuilder;
}
