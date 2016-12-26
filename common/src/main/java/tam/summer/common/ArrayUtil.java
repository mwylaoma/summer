package tam.summer.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tanqimin on 2016/1/22.
 */
public class ArrayUtil {
    public static String[] convert(Object... params) {
        if (params == null) {
            return new String[0];
        }
        int      paramSize = params.length;
        String[] strParams = new String[paramSize];
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                strParams[i] = null;
            } else if (params[i] instanceof Date) {
                strParams[i] = DateTimeUtil.convertToString((Date) params[i]);
            } else {
                strParams[i] = params[i].toString();
            }
        }
        return strParams;
    }

    public static <T> List<T> asList(T... params) {
        ArrayList<T> result = new ArrayList<>();
        if (params == null || params.length == 0) {
            return result;
        }
        for (T param : params) {
            result.add(param);
        }
        return result;
    }
}
