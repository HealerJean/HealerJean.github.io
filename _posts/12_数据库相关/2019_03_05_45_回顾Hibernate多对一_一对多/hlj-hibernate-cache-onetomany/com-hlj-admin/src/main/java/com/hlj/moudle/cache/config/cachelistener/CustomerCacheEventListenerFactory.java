package com.hlj.moudle.cache.config.cachelistener;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/4  下午5:34.
 * 类描述：
 */
public class CustomerCacheEventListenerFactory extends CacheEventListenerFactory {

    @Override
    public CacheEventListener createCacheEventListener(final Properties properties) {
        return new CustomerCacheEventListener();
    }
}

