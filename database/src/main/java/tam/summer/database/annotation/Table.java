package tam.summer.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tanqimin on 2015/10/28.
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Table {
    /**
     * 数据表名称
     *
     * @return
     */
    String value() default "";

    /**
     * 是否使用缓存？
     *
     * @return
     */
    boolean cached() default false;

    /**
     * 缓存过期时间 默认在更新时过期，或者在缓存配置文件中设置过期时间
     *
     * @return
     */
    int expired() default -1;
}
