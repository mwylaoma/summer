package tam.summer.database.cache;

import com.summer.cache.CacheEvent;
import com.summer.cache.CacheProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tam.summer.common.Constant;

import java.util.Arrays;


/**
 * This is a main cache facade. It could be architected in the future to add more cache implementations besides OSCache.
 */
public enum QueryCache {
    INSTANCE;

    public static final  String QUERY_DEF_KEY = "_query";
    private final static Logger logger        = LogManager.getLogger(QueryCache.class);
    private final CacheProvider cacheProvider;

    //singleton
    private QueryCache() {
        cacheProvider = CacheProvider.INSTANCE;
    }

    /**
     * This class is a singleton, get an instance with this method.
     *
     * @return one and only one instance of this class.
     */
    public static QueryCache instance() {
        return INSTANCE;
    }

    static void logAccess(
            String group,
            String query,
            Object[] params,
            String access) {
        if (logger.isDebugEnabled()) {
            StringBuilder log = new StringBuilder().append(access).append(", group: {").append(group).append("}, query: {").append(query).append("} ");
            if (params != null && params.length > 0) {
                log.append(", params: ").append('{');
                int paramCnt = params.length;
                for (int i = 0; i < paramCnt; i++) {
                    Object param = params[i];
                    log.append(param == null ? "null" : param.toString());
                    if (i + 1 < paramCnt) {
                        log.append("}, {");
                    }
                }
                log.append('}');
            }
            logger.debug(log.toString());
        }
    }

    /**
     * Adds an item to cache. Expected some lists of objects returned from "select" queries.
     *
     * @param tableName - name of table.
     * @param query     query text
     * @param params    - list of parameters for a query.
     * @param cache     object to cache.
     */
    public void add(
            String tableName,
            String type,
            String query,
            Object[] params,
            Object cache) {
        add(tableName, type, query, params, cache, -1);
    }

    public void add(
            String tableName,
            String type,
            String query,
            Object[] params,
            Object cache,
            int expired) {
        String group = getGroup(tableName);
        cacheProvider.addCache(group, getKey(group, type, query, params), cache, expired);
    }

    private String getGroup(
            String tableName) {
        return QUERY_DEF_KEY + Constant.CONNECTOR + tableName;
    }

    /**
     * Returns an item from cache, or null if nothing found.
     *
     * @param tableName name of table.
     * @param query     query text.
     * @param params    list of query parameters, can be null if no parameters are provided.
     * @return cache object or null if nothing found.
     */
    public <T> T get(
            String tableName,
            String type,
            String query,
            Object[] params) {

        String group = getGroup(tableName);
        String key   = getKey(group, type, query, params);
        Object item  = cacheProvider.getCache(group, key);
        if (item == null) {
            logAccess(group, query, params, "Miss");
            return null;
        } else {
            logAccess(group, query, params, "Hit");
            return (T) item;
        }
    }

    private String getKey(
            String group,
            String type,
            String query,
            Object[] params) {
        return group + Constant.CONNECTOR + type + Constant.CONNECTOR + query + Constant.CONNECTOR + (params == null ? null : Arrays.asList(params).toString());
    }

    public void remove(
            String tableName,
            String type,
            String query,
            Object[] params) {
        String group = getGroup(tableName);
        cacheProvider.removeCache(group, getKey(group, type, query, params));
    }

    /**
     * This method purges (removes) all caches associated with a table, if caching is enabled and
     * a corresponding model is marked cached.
     *
     * @param tableName table name whose caches are to be purged.
     */
    public void purge(String tableName) {
        cacheProvider.flush(new CacheEvent(getGroup(tableName), getClass().getName()));
    }

    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }
}

