package com.healerjean.proj.hotcache.factory;

import com.healerjean.proj.hotcache.datasource.SnapshotDataSource;
import com.healerjean.proj.hotcache.enums.DataSerializerStrategyEnum;
import com.healerjean.proj.hotcache.enums.DatesetKeyEnum;
import com.healerjean.proj.hotcache.enums.StorageStrategyEnum;
import com.healerjean.proj.hotcache.service.serialization.DataSerializerStrategy;
import com.healerjean.proj.hotcache.service.serialization.FastjsonDataSerializerStrategy;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import com.healerjean.proj.hotcache.service.storage.impl.LocalStorageServiceStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringFactory
 *
 * @author zhangyujin
 * @date 2025/11/4
 */
public class SnapshotFactory {

    private static final Map<String, SnapshotDataSource<?>> STRING_SNAPSHOT_DATA_SOURCE_MAP = new ConcurrentHashMap<>();


    private static final Map<String, DataSerializerStrategy<?>> DATA_SERIALIZER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, StorageServiceStrategy> STORAGE_SERVICE_MAP = new ConcurrentHashMap<>();


    // 私有构造，防止外部 new
    private SnapshotFactory() {
    }

    // 单例
    public static SnapshotFactory getInstance() {
        return Holder.INSTANCE;
    }


    private static class Holder {
        private static final SnapshotFactory INSTANCE = new SnapshotFactory();
    }


    public SnapshotDataSource<?> getSnapshotDataSource(String key) {
        return STRING_SNAPSHOT_DATA_SOURCE_MAP.get(key);
    }


    /**
     * 获取序列话方式
     *
     * @param
     * @return {@link DataSerializerStrategy}
     */
    public DataSerializerStrategy<?> getDataSerializer(String datasetName, String serializerStrategy) {
        String cacheKey = datasetName + "-" + serializerStrategy;
        return DATA_SERIALIZER_MAP.computeIfAbsent(cacheKey, k -> {
            DatesetKeyEnum datesetKeyEnum = DatesetKeyEnum.getDatesetKeyEnum(datasetName);
            DataSerializerStrategyEnum dataSerializerStrategyEnum = DataSerializerStrategyEnum.getDataSerializerStrategyEnumByCode(serializerStrategy);
            switch (dataSerializerStrategyEnum) {
                case FAST_JSON:
                    return new FastjsonDataSerializerStrategy<>(datesetKeyEnum.getClazz());
                default:
                    throw new IllegalArgumentException("No DataSerializerProvider supports: " + serializerStrategy);
            }
        });

    }


    public StorageServiceStrategy getStorageServiceStrategy(String datasetName, String storageStrategy) {
        String cacheKey = datasetName + "-" + storageStrategy;
        return STORAGE_SERVICE_MAP.computeIfAbsent(cacheKey, k -> {
            StorageStrategyEnum storageStrategyEnum = StorageStrategyEnum.getStorageStrategyEnumByCode(storageStrategy);
            switch (storageStrategyEnum) {
                case LOCAL:
                    return new LocalStorageServiceStrategy();
                default:
                    throw new IllegalArgumentException("No StorageServiceProvider supports: " + storageStrategy);
            }
        });
    }



    /**
     * registerSnapshotDataSource
     */
    public <T> void registerSnapshotDataSource(String datasetName, SnapshotDataSource<T> snapshotDataSource) {
        STRING_SNAPSHOT_DATA_SOURCE_MAP.put(datasetName, snapshotDataSource);
    }


}
