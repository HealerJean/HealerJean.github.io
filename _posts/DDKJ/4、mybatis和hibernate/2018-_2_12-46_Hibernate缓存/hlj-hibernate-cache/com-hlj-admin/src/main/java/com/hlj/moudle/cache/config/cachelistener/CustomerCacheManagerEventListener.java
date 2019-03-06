package com.hlj.moudle.cache.config.cachelistener;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Status;
import net.sf.ehcache.event.CacheManagerEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/4  下午5:06.
 * 类描述：
 */
public class CustomerCacheManagerEventListener implements CacheManagerEventListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final CacheManager cacheManager;

    public CustomerCacheManagerEventListener(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void init() throws CacheException {
        logger.info("init ehcache...");
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public void dispose() throws CacheException {
        logger.info("ehcache dispose...");
    }

    @Override
    public void notifyCacheAdded(String s) {
        logger.info("cacheAdded... {}", s);
        logger.info(cacheManager.getCache(s).toString());
    }

    @Override
    public void notifyCacheRemoved(String s) {

    }
}