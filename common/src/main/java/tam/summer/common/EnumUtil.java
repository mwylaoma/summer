package tam.summer.common;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class EnumUtil {
    public static <T extends Enum> T getEnum(
            Class<T> clazz,
            String enumStr) {
        if (enumStr == null) {
            return null;
        }
        T[] enums = clazz.getEnumConstants();
        for (T anEnum : enums) {
            if (ValidateUtil.equalsIgnoreCase(anEnum.toString(), enumStr)) {
                return anEnum;
            }
        }
        return null;
    }
}
