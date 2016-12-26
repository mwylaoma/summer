package tam.summer.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2015/12/4.
 */
public class CollectionUtil {
    /**
     * 把数组按一定大小拆分为多个数组
     *
     * @param array
     * @param bccSize
     * @return
     */
    public static List<Object[]> splitArray(Object[] array, int bccSize) {
        List<Object[]> retLst = new ArrayList<>();
        Object         arr[]  = null;
        for (int j = 1, len = array.length + 1; j < len; j++) {
            if (j == 1) {
                if (len > bccSize) { //初始化数组大小
                    arr = new Object[bccSize];
                } else {
                    arr = new Object[len - 1];
                }
            }
            arr[j - (bccSize * retLst.size()) - 1] = array[j - 1];  //给数组赋值
            if (j % bccSize == 0) {
                retLst.add(arr);  //数组填值满后放到集合中
                if (len - j - 1 > bccSize) {
                    arr = new Object[bccSize];
                    //不允许数组有空值创建最后一个数组的大小(如果都要一定大小可以去掉)
                } else {
                    arr = new Object[len - (bccSize * retLst.size()) - 1];
                }
            } else if (j == len - 1) {
                retLst.add(arr);//最后一个数组可能没有规定大小
            }
        }

        return retLst;
    }

    /**
     * 把集合按一定大小拆分为多个集合
     *
     * @param list
     * @param splitSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        List<List<T>> retLst = new ArrayList<>();
        List<T>       subLst = null;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0 || (i + 1) % splitSize == 1) {
                if (i != 0) {
                    retLst.add(subLst);
                }
                subLst = new ArrayList<>();
            }

            if (subLst != null) {
                subLst.add(list.get(i));
                if (i == list.size() - 1) {
                    retLst.add(subLst);
                }
            }
        }
        return retLst;
    }
}
