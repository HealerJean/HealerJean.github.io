package com.hlj.moudle.cache.config.cachelistener;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.event.CacheManagerEventListener;
import net.sf.ehcache.event.CacheManagerEventListenerFactory;

import java.util.Properties;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/4  下午5:07.
 * 类描述：
 */
public class CustomerCacheManagerEventListenerFactory extends CacheManagerEventListenerFactory {
    @Override
    public CacheManagerEventListener createCacheManagerEventListener(CacheManager cacheManager, Properties properties) {
        return new CustomerCacheManagerEventListener(cacheManager);
    }
}