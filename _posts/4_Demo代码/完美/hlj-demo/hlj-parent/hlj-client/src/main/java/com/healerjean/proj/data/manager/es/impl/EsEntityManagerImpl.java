package com.healerjean.proj.data.manager.es.impl;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.common.anno.EsIndex;
import com.healerjean.proj.data.bo.EsBaseBO;
import com.healerjean.proj.data.bo.EsBaseQueryBO;
import com.healerjean.proj.data.manager.es.EsEntityManager;
import com.healerjean.proj.data.po.EsBasePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * EsEntityManagerImpl
 *
 * @author zhangyujin
 * @date 2024/3/22
 */
@Slf4j
public abstract class EsEntityManagerImpl<P extends EsBasePO, B extends EsBaseBO, EQ extends EsBaseQueryBO<B>> implements EsEntityManager<P,  B, EQ> {


    /**
     * restHighLevelClient
     */
    @Resource
    protected RestHighLevelClient restHighLevelClient;


    /**
     * saveDocument
     *
     * @param po baseEntity
     * @return {@link boolean}
     */
    @Override
    public boolean saveDocument(P po) {
        String indexName = this.getIndexName();
        String id = po.getUuid();
        if (StringUtils.isBlank(id)) {
            log.info("保存ES时uuid为空!,对象:{}", JSON.toJSONString(po));
            throw new RuntimeException("保存ES时uuid为空!");
        }
        if (StringUtils.isNotBlank(id) && this.exists(indexName, id)) {
            this.updateDocument(po);
        } else {
            this.insertDocument(po);
        }
        return false;
    }


