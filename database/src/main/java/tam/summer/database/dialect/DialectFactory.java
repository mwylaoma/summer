package tam.summer.database.dialect;

import tam.summer.database.exception.DatabaseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class DialectFactory {
    private final static Map<String, IDialect> map;

    static {
        map = new HashMap<>();
        map.put("sqlserver", new MSSQLDialect());
        map.put("mysql", new MYSQLDialect());
    }

    private DialectFactory() {
    }

    public static IDialect getInstance(String dbType) {
        IDialect iDialect = map.get(dbType);
        if (iDialect == null) throw new DatabaseException(String.format("暂时不支持 %s 类型的数据库！", dbType));
        return iDialect;
    }
}
