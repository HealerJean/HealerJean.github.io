package com.healerjean.proj.hotcache.factory;

import com.healerjean.proj.hotcache.datasource.SnapshotDataSource;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * HotCacheFactoryInitializer
 *
 * @author zhangyujin
 * @date 2025/11/4
 */
@Configuration
public class SnapshotFactoryInitializer {

    /**
     * snapshotDataSources
     */
    @Resource
    private List<SnapshotDataSource<?>> snapshotDataSources;

    @PostConstruct
    public void registerService() {
        snapshotDataSources.forEach(snapshotDataSource -> SnapshotFactory.getInstance().registerSnapshotDataSource(snapshotDataSource.getDatesetKeyEnum().getCode(), snapshotDataSource));
    }

}