    /**
     * 批量创建文档
     *
     * @param baseEntities baseEntities
     */
    @Override
    public void batchInsertDocument(List<P> baseEntities) {
        String indexName = this.getIndexName();
        BulkRequest bulkRequest = new BulkRequest();
        baseEntities.forEach(baseEntity ->{
            String id = baseEntity.getUuid();
            String data = JSON.toJSONString(baseEntity);
            // IndexRequest 存在返回id集合， UpdateResponse 存在则更新
            IndexRequest request = new IndexRequest(indexName).id(id).source(data, XContentType.JSON);
            // UpdateRequest request = new UpdateRequest(indexName, "_doc", id);
            // request.doc(data, XContentType.JSON);
            //
            // // 设置Upsert，如果文档不存在则插入新文档
            // String sourceString = new String(org.elasticsearch.common.xcontent.json.JsonXContent.contentBuilder()
            //         .prettyPrint()
            //         .map(data)
            //         .bytes(), StandardCharsets.UTF_8);
            // request.upsert(sourceString, XContentType.JSON);
            bulkRequest.add(request);
        });
        BulkResponse response;
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("执行增加文档error：", e);
            throw new RuntimeException("批量增加es异常", e);
        }
        log.info("batchInsertDocument 状态：{}, size:{}", Objects.requireNonNull(response).status(), baseEntities.size());
    }


    /**
     * 添加文档
     *
     * @param baseEntity 基础实体对象
     */
    protected void insertDocument(P baseEntity) {
        String indexName = this.getIndexName();
        String id = baseEntity.getUuid();
        String data = JSON.toJSONString(baseEntity);
        IndexRequest indexRequest = new IndexRequest(indexName).id(id).source(data, XContentType.JSON);
        IndexResponse response;
        try {
            response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("执行增加文档error：", e);
            throw new RuntimeException("增加es异常", e);
        }
        log.info("创建文档:{},状态：{}", id, Objects.requireNonNull(response).status());
    }

    /**
     * 更新文档信息
     *
     * @param baseEntity baseEntity
     */
    protected void updateDocument(P baseEntity) {
        String indexName = this.getIndexName();
        String id = baseEntity.getUuid();
        String data = JSON.toJSONString(baseEntity);
        UpdateRequest updateRequest = new UpdateRequest(indexName, id);
        updateRequest.doc(data, XContentType.JSON);
        UpdateResponse response;
        try {
            response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("更新文档信息error：", e);
            throw new RuntimeException("更新es异常", e);
        }
        log.info("更新文档:{},信息状态：{}", id, Objects.requireNonNull(response).status());
    }


    /**
     * 删除文档
     *
     * @param idList 待删除文档的ID列表
     */
    public void deleteDocument(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        String indexName = this.getIndexName();
        for (String id : idList) {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
            DeleteResponse response;
            try {
                response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            } catch (Exception e) {
                log.error("删除文档信息error：", e);
                throw new RuntimeException("删除es异常", e);
            }
            log.info("删除状态：{}", Objects.requireNonNull(response).status());
        }
    }


    /**
     * 根据条件批量删除
     *
     * @param eq entityParam
     */
    @Override
    public void batchDeleteDocument(EQ eq) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(this.getIndexName());
        // 刷新索引以便立即看到删除效果
        request.setRefresh(true);
        request.setQuery(eq.getBoolQueryBuilder());
        log.info("[EsEntityManager#batchDeleteDocument] eq:{}", JSON.toJSONString(eq));
        BulkByScrollResponse response;
        try {
             response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("删除文档信息error：", e);
            throw new RuntimeException("删除es异常", e);
        }
        log.info("batchDeleteDocument count:{}", Objects.requireNonNull(response).getDeleted());

    }


    /**
     * 关键字搜索
     *
     * @param eq eq
     * @return {@link ArrayList<P>}
     */
    protected List<P> searchList(EQ eq) {
        try {
            SearchResponse searchResponse = searchListResponse(eq);
            return responeToList(searchResponse);
        } catch (Exception e) {
            log.error("ES关键字搜索失败：{}", JSON.toJSONString(eq), e);
            throw new RuntimeException("ES关键字搜索失败", e);
        }
    }




    /**
     * 返回搜索响应
     *
     * @param entityParam entityParam
     * @return {@link SearchResponse}
     */
    protected SearchResponse searchListResponse(EQ entityParam) {
        try {
            SearchSourceBuilder searchSourceBuilder = entityParam.getSearchSourceBuilder();
            searchSourceBuilder.from((entityParam.getPageIndex() - 1) * entityParam.getPageSize());
            searchSourceBuilder.size(entityParam.getPageSize());
            SearchRequest searchRequest = new SearchRequest(this.getIndexName());
            searchRequest.source(searchSourceBuilder);
            if (entityParam.isScroll()) {
                final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
                if (StringUtils.isBlank(entityParam.getScrollId())) {
                    searchRequest.scroll(scroll);
                    return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                } else {
                    SearchScrollRequest scrollRequest = new SearchScrollRequest(entityParam.getScrollId());
                    scrollRequest.scroll(scroll);
                    return restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                }
            }
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("ES关键字搜索失败：{}", JSON.toJSONString(entityParam), e);
            throw new RuntimeException("ES关键字搜索失败", e);
        }
    }


    /**
     * 判断文档是否存在
     *
     * @param indexName indexName
     * @param id        id
     * @return {@link boolean}
     */
    public boolean exists(String indexName, String id) {
        GetRequest request = new GetRequest(indexName, id);
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean exists = false;
        try {
            exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("判断文档是否存在error：", e);
            throw new RuntimeException("判断es异常", e);
        }
        return exists;
    }

    /**
     * 将搜索响应转换为实体列表
     *
     * @param searchResponse searchResponse
     * @return {@link ArrayList<P>}
     */
    protected List<P> responeToList(SearchResponse searchResponse) {
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            SearchHits hits = searchResponse.getHits();
            List<P> list = new ArrayList<>(hits.getHits().length);
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                P baseEntity = JSON.parseObject(sourceAsString, this.getBeClass());
                list.add(baseEntity);
            }
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 获取BE类的真实Class对象
     */
    private Class<P> getBeClass() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        return (Class<P>) actualTypeArgument;
    }

    /**
     * 获取实体类的索引名称
     */
    public String getIndexName() {
        Class<P> actualTypeArgument = getBeClass();
        EsIndex esIndex = actualTypeArgument.getAnnotation(EsIndex.class);
        if (null == esIndex) {
            throw new RuntimeException(String.format("请在实体类[%s]增加@EsIndex注解!", actualTypeArgument.getName()));
        }
        return esIndex.value();
    }

}
