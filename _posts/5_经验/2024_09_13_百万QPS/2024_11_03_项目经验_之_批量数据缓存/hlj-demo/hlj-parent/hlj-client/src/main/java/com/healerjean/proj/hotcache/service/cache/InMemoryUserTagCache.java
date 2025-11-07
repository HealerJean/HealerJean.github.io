package com.healerjean.proj.hotcache.service.cache;

import com.healerjean.proj.hotcache.model.UserTag;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemoryUserTagCache
 *
 * @author zhangyujin
 * @date 2025/11/6
 */
@Component
public class InMemoryUserTagCache {

    private final Map<String, UserTag> cache = new ConcurrentHashMap<>();

    public void put(String userId, UserTag tag) {
        cache.put(userId, tag);
    }

    public UserTag get(String userId) {
        return cache.get(userId);
    }

    public void remove(String userId) {
        cache.remove(userId);
    }

    public Map<String, UserTag> getAll() {
        return new HashMap<>(cache);
    }
}