package com.healerjean.proj.hotcache.service.pull;

/**
 * 增量数据处理口子：业务方实现，决定拉出来的 data 怎么处理
 * <p>
 * 由 {@link IncrementLoadToMemoryService} 在拉取到一条增量数据后回调，
 * 业务方在实现里自由决定：放内存 Map / 写文件 / 转发 MQ / 交给下游服务 / ...
 *
 * @author zhangyujin
 * @date 2025/11/6
 */
public interface IncrementalDataHandler {

    /**
     * 处理一条增量数据
     *
     * @param datasetName 数据集
     * @param offset      Redis 中的 offset
     * @param data        Redis 中的 data 本体
     */
    void handle(String datasetName, long offset, String data);

}
