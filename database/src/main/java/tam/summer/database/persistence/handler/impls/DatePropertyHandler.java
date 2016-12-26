package tam.summer.database.persistence.handler.impls;

import tam.summer.database.persistence.handler.PropertyHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class DatePropertyHandler
        implements PropertyHandler {

    @Override
    public boolean match(
            Class<?> propType) {
        return propType.equals(Date.class);
    }

    @Override
    public Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        if (timestamp == null) {
            return null;
        }
        return new java.util.Date(timestamp.getTime());
    }

    @Override
    public void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException {
        if (param == null) {
            ps.setTimestamp(paramIndex, null);
        } else {
            ps.setTimestamp(paramIndex, new Timestamp(((java.util.Date) param).getTime()));
        }

    }
}
