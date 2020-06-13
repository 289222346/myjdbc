package com.myjdbc.api.annotations;

import java.lang.annotation.*;

/**
 * @author 陈文
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyApiModel {

    /**
     * 实体的数据库名称
     * <p>
     * 默认情况下，使用类名.
     */
    String value() default "";

    /**
     * 提供该类的详细说明.
     */
    String description() default "";
}
