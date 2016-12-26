package tam.summer.database.dialect;

import tam.summer.database.meta.Sql;

import java.util.regex.Matcher;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class MSSQLDialect
        extends DefaultDialect {
    @Override
    public String getDialectName() {
        return "mssql";
    }

    @Override
    public Sql selectTop(
            int currentPage,
            int recordsPerPage,
            String sql,
            Object[] params) {
        if (currentPage == 1 && recordsPerPage == 1) {
            //如果sql本身只返回一个结果
            if (selectSinglePattern.matcher(sql).find()) {
                return new Sql(sql, params);
            }
        }

        int offset = recordsPerPage * (currentPage - 1);

        String  orderBys = null;
        Matcher om       = orderPattern.matcher(sql);
        if (om.find()) {
            orderBys = sql.substring(om.end(), sql.length());
            sql = sql.substring(0, om.start());
        }

        //mssql ROW_NUMBER分页必须要至少一个ORDER BY
        if (orderBys == null) {
            orderBys = "CURRENT_TIMESTAMP";
        }

        Sql querySql = new Sql();
        querySql.append("SELECT paginate_alias.* FROM (SELECT ROW_NUMBER() OVER (ORDER BY ");
        querySql.append(orderBys);
        querySql.append(") rownumber,");

        Matcher sm = selectPattern.matcher(sql);
        if (sm.find()) {
            querySql.append(sql.substring(sm.end()), params);
        } else {
            querySql.append(sql, params);
        }

        // T-SQL offset starts with 1, not like MySQL with 0;
        querySql.append(") paginate_alias WHERE rownumber BETWEEN ? AND ?", offset + 1, recordsPerPage + offset);

        return querySql;
    }
}
