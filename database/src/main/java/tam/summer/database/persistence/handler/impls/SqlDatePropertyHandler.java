package tam.summer.database.persistence.handler.impls;

import tam.summer.database.persistence.handler.PropertyHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class SqlDatePropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Date.class);
    }

    @Override
    public Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException {
        return rs.getDate(columnName);
    }

    @Override
    public void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException {
        ps.setDate(paramIndex, (Date) param);
    }
}
