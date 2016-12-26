package tam.summer.database;

import org.springframework.jdbc.core.JdbcTemplate;
import tam.summer.common.config.Config;
import tam.summer.database.dialect.DialectFactory;
import tam.summer.database.dialect.IDialect;
import tam.summer.database.exception.ConfigException;
import tam.summer.database.exception.DatabaseException;

import javax.sql.DataSource;

/**
 * Created by tanqimin on 2015/11/4.
 */
public class DatabaseTemplate
        extends JdbcTemplate {
    private IDialect dialect;

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    public IDialect getDialect() {
        String dbType = getConfig().getDbType();
        if (dbType == null || dbType.length() == 0) {
            throw new DatabaseException("请设置数据库类型");
        }
        if (this.dialect == null) {
            this.dialect = DialectFactory.getInstance(dbType);
        }
        return this.dialect;
    }

    public Config getConfig() {
        if (Config.Instance == null) {
            throw new ConfigException("请先在Spring Context中注入Config");
        }
        return Config.Instance;
    }

    public void setConfig(Config config) {
        Config.Instance = config;
    }
}
