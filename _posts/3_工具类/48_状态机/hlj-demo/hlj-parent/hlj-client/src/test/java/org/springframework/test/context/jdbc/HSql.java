package org.springframework.test.context.jdbc;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * interface-HSql
 * @author zhangyujin
 * @date 2023/6/15  15:14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(HSqlGroup.class)
public @interface HSql {
    /**
     * value
     *
     * @return value
     */
    @AliasFor("scripts")
    String[] value() default {};

    /**
     * scripts
     *
     * @return scripts
     */
    @AliasFor("value")
    String[] scripts() default {};

    /**
     * statements
     *
     * @return statements
     */
    String[] statements() default {};

    /**
     * executionPhase
     *
     * @return executionPhase
     */
    ExecutionPhase executionPhase() default ExecutionPhase.BEFORE_TEST_METHOD;

    /**
     * config
     *
     * @return config
     */
    SqlConfig config() default @SqlConfig;

    /**
     * ExecutionPhase
     */
    enum ExecutionPhase {
        /**
         * ExecutionPhase
         */
        BEFORE_TEST_METHOD,
        AFTER_TEST_METHOD;

        ExecutionPhase() {
        }
    }
}