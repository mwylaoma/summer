package tam.summer.database.persistence.handler.impls;

import tam.summer.database.persistence.handler.PropertyHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class BytePropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Byte.TYPE) || propType.equals(Byte.class);
    }

    @Override
    public Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException {
        return Byte.valueOf(rs.getByte(columnName));
    }

    @Override
    public void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException {
        ps.setByte(paramIndex, (Byte) param);
    }
}
