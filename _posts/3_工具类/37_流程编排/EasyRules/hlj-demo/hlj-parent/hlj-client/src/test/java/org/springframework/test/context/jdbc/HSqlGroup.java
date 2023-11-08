package org.springframework.test.context.jdbc;

import java.lang.annotation.*;

/**
 * HSqlGroup
 * @author zhangyujin
 * @date 2023/6/15  15:14
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HSqlGroup {
    /**
     * value
     *
     * @return NoTXSql[]
     */
    HSql[] value();
}