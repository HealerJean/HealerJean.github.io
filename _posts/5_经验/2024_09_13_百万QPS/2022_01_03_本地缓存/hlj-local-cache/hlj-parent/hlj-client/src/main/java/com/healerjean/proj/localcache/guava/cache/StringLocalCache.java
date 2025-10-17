package com.healerjean.proj.localcache.guava.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * StringLocalCache
 * @author zhangyujin
 * @date 2023/1/3  13:30.
 */
@Slf4j
@Component
public class StringLocalCache extends AbstractLocalCacheManager<String, List<String>> {

    /**
     * @param s s
     * @return load
     */
    @Override
    protected List<String> load(String s) {
        return null;
    }

    /**
     * load
     *
     * @param keys keys
     * @return Map<String, String>
     */
    @Override
    protected Map<String, List<String>> load(Set<String> keys) {
        return null;
    }


    /**
     * loadAll
     *
     * @return Map<String, String>
     */
    @Override
    protected Map<String, List<String>> loadAll() {
        return null;
    }


    /**
     * 每5分钟更新一次缓存
     */
    @Scheduled(fixedRate = 1 * 60 * 1000)
    @Override
    public void refreshAll() {
        super.refreshAll();
    }

}
