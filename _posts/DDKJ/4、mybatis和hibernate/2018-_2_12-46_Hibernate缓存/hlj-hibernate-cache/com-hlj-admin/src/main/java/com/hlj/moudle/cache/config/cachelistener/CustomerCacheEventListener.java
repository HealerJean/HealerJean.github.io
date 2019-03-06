package com.hlj.moudle.cache.config.cachelistener;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/4  下午5:07.
 * 类描述：
 */
public class CustomerCacheEventListener implements CacheEventListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 缓存移除的时候进入
     * @param ehcache
     * @param element
     * @throws CacheException
     */
    @Override
    public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
        logger.info("cache removed. key = {}, value = {}", element.getObjectKey(), element.getObjectValue());
    }

    /**
     * 放入缓存的时候调用该放阿飞
     * @param ehcache
     * @param element
     * cache put. key = com.hlj.entity.db.demo.DemoEntity#1, value = org.hibernate.cache.ehcache.internal.strategy.AbstractReadWriteEhcacheAccessStrategy$Item@e99a9dd
     * @throws CacheException
     */
    @Override
    public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
        logger.info("cache put. key = {}, value = {}", element.getObjectKey(), element.getObjectValue());
    }

    /**
     * 缓存更新进入
     * @param ehcache
     * @param element
     * @throws CacheException
     */
    @Override
    public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
        logger.info("cache updated. key = {}, value = {}", element.getObjectKey(), element.getObjectValue());
    }

    @Override
    public void notifyElementExpired(Ehcache ehcache, Element element) {
        logger.info("cache expired. key = {}, value = {}", element.getObjectKey(), element.getObjectValue());
    }

    @Override
    public void notifyElementEvicted(Ehcache ehcache, Element element) {
        logger.info("cache evicted. key = {}, value = {}", element.getObjectKey(), element.getObjectValue());
    }

    @Override
    public void notifyRemoveAll(Ehcache ehcache) {
        logger.info("all elements removed. cache name = {}", ehcache.getName());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public void dispose() {
        logger.info("cache dispose.");
    }
}