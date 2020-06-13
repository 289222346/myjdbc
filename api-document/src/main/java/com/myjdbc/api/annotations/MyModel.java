package com.myjdbc.api.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 陈文
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyModel {

    /**
     * 提供该类的详细说明
     */
    @AliasFor(attribute = "description")
    String value() default "";

    /**
     * 提供该类的详细说明.
     */
    @AliasFor(attribute = "value")
    String description() default "";

    /**
     * 实体的数据库名称
     * <p>
     * 默认情况下，使用类名.
     */
    String name() default "";

}
