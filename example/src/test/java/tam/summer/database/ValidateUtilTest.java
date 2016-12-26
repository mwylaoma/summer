package tam.summer.database;

import org.junit.Assert;
import org.junit.Test;
import tam.summer.common.ValidateUtil;
import tam.summer.database.meta.Sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2016/1/7.
 */
public class ValidateUtilTest {

    @Test
    public void isBlank() {
        List<String> list1 = new ArrayList<>();
        Assert.assertTrue(ValidateUtil.isBlank(list1));
        list1.add("aaa");
        Assert.assertTrue(ValidateUtil.isNotBlank(list1));

        String[] array1 = new String[]{};
        Assert.assertTrue(ValidateUtil.isBlank(array1));
    }

    @Test
    public void test(){
        Assert.assertTrue( 10 % 5 == 0);
    }
}
