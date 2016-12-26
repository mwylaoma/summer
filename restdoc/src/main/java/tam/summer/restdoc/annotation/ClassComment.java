package tam.summer.restdoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * API 类型、方法、字段注释
 * Created by tanqimin on 2015/11/14.
 */
@java.lang.annotation.Target({
        ElementType.TYPE
})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface ClassComment {
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
