package tam.summer.database.persistence.handler.impls;

import tam.summer.database.persistence.handler.PropertyHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class BooleanPropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Boolean.TYPE) || propType.equals(Boolean.class);
    }

    @Override
    public Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException {
        return Boolean.valueOf(rs.getBoolean(columnName));
    }

    @Override
    public void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException {
        ps.setBoolean(paramIndex, (Boolean) param);
    }
}
