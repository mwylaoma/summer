package tam.summer.database.persistence.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理接口
 * Created by tanqimin on 2016/1/29.
 */
public interface PropertyHandler {
    boolean match(
            Class<?> propType);

    Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException;

    void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException;
}
