package tam.summer.restdoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * API ModuleComment
 * Created by tanqimin on 2015/11/14.
 */
@java.lang.annotation.Target({
        ElementType.TYPE
})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface ModuleComment {
    /**
     * 名称
     *
     * @return
     */
    String value() default "";

    /**
     * 排列顺序
     *
     * @return
     */
    int sortIndex() default -1;
}
