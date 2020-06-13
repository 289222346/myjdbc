package com.myjdbc.api.annotations;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @param {@code value} 是属性的描述,
 * @param {@code name} 是属性的别名（数据库名称）,
 * @param {@code hidden} 表示是否参与数据库映射（在映射时隐藏该属性）
 * @param {@code required} 表示是否是必填属性
 * @author 陈文
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyModelProperty {

    /**
     * 属性的描述
     */
    @AliasFor(attribute = "description")
    String value() default "";

    /**
     * 属性的描述
     */
    @AliasFor(attribute = "value")
    String description() default "";

    /**
     * 属性的别名（数据库名称）
     */
    String name() default "";

    /**
     * 是否参与数据库映射（在映射时隐藏该属性）
     * true:不参与数据库映射
     * false:参与数据库映射
     * 默认：false
     */
    boolean hidden() default false;

    /**
     * 是否是必填属性
     * true:必填
     * false:非必填
     * 默认:false;
     */
    boolean required() default false;

}
