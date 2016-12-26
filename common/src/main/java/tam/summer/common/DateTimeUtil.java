package tam.summer.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanqimin on 2015/11/26.
 */
public class DateTimeUtil {
    private static Logger logger = LogManager.getLogger(DateTimeUtil.class);

    private static Map<String, SimpleDateFormat> cache = new HashMap<>();

    /**
     * 使用 Date 输出 yyyy-MM-dd HH:mm:ss.SSS 格式的日期字符串
     *
     * @param date
     * @return
     */
    public static String convertToString(Date date) {
        SimpleDateFormat simpleDateFormat;
        String           format = DateTimeFormat.DATE_TIME;
        if (cache.containsKey(format)) {
            simpleDateFormat = cache.get(format);
        } else {
            simpleDateFormat = new SimpleDateFormat(format);
            cache.put(format, simpleDateFormat);
        }

        return simpleDateFormat.format(date);
    }

    /**
     * 使用Date输出format格式的日期字符串
     *
     * @param date
     * @param format tam.summer.common.DateTimeFormat
     * @return
     */
    public static String convertToString(
            Date date,
            String format) {
        SimpleDateFormat simpleDateFormat;
        if (cache.containsKey(format)) {
            simpleDateFormat = cache.get(format);
        } else {
            simpleDateFormat = new SimpleDateFormat(format);
            cache.put(format, simpleDateFormat);
        }

        return simpleDateFormat.format(date);
    }

    /**
     * 使用 yyyy-MM-dd HH:mm:ss.SSS 格式的日期字符串dateStr输出Date
     *
     * @param dateStr 日期字符串
     * @return
     * @throws ParseException
     */
    public static Date convertToDate(String dateStr)
            throws ParseException {
        return convertToDate(dateStr, DateTimeFormat.DATE_TIME);
    }

    /**
     * 使用format格式的日期字符串dateStr输出Date
     *
     * @param dateStr 日期字符串
     * @param format  tam.summer.common.DateTimeFormat
     * @return
     * @throws ParseException
     */
    public static Date convertToDate(
            String dateStr,
            String format)
            throws ParseException {
        if (ValidateUtil.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat;
        if (cache.containsKey(format)) {
            simpleDateFormat = cache.get(format);
        } else {
            simpleDateFormat = new SimpleDateFormat(format);
            cache.put(format, simpleDateFormat);
        }
        return simpleDateFormat.parse(dateStr);
    }
}
