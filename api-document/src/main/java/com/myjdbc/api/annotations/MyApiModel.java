package com.myjdbc.api.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 陈文
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
//@IDAutoGenerator
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

//    /**
//     * ID自动生成器工作方式
//     */
//    @AliasFor(attribute = "type", annotation = IDAutoGenerator.class)
//    IDAutoGenerator.Type type() default IDAutoGenerator.Type.DEFAULT;
}
