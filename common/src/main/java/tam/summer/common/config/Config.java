package tam.summer.common.config;

/**
 * Created by tanqimin on 2015/11/14.
 */
public class Config {
    public static Config Instance = null;

    private boolean showSql       = false;
    private String  dbType        = "sqlserver";
    private boolean cacheEnabled  = false;
    private String  cacheProvider = "com.summer.cache.ehcache.EHCacheProvider";

    public boolean getShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public String getCacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(String cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public static void initConfig(){

    }
}
