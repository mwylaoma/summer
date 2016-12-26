package tam.summer.restdoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tanqimin on 2015/11/15.
 */
@java.lang.annotation.Target({
        ElementType.PARAMETER
})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface ParamComment {
    /**
     * 名称
     *
     * @return
     */
    String value() default "";

    /**
     * 描述
     *
     * @return
     */
    String description() default "";
}
