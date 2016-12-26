package tam.summer.database.persistence.handler.impls;

import tam.summer.database.persistence.handler.PropertyHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class ObjectPropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return true;
    }

    @Override
    public Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException {
        return rs.getObject(columnName);
    }

    @Override
    public void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException {
        ps.setObject(paramIndex, param);
    }
}
