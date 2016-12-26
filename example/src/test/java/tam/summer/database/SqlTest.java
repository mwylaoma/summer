package tam.summer.database;

import org.junit.Assert;
import org.junit.Test;
import tam.summer.database.meta.Sql;

/**
 * Created by tanqimin on 2016/1/7.
 */
public class SqlTest {

    @Test
    public void where() {
        Sql sql = new Sql();
        sql.where();
        Assert.assertEquals(sql.getSql(), "WHERE 1 = 1");
    }
}
