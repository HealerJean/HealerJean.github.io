package com.healerjean.proj.data.manager.es.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.healerjean.proj.data.bo.EsOrderLogQueryBO;
import com.healerjean.proj.data.bo.EsTradeLogBO;
import com.healerjean.proj.data.converter.TradeLogConverter;
import com.healerjean.proj.data.manager.es.EsTradeLogManager;
import com.healerjean.proj.data.po.EsTradeLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * EsTradeLogManagerImpl
 *
 * @author zhangyujin
 * @date 2024/3/22
 */
@Slf4j
@Service
public class EsTradeLogManagerImpl extends EsEntityManagerImpl<EsTradeLog, EsTradeLogBO, EsOrderLogQueryBO> implements EsTradeLogManager {


    /**
     * 列表查询-CkOrderLog
     *
     * @param queryBo queryBo
     * @return boolean
     */
    @Override
    public List<EsTradeLogBO> queryOrderLogList(EsOrderLogQueryBO queryBo) {
        SearchSourceBuilder searchSourceBuilder = searchSourceBuilder(queryBo);
        queryBo.setSearchSourceBuilder(searchSourceBuilder);
        List<EsTradeLog> pos = super.searchList(queryBo);
        if (CollectionUtils.isEmpty(pos)){
            return Collections.emptyList();
        }
        pos.sort(Comparator.comparing(EsTradeLog::getTraceSortId));
        return TradeLogConverter.INSTANCE.convertTradeLogPoToBoList(pos);
    }



    /**
     * 删除-EsTradeLog
     *
     * @param ckOrderLogQuery ckOrderLogQuery
     * @return boolean
     */
    @Override
    public void batchDeleteTradeLog(EsOrderLogQueryBO ckOrderLogQuery) {
        BoolQueryBuilder boolQueryBuilder = boolQueryBuilder(ckOrderLogQuery);
        ckOrderLogQuery.setBoolQueryBuilder(boolQueryBuilder);
        batchDeleteDocument(ckOrderLogQuery);
    }


    /**
     * searchSourceBuilder
     *
     * @param entityParam entityParam
     * @return {@link SearchSourceBuilder}
     */
    public SearchSourceBuilder searchSourceBuilder(EsOrderLogQueryBO entityParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(this.boolQueryBuilder(entityParam));
        searchSourceBuilder.sort(new FieldSortBuilder("createdTime").order(SortOrder.ASC));
        return searchSourceBuilder;
    }


    /**
     * searchSourceBuilder
     *
     * @param query query
     * @return {@link BoolQueryBuilder}
     */
    public BoolQueryBuilder boolQueryBuilder(EsOrderLogQueryBO query){
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (query.getInValidateTimeStart() != null){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("inValidateTime").gte(query.getInValidateTimeStart() ));
        }

        if (query.getInValidateTimeEnd() != null){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("inValidateTime").lte(query.getInValidateTimeEnd() ));
        }

        if (query.getCreateTimeStart() != null){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createdTime").gte(query.getCreateTimeStart() ));
        }

        if (query.getCreateTimeEnd() != null){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createdTime").lte(query.getCreateTimeEnd() ));
        }

        EsTradeLogBO baseEntity = query.getBaseEntity();
        if (Objects.isNull(baseEntity)) {
            return boolQueryBuilder;
        }
        JSONObject entityJson = JSON.parseObject(JSON.toJSONString(baseEntity));
        entityJson.forEach((key, value) -> {
            if (value == null || StringUtils.isBlank(value.toString())) {
                return;
            }
            if (key.toLowerCase().contains("name")) {
                //模糊查询
                boolQueryBuilder.must(QueryBuilders.wildcardQuery(key, "*" + value + "*"));
            } else {
                //字段等于匹配
                boolQueryBuilder.must(QueryBuilders.termQuery(key, value));
            }
        });
        return boolQueryBuilder;
    }

}
