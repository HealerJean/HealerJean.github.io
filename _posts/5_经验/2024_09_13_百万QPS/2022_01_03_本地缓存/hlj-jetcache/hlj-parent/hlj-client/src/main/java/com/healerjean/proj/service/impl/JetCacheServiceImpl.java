package com.healerjean.proj.service.impl;

import com.alicp.jetcache.Cache;
import com.healerjean.proj.config.JetCacheConfig;
import com.healerjean.proj.service.JetCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * JetCacheServiceImpl
 *
 * @author zhangyujin
 * @date 2023/11/21
 */
@Service
public class JetCacheServiceImpl implements JetCacheService {

    /**
     * jetCacheConfig
     */
    @Resource
    private JetCacheConfig jetCacheConfig;

    @Autowired
    private Cache<Long, Object> userCache;

}
