package com.summer.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tam.summer.common.config.Config;

/**
 * Abstract method to be sub-classed by various caching technologies.
 */
public abstract class CacheProvider {
    public static CacheProvider INSTANCE;
    private final static Logger logger = LogManager.getLogger(CacheProvider.class);

    static {
        //Config通过Spring注入
        if (Config.Instance.getCacheEnabled()) {
            try {
                //加载缓存驱动
                Class cacheClass = Class.forName(Config.Instance.getCacheProvider());
                CacheProvider.INSTANCE = (CacheProvider) cacheClass.newInstance();
            } catch (ClassNotFoundException e) {
                logger.error("Could not found CacheProvider Class.", e);
            } catch (InstantiationException e) {
                logger.error("Could not init CacheProvider Class.", e);
            } catch (IllegalAccessException e) {
                logger.error("Could not access CacheProvider Class.", e);
            }
        }
    }

    /**
     * Returns a cached item. Can return null if not found.
     *
     * @param group group of caches - this is a name of a table for which query results are cached
     * @param key   key of the item.
     * @return a cached item. Can return null if not found.
     */
    public abstract <T> T getCache(
            String group,
            String key);

    /**
     * Adds item to cache.
     *
     * @param group group name of cache.
     * @param key   key of the item.
     * @param cache cache item to add to cache.
     */
    public void addCache(
            String group,
            String key,
            Object cache) {
        addCache(group, key, cache, -1);
    }

    public abstract void addCache(
            String group,
            String key,
            Object cache,
            int expired);

    /**
     * remove item from cache.
     *
     * @param group group name of cache.
     * @param key   key of the item.
     */
    public abstract void removeCache(
            String group,
            String key);

    public abstract void doFlush(CacheEvent event);


    /**
     * Flash cache.
     *
     * @param event type of caches to flush.
     */
    public final void flush(CacheEvent event) {
        doFlush(event);
        if (logger.isDebugEnabled()) {
            String message = "Cache purged: " + (event.getType() == CacheEvent.CacheEventType.ALL ? "all caches" : "group '" + event.getGroup() + "'");
            logger.debug(message);
        }
    }
}
